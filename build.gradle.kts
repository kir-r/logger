import java.net.*

plugins {
    kotlin("multiplatform")
    id("com.github.hierynomus.license")
    `maven-publish`
}

val scriptUrl: String by extra
val kniVersion: String by extra
val drillLoggerApiVersion: String by extra
val ktorUtilVersion: String by extra

apply(from = "$scriptUrl/git-version.gradle.kts")
apply(from = "$scriptUrl/maven-repo.gradle.kts")

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    targets {
        mingwX64()
        macosX64()
        linuxX64()
        jvm {
            compilations.getByName("main").defaultSourceSet {
                dependencies {
                    implementation("com.epam.drill.kni:runtime:$kniVersion")
                }
            }
        }
    }
    sourceSets {
        all {
            languageSettings.optIn("io.ktor.utils.io.core.ExperimentalIoApi")
        }
        val commonMain by getting {
            dependencies {
                api("com.epam.drill.logger:logger-api:$drillLoggerApiVersion")
                implementation("io.ktor:ktor-utils:$ktorUtilVersion")
            }
        }

        val commonNative by creating {
            dependsOn(commonMain)
        }
        val linuxX64Main by getting {
            dependsOn(commonNative)
        }
        val mingwX64Main by getting {
            dependsOn(commonNative)
        }
        val macosX64Main by getting {
            dependsOn(commonNative)
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-annotations-common"))
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

val licenseFormatSettings by tasks.registering(com.hierynomus.gradle.license.tasks.LicenseFormat::class) {
    source = fileTree(project.projectDir).also {
        include("**/*.kt", "**/*.java", "**/*.groovy")
        exclude("**/.idea")
    }.asFileTree
    headerURI = URI("https://raw.githubusercontent.com/Drill4J/drill4j/develop/COPYRIGHT")
}

tasks["licenseFormat"].dependsOn(licenseFormatSettings)
