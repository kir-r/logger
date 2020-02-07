import org.jetbrains.kotlin.konan.target.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
    kotlin("multiplatform")
    `maven-publish`
}

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://dl.bintray.com/kotlin/kotlinx/")
    }
}

kotlin {

    val targetAttribute = Attribute.of("${project.group}.${project.name}.target", String::class.java)

    val commonNative = linuxX64("commonNative")

    targets.withType<KotlinNativeTarget> {
        attributes {
            attribute(targetAttribute, name)
        }
    }

    targets.withType<KotlinNativeTarget>().matching { it != commonNative }.all {
        compilations.all {
            if (!target.publishable) {
                defaultSourceSet.kotlin.setSrcDirs(emptyList<Any>())
            }
            defaultSourceSet {
                val main by commonNative.compilations
                dependsOn(main.defaultSourceSet)
            }
        }
    }

    linuxX64()
    macosX64()
    mingwX64()
    sourceSets {
        jvm {
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationRuntimeVersion")
                    implementation("io.github.microutils:kotlin-logging:$loggingVersion")
                }
                compilations["test"].defaultSourceSet {
                    dependencies {
                        implementation(kotlin("test-junit"))
                    }
                }
            }
        }
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationRuntimeVersion")
                implementation("io.github.microutils:kotlin-logging-common:$loggingVersion")
            }
        }

        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
            }
        }

        named("commonNativeMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serializationRuntimeVersion")
                implementation("io.ktor:ktor-utils-native:$ktorUtilVersion")
            }
        }
    }
}
