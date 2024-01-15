plugins {
    kotlin("jvm") version "1.9.21"
    id("com.gradle.plugin-publish") version "1.0.0"
    `kotlin-dsl`
}

group = "io.github.isning.gradle.plugins"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.apache.commons:commons-text:1.10.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

gradlePlugin {
    website.set("https://github.com/ISNing/GradlePlugins")
    vcsUrl.set("https://github.com/ISNing/GradlePlugins")

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