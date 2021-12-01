import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.targets.native.tasks.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    kotlin("multiplatform")
    id("com.epam.drill.gradle.plugin.kni")
}

val scriptUrl: String by extra
val kniVersion: String by extra
repositories {
    mavenLocal()
    apply(from = "$scriptUrl/maven-repo.gradle.kts")
    mavenCentral()
}

val drillJvmApiLibVersion: String by extra
val nativeTargets = mutableSetOf<KotlinNativeTarget>()

kotlin {
    val targetFromPreset = targetFromPreset(
        preset = presets.getByName(HostManager.host.presetName) as AbstractKotlinNativeTargetPreset,
        name = "native"
    ) {
        binaries.sharedLib("agentOnlyForTest", setOf(DEBUG))
        compilations["main"].defaultSourceSet {
            dependencies {
                implementation("com.epam.drill:jvmapi:$drillJvmApiLibVersion")
                implementation("com.epam.drill.kni:runtime:$kniVersion")
                implementation(project(":"))
            }
        }
    }

    nativeTargets.add(targetFromPreset)

    kni {
        jvmTargets = sequenceOf(jvm())
        jvmtiAgentObjectPath = "test.Agent"
        nativeCrossCompileTarget = nativeTargets.asSequence()
    }

    jvm {
        compilations["main"].defaultSourceSet {
            dependencies {
                implementation(project(":"))
                implementation("com.epam.drill.kni:runtime:$kniVersion")
            }
        }
    }
    sourceSets {
        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
            }
        }
    }
}

tasks {
    withType<KotlinNativeTest> { enabled = false }
    val generateNativeClasses by getting {}
    named("compileKotlinNative") { dependsOn(generateNativeClasses) }

    val cleanExtraData by registering(Delete::class) {
        group = "build"
        nativeTargets.forEach {
            val path = "src/${it.name}Main/kotlin/"
            delete(file("${path}kni"))
        }
    }

    clean {
        dependsOn(cleanExtraData)
    }
}

