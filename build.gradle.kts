plugins {
    kotlin("multiplatform")
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

val ktorUtilVersion: String by extra
val klockVersion: String by extra

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
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

    crossCompilation {
        common {
            defaultSourceSet {
                dependsOn(sourceSets.commonMain.get())
                dependencies {
                    implementation("io.ktor:ktor-utils-native:$ktorUtilVersion")
                }
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

    jvm {
        compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("com.soywiz.korlibs.klock:klock-jvm:$klockVersion")
            }
        }
        compilations["test"].defaultSourceSet {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest> {
    val agentLibTaskPath = ":test-agent:linkAgentOnlyForTestDebugSharedNative"
    dependsOn(agentLibTaskPath)
    doFirst{
        val libExtensions = listOf("so", "dylib", "dll")
        val outputDir = tasks.getByPath(agentLibTaskPath).outputs.files.first()
        val agent = outputDir.walkTopDown().last { it.extension in libExtensions }
        jvmArgs("-agentpath:$agent")
    }
}

