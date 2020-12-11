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
    jcenter()
}
val kniOutputDir = "src/kni/kotlin"
val drillJvmApiLibVersion: String by extra

kotlin {
    targetFromPreset(
        preset = presets.getByName(HostManager.host.presetName) as AbstractKotlinNativeTargetPreset,
        name = "native"
    ) {
        binaries.sharedLib("agentOnlyForTest", setOf(DEBUG))
        compilations["main"].defaultSourceSet {
            kotlin.srcDir(file(kniOutputDir))
            dependencies {
                implementation("com.epam.drill:jvmapi:$drillJvmApiLibVersion")
                implementation("com.epam.drill.kni:runtime:$kniVersion")
                implementation(project(":"))
            }
        }
    }

    kni {
        jvmTargets = sequenceOf(jvm())
        srcDir = kniOutputDir
        jvmtiAgentObjectPath = "test.Agent"
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
        delete(kniOutputDir)
    }

    clean {
        dependsOn(cleanExtraData)
    }
}

