plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.epam.drill.cross-compilation")
    `maven-publish`
}

val scriptUrl: String by extra

apply(from = "$scriptUrl/git-version.gradle.kts")

repositories {
    mavenLocal()
    apply(from = "$scriptUrl/maven-repo.gradle.kts")
    jcenter()
}

val serializationRuntimeVersion: String by extra
val ktorUtilVersion: String by extra
val loggingVersion: String by extra
val klockVersion: String by extra
val drillJvmApiLibVersion: String by extra


val libName = "agentOnlyForTest"

val JVM_TEST_TARGET_NAME = "agentOnlyForTestPurpose"


kotlin {

    crossCompilation {
        common {
            defaultSourceSet {
                dependsOn(sourceSets.named("commonMain").get())
            }
            defaultSourceSet.dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serializationRuntimeVersion")
                implementation("io.ktor:ktor-utils-native:$ktorUtilVersion")
                implementation("com.epam.drill:jvmapi-native:$drillJvmApiLibVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-native:$serializationRuntimeVersion")
            }
        }
    }
    macosX64 {
        compilations["main"].defaultSourceSet {
            dependencies {
                implementation("com.soywiz.korlibs.klock:klock-macosx64:$klockVersion")
            }
        }
    }
    mingwX64 {
        compilations["main"].defaultSourceSet {
            dependencies {
                implementation("com.soywiz.korlibs.klock:klock-mingwx64:$klockVersion")
            }
        }
    }
    linuxX64 {
        compilations["main"].defaultSourceSet {
            dependencies {
                implementation("com.soywiz.korlibs.klock:klock-linuxx64:$klockVersion")
            }
        }
    }

    jvm("jvmAgent") {
        compilations["main"].defaultSourceSet {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationRuntimeVersion")
                implementation("com.soywiz.korlibs.klock:klock-jvm:$klockVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:$serializationRuntimeVersion")
            }
        }
        compilations["test"].defaultSourceSet {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationRuntimeVersion")
                implementation("com.soywiz.korlibs.klock:klock:$klockVersion")

            }
        }

        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
            }
        }
        all {
            languageSettings.useExperimentalAnnotation("io.ktor.utils.io.core.ExperimentalIoApi")
        }
    }
}
val linkJVMTIAgentTaskName = "link${libName.capitalize()}DebugShared${JVM_TEST_TARGET_NAME.capitalize()}"


tasks.withType<org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest> {
    dependsOn("test-agent:$linkJVMTIAgentTaskName")
    testLogging.showStandardStreams = true
    doFirst{
        val agent = project(":test-agent").buildDir
            .resolve("bin")
            .resolve("agentOnlyForTestPurpose")
            .resolve("agentOnlyForTestDebugShared")
            .listFiles().apply {
                this?.forEach { println(it.name) }
            }
            ?.first { it.extension == "dll" || it.extension == "so" || it.extension == "dylib" }
        jvmArgs("-agentpath:$agent")
    }
}

