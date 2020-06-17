import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.targets.native.tasks.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    kotlin("multiplatform")
}

val scriptUrl: String by extra

repositories {
    mavenLocal()
    apply(from = "$scriptUrl/maven-repo.gradle.kts")
    jcenter()
}

val drillJvmApiLibVersion: String by extra

kotlin {
    targetFromPreset(
        preset = presets.getByName(HostManager.host.presetName) as AbstractKotlinNativeTargetPreset,
        name = "native"
    ) {
        binaries.sharedLib("agentOnlyForTest", setOf(DEBUG))
        compilations["main"].defaultSourceSet {
            dependencies {
                implementation("com.epam.drill:jvmapi-native:$drillJvmApiLibVersion")
                implementation(project(":"))
            }
        }
        println(compilations["test"].binariesTaskName)
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
            }
        }

        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
            }
        }
    }
}

tasks.withType<KotlinNativeTest> {
    enabled = false
}
