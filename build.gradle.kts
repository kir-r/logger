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
val ktorUtilVersion: String by extra
val klockVersion: String by extra
val kniVersion: String by extra

kotlin {
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("io.ktor.utils.io.core.ExperimentalIoApi")
        }
        commonMain {
            dependencies {
                api("com.epam.drill.logger:logger-api:$drillLoggerApiVersion")
                implementation("com.soywiz.korlibs.klock:klock:$klockVersion")
            }
        }
        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
            }
        }
    }

    crossCompilation {
        common {
            defaultSourceSet {
                dependsOn(sourceSets.commonMain.get())
                dependencies {
                    implementation("io.ktor:ktor-utils:$ktorUtilVersion")
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
        main.defaultSourceSet {
            dependencies {
                implementation("com.soywiz.korlibs.klock:klock:$klockVersion")
            }
        }
    }

    jvm {
        val main by compilations
        main.defaultSourceSet {
            dependencies {
                implementation("io.ktor:ktor-utils:$ktorUtilVersion")
                implementation("com.soywiz.korlibs.klock:klock:$klockVersion")
                implementation("com.epam.drill.kni:runtime:$kniVersion")
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

val skipJvmTests: Boolean = extra["skipJvmTests"]?.toString()?.toBoolean() ?: false

tasks.withType<org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest> {
    enabled = !skipJvmTests
    val agentLibTaskPath = ":test-agent:linkAgentOnlyForTestDebugSharedNative"
    runCatching { tasks.getByPath(agentLibTaskPath) }.getOrNull()?.let { dependsOn(it) }
    doFirst {
        val libExtensions = listOf("so", "dylib", "dll")
        val outputDir = tasks.getByPath(agentLibTaskPath).outputs.files.first()
        val agent = outputDir.walkTopDown().last { it.extension in libExtensions }
        jvmArgs("-agentpath:$agent")
    }
}
