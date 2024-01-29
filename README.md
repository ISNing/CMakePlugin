# CMakePlugin

This plugin allows to configure and build using CMake.

This plugin should work as documented, but is in an early development phase.
If you have feature requests or find bugs, please create an issue.

## Prerequisites

* `CMake` installed on the system. Available [here](https://www.cmake.org "CMake Homepage").

## To apply the plugin:

```kotlin
plugins {
  id("io.github.isning.gradle.plugins.cmake") version "0.1.1"
}
```

and configure within `cmake { ... }` block:

```kotlin
cmake {
    projects {
        val hello by creating {
            val buildPath = project.layout.buildDirectory.dir("cmake").get().asFile.absolutePath +
                    "/{projectName}/{targetName}"
            configParams {
                sourceDir = file("src/cpp").absolutePath
                buildDir = buildPath
            }
            buildParams {
                buildDir = buildPath
            }

            listOf(
              androidX64.ndk(),
              androidX86.ndk(),
              androidArm32.ndk(),
              androidArm64.ndk(),
            ).forEach {
                it.configParams {
                    entries {
                        ndk = "C:/Users/ISNing/AppData/Local/Android/Sdk/ndk/25.2.9519653"
                    }
                }
            }
          mingwX64.zig()
          msvcX64.zig()
            host()
        }
    }
}
```

**Note:** Here's `project` refers to CMakeProject as well as a library or something, not gradle project.

You can define properties in different scopes such as `cmake` block, `CMakeProject` block or`CMakeTarget` block,
and finally when it's going to build, the properties will be merged together.

The preset functions like `androidX64.ndk()` will return a target with some default properties for crosscompiling using
Android NDK toolchains, and there's also different toolchain specified presets for different target.

And for different presets, there are still some properties needs to be manually filled
For example, `androidX64()` will require `ndk` and/or other related properties.

And for all the properties, there are several stub strings that will be replaced by the plugin:

| Stub String           | To                         |
|-----------------------|----------------------------|
| `{gradleProjectName}` | The name of gradle project |
| `{projectName}`       | The name of the project    |
| `{targetName}`        | The name of the target     |

## Auto-created tasks

* *cmakeConfigure`${projectName}${targetName}`*: Calls CMake to generate build scripts for the specified target.

* *cmakeBuild`${projectName}${targetName}`*: Calls CMake to build the specified target.

* *cmakeClean*: Cleans the workingFolder.

* *cmakeListGenerators*: Trys to list the generators available on the current platform by parsing `cmake --help`'s
  output.

* *cmakeVersion*: CMake's version info. `cmake --version`'s output.

## Examples

clean, configure and build:

```bash
./gradlew cmakeClean cmakeConfigureHelloAndroidArm64 cmakeBuildHelloAndroidArm64
```

and just call

```bash
./gradlew clean assemble
```

If you want to get the output of cmake, add -i to your gradle call, for example:

```bash
./gradlew cmakeConfigure -i
```

## Custom tasks

You can create custom tasks the following way:

```kotlin
val configureFoo = task("configureFoo", CMakeConfigureTask::class) {
    parameters = ModifiableCMakeGeneralParamsImpl {
        generator = "Ninja"
        buildDir = "build/cmake"
        sourceDir = "src/cpp"
        // ...
    } + ModifiableBasicCMakeEntriesImpl().apply {
        /// ...
    }.asCMakeParams
}

val buildFoo = task("configureFoo", type = CMakeBuildTask::class) {
    parameters = ModifiableCMakeBuildParamsImpl {
        buildDir = "build/cmake"
    }
}

buildFoo.dependsOn(configureFoo) // Optional: Just make sure its configured when you run the build task
```

# Learn more about configuring Cross-Compiling

Go to CMake official
docs: [Cross-Compiling](https://cmake.org/cmake/help/latest/manual/cmake-toolchains.7.html#cross-compiling)

Go to LLVM official docs: [Cross-compilation using Clang](https://clang.llvm.org/docs/CrossCompilation.html)

## License

All these plugins are licensed under the Apache License, Version 2.0 with no warranty (expressed or implied) for any
purpose.