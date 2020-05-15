plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

val scriptUrl: String by extra

repositories {
    mavenLocal()
    apply(from = "$scriptUrl/maven-repo.gradle.kts")
    jcenter()
}

val drillJvmApiLibVersion: String by extra

val presetName: String =
    when {
        org.apache.tools.ant.taskdefs.condition.Os.isFamily(org.apache.tools.ant.taskdefs.condition.Os.FAMILY_MAC) -> "macosX64"
        org.apache.tools.ant.taskdefs.condition.Os.isFamily(org.apache.tools.ant.taskdefs.condition.Os.FAMILY_UNIX) -> "linuxX64"
        org.apache.tools.ant.taskdefs.condition.Os.isFamily(org.apache.tools.ant.taskdefs.condition.Os.FAMILY_WINDOWS) -> "mingwX64"
        else -> throw RuntimeException("Target ${System.getProperty("os.name")} is not supported")
    }

fun org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.currentTarget(
    name: String = presetName,
    config: org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit = {}
): org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget {
    val createTarget =
        (presets.getByName(presetName) as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTestsPreset).createTarget(
            name
        )
    targets.add(createTarget)
    config(createTarget)
    return createTarget
}

val libName = "agentOnlyForTest"

val JVM_TEST_TARGET_NAME = "agentOnlyForTestPurpose"


kotlin {
    currentTarget(JVM_TEST_TARGET_NAME) {
        binaries.sharedLib(libName, setOf(DEBUG))
        compilations["main"].defaultSourceSet {
            dependencies {
                implementation("com.epam.drill:jvmapi-native:$drillJvmApiLibVersion")
                implementation(project(":"))
            }
        }
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

tasks.named<org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest>("${JVM_TEST_TARGET_NAME}Test") {
    enabled = false
}
