rootProject.name = "logger"

val scriptUrl: String by extra
apply(from = "$scriptUrl/maven-repo.settings.gradle.kts")

pluginManagement {
    repositories {
        mavenLocal()
        maven(url = "http://oss.jfrog.org/oss-release-local")
        gradlePluginPortal()
    }
    val kotlinVersion: String by extra
    val drillGradlePluginVersion: String by extra
    val kniVersion: String by extra
    plugins {
        kotlin("multiplatform") version kotlinVersion
        id("com.epam.drill.cross-compilation") version drillGradlePluginVersion
        id("com.epam.drill.gradle.plugin.kni") version kniVersion
    }
}
val skipJvmTests: Boolean = extra["skipJvmTests"]?.toString()?.toBoolean() ?: false

if (!skipJvmTests)
    include(":test-agent")
