rootProject.name = "logger"

val scriptUrl: String by extra
apply(from = "$scriptUrl/maven-repo.settings.gradle.kts")

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://drill4j.jfrog.io/artifactory/drill")
    }
    val kotlinVersion: String by extra
    val kniVersion: String by extra
    val licenseVersion: String by extra
    plugins {
        kotlin("multiplatform") version kotlinVersion
        id("com.epam.drill.gradle.plugin.kni") version kniVersion
        id("com.github.hierynomus.license") version licenseVersion
    }
}
val skipJvmTests: Boolean = extra["skipJvmTests"]?.toString()?.toBoolean() ?: false

if (!skipJvmTests)
    include(":test-agent")
