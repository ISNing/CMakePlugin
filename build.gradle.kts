import java.util.*

plugins {
    kotlin("jvm") version "1.9.21"
    id("com.gradle.plugin-publish") version "1.2.1"
    `kotlin-dsl`
}

group = "io.github.isning.gradle.plugins"
version = "0.1.0"

Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() && it.isFile }?.let { load(it.reader()) }
}.onEach { (key, value) ->
    if (key is String) ext[key] = value
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