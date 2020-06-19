import org.jetbrains.kotlin.konan.target.*

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

val drillLoggerApiVersion: String by extra
val atomicFuVersion: String by extra
val ktorUtilVersion: String by extra
val klockVersion: String by extra

kotlin {
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("io.ktor.utils.io.core.ExperimentalIoApi")
        }
        commonMain {
            dependencies {
                api("com.epam.drill.logger:logger-api:$drillLoggerApiVersion")
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

    listOf(
        linuxX64(),
        macosX64(),
        mingwX64()
    ).forEach {
        val main by it.compilations
        val suffix = main.konanTarget.presetName.toLowerCase()
        main.defaultSourceSet {
            dependencies {
                implementation("com.soywiz.korlibs.klock:klock-$suffix:$klockVersion")
            }
        }
    }

    jvm {
        val main by compilations
        main.defaultSourceSet {
            dependencies {
                compileOnly(kotlin("stdlib"))
                compileOnly("org.jetbrains.kotlinx:atomicfu:$atomicFuVersion")
                implementation("io.ktor:ktor-utils-jvm:$ktorUtilVersion")
                implementation("com.soywiz.korlibs.klock:klock-jvm:$klockVersion")
            }
        }
        val test by compilations
        test.defaultSourceSet {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest> {
    val agentLibTaskPath = ":test-agent:linkAgentOnlyForTestDebugSharedNative"
    dependsOn(agentLibTaskPath)
    doFirst {
        val libExtensions = listOf("so", "dylib", "dll")
        val outputDir = tasks.getByPath(agentLibTaskPath).outputs.files.first()
        val agent = outputDir.walkTopDown().last { it.extension in libExtensions }
        jvmArgs("-agentpath:$agent")
    }
}
