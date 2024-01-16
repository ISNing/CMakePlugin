import java.util.*

plugins {
    kotlin("jvm") version "1.9.21"
    id("com.gradle.plugin-publish") version "1.2.1"
    `kotlin-dsl`
}

group = "io.github.isning.gradle.plugins"
version = "1.0-SNAPSHOT"

val localPropsFile = project.rootProject.file("credentials.properties")
if (localPropsFile.exists()) {
    val properties = Properties().apply { load(localPropsFile.reader()) }
    if (properties.isNotEmpty()) {
        properties.onEach { (key, value) ->
            ext[key.toString()] = value
        }
    }
}

if (!ext.has("gradle.publish.key")) ext["gradle.publish.key"] =
    System.getenv("GRADLE_PUBLISH_KEY")
if (!ext.has("gradle.publish.secret")) ext["gradle.publish.secret"] =
    System.getenv("GRADLE_PUBLISH_SECRET")


repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.apache.commons:commons-text:1.10.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

gradlePlugin {
    website.set("https://github.com/ISNing/CMakePlugin")
    vcsUrl.set("https://github.com/ISNing/CMakePlugin")

    plugins {
        create("cmake") {
            id = "io.github.isning.gradle.plugins.cmake"
            implementationClass = "io.github.isning.gradle.plugins.cmake.CMakePlugin"
            displayName = "Integrate CMake into your Gradle build"
            description = "Seamlessly integrate CMake into your Gradle build"
            tags.set(listOf("c", "cxx", "cmake", "clang", "crosscompile"))
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}