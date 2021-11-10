import java.net.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
    kotlin("multiplatform")
    id("com.epam.drill.cross-compilation")
    id("com.github.hierynomus.license")
    id("com.epam.drill.gradle.plugin.kni")
    `maven-publish`
}

val scriptUrl: String by extra


apply(from = "$scriptUrl/git-version.gradle.kts")
apply(from = "$scriptUrl/maven-repo.gradle.kts")

repositories {
    mavenLocal()
    mavenCentral()
}

val drillLoggerApiVersion: String by extra
val ktorUtilVersion: String by extra
val kniVersion: String by extra
val kxDatetime: String by extra
val drillJvmApiLibVersion: String by extra

val nativeTargets = mutableSetOf<KotlinNativeTarget>()

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
            languageSettings.useExperimentalAnnotation("io.ktor.utils.io.core.ExperimentalIoApi")
        }
        commonMain {
            dependencies {
                api("com.epam.drill.logger:logger-api:$drillLoggerApiVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kxDatetime")
                implementation("io.ktor:ktor-utils:$ktorUtilVersion")
                implementation("com.epam.drill:jvmapi:$drillJvmApiLibVersion")
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

    jvm {
        val main by compilations
        main.defaultSourceSet {
            dependencies {
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
    kni {
        jvmTargets = sequenceOf(jvm())
        additionalJavaClasses = sequenceOf()
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

tasks.named<ProcessResources>("jvmProcessResources") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

val licenseFormatSettings by tasks.registering(com.hierynomus.gradle.license.tasks.LicenseFormat::class) {
    source = fileTree(project.projectDir).also {
        include("**/*.kt", "**/*.java", "**/*.groovy")
        exclude("**/.idea")
    }.asFileTree
    headerURI = URI("https://raw.githubusercontent.com/Drill4J/drill4j/develop/COPYRIGHT")
}

tasks["licenseFormat"].dependsOn(licenseFormatSettings)
