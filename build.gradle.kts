plugins {
    kotlin("multiplatform") version "1.3.70"
    id("com.epam.drill.cross-compilation") version "0.16.0"
    `maven-publish`
}
apply(from = "https://raw.githubusercontent.com/Drill4J/build-scripts/master/git-version.gradle.kts")
repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://dl.bintray.com/kotlin/kotlinx/")
    maven(url = "https://kotlin.bintray.com/ktor")
}

val serializationRuntimeVersion: String by extra
val ktorUtilVersion: String by extra
val loggingVersion: String by extra

kotlin {

    crossCompilation {
        common {
            defaultSourceSet.dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serializationRuntimeVersion")
                implementation("io.ktor:ktor-utils-native:$ktorUtilVersion")
            }
        }
    }
    macosX64()
    mingwX64()
    linuxX64()

    jvm {
        compilations["main"].defaultSourceSet {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationRuntimeVersion")
                implementation("io.github.microutils:kotlin-logging:$loggingVersion")
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
                implementation("io.github.microutils:kotlin-logging-common:$loggingVersion")
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

publishing {
    repositories {
        maven {
            url = uri("https://oss.jfrog.org/oss-release-local")
            credentials {
                username =
                    if (project.hasProperty("bintrayUser"))
                        project.property("bintrayUser").toString()
                    else System.getenv("BINTRAY_USER")
                password =
                    if (project.hasProperty("bintrayApiKey"))
                        project.property("bintrayApiKey").toString()
                    else System.getenv("BINTRAY_API_KEY")
            }
        }
    }
}

