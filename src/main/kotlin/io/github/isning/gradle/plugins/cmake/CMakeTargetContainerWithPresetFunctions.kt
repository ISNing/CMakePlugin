/*
 * Copyright 2024 ISNing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.isning.gradle.plugins.cmake

import io.github.isning.gradle.plugins.cmake.targets.*
import org.gradle.api.Action
import org.gradle.api.Project

fun <T : CMakeTarget> (Pair<String, (String) -> T?>).factory(configure: T.() -> Unit):
        CMakeTargetFactory<T> = object : CMakeTargetFactory<T> {
    override val factoryName: String = first
    override fun create(name: String): T? = second.invoke(name)?.apply(configure)
}

interface CMakeTargetContainerWithFactoriesRegisterer : CMakeTargetContainerWithPresetFunctions {
    fun registerFactories(project: Project, inheritedParents: List<CMakeConfiguration>, inheritedNames: List<String>) {
        factories.add(("host" to { name: String ->
            HostTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory { })
        factories.add(("androidX64" to { name: String ->
            AndroidTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    archAbi = "x86_64"
                }
            }
        })
        factories.add(("androidX86" to { name: String ->
            AndroidTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    archAbi = "x86"
                }
            }
        })
        factories.add(("androidArm32" to { name: String ->
            AndroidTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    archAbi = "armeabi-v7a"
                }
            }
        })
        factories.add(("androidArm64" to { name: String ->
            AndroidTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    archAbi = "arm64-v8a"
                }
            }
        })
        factories.add(("iosArm32" to { name: String ->
            IOSTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    osxArchitectures = "armv7"
                }
            }
        })
        factories.add(("iosArm64" to { name: String ->
            IOSTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    osxArchitectures = "arm64"
                }
            }
        })
        factories.add(("iosX64" to { name: String ->
            IOSTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    osxArchitectures = "x86_64"
                }
            }
        })
        factories.add(("watchosArm32" to { name: String ->
            WatchOSTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    osxArchitectures = "armv7k"
                }
            }
        })
        factories.add(("watchosArm64" to { name: String ->
            WatchOSTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    osxArchitectures = "arm64_32"
                }
            }
        })
        factories.add(("watchosX86" to { name: String ->
            WatchOSTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    osxArchitectures = "i386"
                }
            }
        })
        factories.add(("watchosX64" to { name: String ->
            WatchOSTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    osxArchitectures = "x86_64"
                }
            }
        })
        factories.add(("tvosArm64" to { name: String ->
            TvOSTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    osxArchitectures = "arm64"
                }
            }
        })
        factories.add(("tvosX64" to { name: String ->
            TvOSTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    osxArchitectures = "x86_64"
                }
            }
        })
        factories.add(("linuxX64" to { name: String ->
            LinuxTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    systemProcessor = "x86_64"
                }
            }
            useClang()
            setCompilerTarget("x86_64-linux-gnu")
            forceUseLld()
        })
        factories.add(("linuxArm64" to { name: String ->
            LinuxTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    systemProcessor = "aarch64"
                }
            }
            useClang()
            setCompilerTarget("aarch64-linux-gnu")
            forceUseLld()
        })
        factories.add(("linuxArm32Hfp" to { name: String ->
            LinuxTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    systemProcessor = "armv7hf"
                }
            }
            useClang()
            setCompilerTarget("armv7hf-linux-gnu")
            forceUseLld()
        })
        factories.add(("linuxMips32" to { name: String ->
            LinuxTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    systemProcessor = "mips"
                }
            }
            useClang()
            setCompilerTarget("mips-linux-gnu")
            forceUseLld()
        })
        factories.add(("linuxMipsel32" to { name: String ->
            LinuxTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    systemProcessor = "mipsel"
                }
            }
            useClang()
            setCompilerTarget("mipsel-linux-gnu")
            forceUseLld()
        })
        factories.add(("msvcX86" to { name: String ->
            MSVCTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    systemProcessor = "x86"
                }
            }
        })
        factories.add(("msvcX64" to { name: String ->
            MSVCTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    systemProcessor = "x86_64"
                }
            }
        })
        factories.add(("mingwX86" to { name: String ->
            MinGWTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    // When using llvm toolchains, "Windows" target will cause the generation of invalid parameter,
                    // which will cause build failure, so we override system name to "Generic" here
                    systemName = "Generic"
                    systemProcessor = "i686"
                }
            }
            useClang()
            setCompilerTarget("i686-w64-mingw32")
            forceUseLld()
        })
        factories.add(("mingwX64" to { name: String ->
            MinGWTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    // When using llvm toolchains, "Windows" target will cause the generation of invalid parameter,
                    // which will cause build failure, so we override system name to "Generic" here
                    systemName = "Generic"
                    systemProcessor = "x86_64"
                }
            }
            useClang()
            setCompilerTarget("x86_64-w64-mingw32")
            forceUseLld()
        })
        // Darwin targets ar not tested
        factories.add(("macosX64" to { name: String ->
            DarwinTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    systemProcessor = "x86_64"
                }
            }
        })
        factories.add(("macosArm64" to { name: String ->
            DarwinTarget(
                project,
                name,
                inheritedParents,
                inheritedNames
            )
        }).factory {
            configParams {
                entries {
                    systemProcessor = "arm64"
                }
            }
        })
    }
}

interface CMakeTargetContainerWithPresetFunctions : CMakeTargetContainerWithFactories {
    @Suppress("UNCHECKED_CAST")
    fun host(
        name: String = "host",
        configure: HostTarget.() -> Unit = { }
    ): HostTarget =
        configureOrCreate(
            name,
            factories.getByName("host") as CMakeTargetFactory<HostTarget>,
            configure
        )

    fun host() = host("host") { }
    fun host(name: String) = host(name) { }
    fun host(name: String, configure: Action<HostTarget>) = host(name) { configure.execute(this) }
    fun host(configure: Action<HostTarget>) = host("host") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun androidX64(
        name: String = "androidX64",
        configure: AndroidTarget.() -> Unit = { }
    ): AndroidTarget =
        configureOrCreate(
            name,
            factories.getByName("androidX64") as CMakeTargetFactory<AndroidTarget>,
            configure
        )

    fun androidX64() = androidX64("androidX64") { }
    fun androidX64(name: String) = androidX64(name) { }
    fun androidX64(name: String, configure: Action<AndroidTarget>) = androidX64(name) { configure.execute(this) }
    fun androidX64(configure: Action<AndroidTarget>) = androidX64("androidX64") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun androidX86(
        name: String = "androidX86",
        configure: AndroidTarget.() -> Unit = { }
    ): AndroidTarget =
        configureOrCreate(
            name,
            factories.getByName("androidX86") as CMakeTargetFactory<AndroidTarget>,
            configure
        )

    fun androidX86() = androidX86("androidX86") { }
    fun androidX86(name: String) = androidX86(name) { }
    fun androidX86(name: String, configure: Action<AndroidTarget>) = androidX86(name) { configure.execute(this) }
    fun androidX86(configure: Action<AndroidTarget>) = androidX86("androidX86") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun androidArm32(
        name: String = "androidArm32",
        configure: AndroidTarget.() -> Unit = { }
    ): AndroidTarget =
        configureOrCreate(
            name,
            factories.getByName("androidArm32") as CMakeTargetFactory<AndroidTarget>,
            configure
        )

    fun androidArm32() = androidArm32("androidArm32") { }
    fun androidArm32(name: String) = androidArm32(name) { }
    fun androidArm32(name: String, configure: Action<AndroidTarget>) = androidArm32(name) { configure.execute(this) }
    fun androidArm32(configure: Action<AndroidTarget>) = androidArm32("androidArm32") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun androidArm64(
        name: String = "androidArm64",
        configure: AndroidTarget.() -> Unit = { }
    ): AndroidTarget =
        configureOrCreate(
            name,
            factories.getByName("androidArm64") as CMakeTargetFactory<AndroidTarget>,
            configure
        )

    fun androidArm64() = androidArm64("androidArm64") { }
    fun androidArm64(name: String) = androidArm64(name) { }
    fun androidArm64(name: String, configure: Action<AndroidTarget>) = androidArm64(name) { configure.execute(this) }
    fun androidArm64(configure: Action<AndroidTarget>) = androidArm64("androidArm64") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun iosArm32(
        name: String = "iosArm32",
        configure: IOSTarget.() -> Unit = { }
    ): IOSTarget =
        configureOrCreate(
            name,
            factories.getByName("iosArm32") as CMakeTargetFactory<IOSTarget>,
            configure
        )

    fun iosArm32() = iosArm32("iosArm32") { }
    fun iosArm32(name: String) = iosArm32(name) { }
    fun iosArm32(name: String, configure: Action<IOSTarget>) = iosArm32(name) { configure.execute(this) }
    fun iosArm32(configure: Action<IOSTarget>) = iosArm32("iosArm32") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun iosArm64(
        name: String = "iosArm64",
        configure: IOSTarget.() -> Unit = { }
    ): IOSTarget =
        configureOrCreate(
            name,
            factories.getByName("iosArm64") as CMakeTargetFactory<IOSTarget>,
            configure
        )

    fun iosArm64() = iosArm64("iosArm64") { }
    fun iosArm64(name: String) = iosArm64(name) { }
    fun iosArm64(name: String, configure: Action<IOSTarget>) = iosArm64(name) { configure.execute(this) }
    fun iosArm64(configure: Action<IOSTarget>) = iosArm64("iosArm64") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun iosX64(
        name: String = "iosX64",
        configure: IOSTarget.() -> Unit = { }
    ): IOSTarget =
        configureOrCreate(
            name,
            factories.getByName("iosX64") as CMakeTargetFactory<IOSTarget>,
            configure
        )

    fun iosX64() = iosX64("iosX64") { }
    fun iosX64(name: String) = iosX64(name) { }
    fun iosX64(name: String, configure: Action<IOSTarget>) = iosX64(name) { configure.execute(this) }
    fun iosX64(configure: Action<IOSTarget>) = iosX64("iosX64") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun watchosArm32(
        name: String = "watchosArm32",
        configure: WatchOSTarget.() -> Unit = { }
    ): WatchOSTarget =
        configureOrCreate(
            name,
            factories.getByName("watchosArm32") as CMakeTargetFactory<WatchOSTarget>,
            configure
        )

    fun watchosArm32() = watchosArm32("watchosArm32") { }
    fun watchosArm32(name: String) = watchosArm32(name) { }
    fun watchosArm32(name: String, configure: Action<WatchOSTarget>) = watchosArm32(name) { configure.execute(this) }
    fun watchosArm32(configure: Action<WatchOSTarget>) = watchosArm32("watchosArm32") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun watchosArm64(
        name: String = "watchosArm64",
        configure: WatchOSTarget.() -> Unit = { }
    ): WatchOSTarget =
        configureOrCreate(
            name,
            factories.getByName("watchosArm64") as CMakeTargetFactory<WatchOSTarget>,
            configure
        )

    fun watchosArm64() = watchosArm64("watchosArm64") { }
    fun watchosArm64(name: String) = watchosArm64(name) { }
    fun watchosArm64(name: String, configure: Action<WatchOSTarget>) = watchosArm64(name) { configure.execute(this) }
    fun watchosArm64(configure: Action<WatchOSTarget>) = watchosArm64("watchosArm64") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun watchosX86(
        name: String = "watchosX86",
        configure: WatchOSTarget.() -> Unit = { }
    ): WatchOSTarget =
        configureOrCreate(
            name,
            factories.getByName("watchosX86") as CMakeTargetFactory<WatchOSTarget>,
            configure
        )

    fun watchosX86() = watchosX86("watchosX86") { }
    fun watchosX86(name: String) = watchosX86(name) { }
    fun watchosX86(name: String, configure: Action<WatchOSTarget>) = watchosX86(name) { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun watchosX64(
        name: String = "watchosX64",
        configure: WatchOSTarget.() -> Unit = { }
    ): WatchOSTarget =
        configureOrCreate(
            name,
            factories.getByName("watchosX64") as CMakeTargetFactory<WatchOSTarget>,
            configure
        )

    fun watchosX64() = watchosX64("watchosX64") { }
    fun watchosX64(name: String) = watchosX64(name) { }
    fun watchosX64(name: String, configure: Action<WatchOSTarget>) = watchosX64(name) { configure.execute(this) }
    fun watchosX64(configure: Action<WatchOSTarget>) = watchosX64("watchosX64") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun tvosArm64(
        name: String = "tvosArm64",
        configure: TvOSTarget.() -> Unit = { }
    ): TvOSTarget =
        configureOrCreate(
            name,
            factories.getByName("tvosArm64") as CMakeTargetFactory<TvOSTarget>,
            configure
        )

    fun tvosArm64() = tvosArm64("tvosArm64") { }
    fun tvosArm64(name: String) = tvosArm64(name) { }
    fun tvosArm64(name: String, configure: Action<TvOSTarget>) = tvosArm64(name) { configure.execute(this) }
    fun tvosArm64(configure: Action<TvOSTarget>) = tvosArm64("tvosArm64") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun tvosX64(
        name: String = "tvosX64",
        configure: TvOSTarget.() -> Unit = { }
    ): TvOSTarget =
        configureOrCreate(
            name,
            factories.getByName("tvosX64") as CMakeTargetFactory<TvOSTarget>,
            configure
        )

    fun tvosX64() = tvosX64("tvosX64") { }
    fun tvosX64(name: String) = tvosX64(name) { }
    fun tvosX64(name: String, configure: Action<TvOSTarget>) = tvosX64(name) { configure.execute(this) }
    fun tvosX64(configure: Action<TvOSTarget>) = tvosX64("tvosX64") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun linuxX64(
        name: String = "linuxX64",
        configure: LinuxTarget.() -> Unit = { }
    ): LinuxTarget =
        configureOrCreate(
            name,
            factories.getByName("linuxX64") as CMakeTargetFactory<LinuxTarget>,
            configure
        )

    fun linuxX64() = linuxX64("linuxX64") { }
    fun linuxX64(name: String) = linuxX64(name) { }
    fun linuxX64(name: String, configure: Action<LinuxTarget>) = linuxX64(name) { configure.execute(this) }
    fun linuxX64(configure: Action<LinuxTarget>) = linuxX64("linuxX64") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun linuxArm64(
        name: String = "linuxArm64",
        configure: CMakeTarget.() -> Unit = { }
    ): CMakeTarget =
        configureOrCreate(
            name,
            factories.getByName("linuxArm64") as CMakeTargetFactory<LinuxTarget>,
            configure
        )

    fun linuxArm64() = linuxArm64("linuxArm64") { }
    fun linuxArm64(name: String) = linuxArm64(name) { }
    fun linuxArm64(name: String, configure: Action<CMakeTarget>) = linuxArm64(name) { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun linuxArm32Hfp(
        name: String = "linuxArm32Hfp",
        configure: LinuxTarget.() -> Unit = { }
    ): LinuxTarget =
        configureOrCreate(
            name,
            factories.getByName("linuxArm32Hfp") as CMakeTargetFactory<LinuxTarget>,
            configure
        )

    fun linuxArm32Hfp() = linuxArm32Hfp("linuxArm32Hfp") { }

    fun linuxArm32Hfp(name: String) = linuxArm32Hfp(name) { }

    fun linuxArm32Hfp(name: String, configure: Action<LinuxTarget>) = linuxArm32Hfp(name) { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun linuxMips32(
        name: String = "linuxMips32",
        configure: LinuxTarget.() -> Unit = { }
    ): LinuxTarget =
        configureOrCreate(
            name,
            factories.getByName("linuxMips32") as CMakeTargetFactory<LinuxTarget>,
            configure
        )

    fun linuxMips32() = linuxMips32("linuxMips32") { }
    fun linuxMips32(name: String) = linuxMips32(name) { }
    fun linuxMips32(name: String, configure: Action<LinuxTarget>) = linuxMips32(name) { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun linuxMipsel32(
        name: String = "linuxMipsel32",
        configure: LinuxTarget.() -> Unit = { }
    ): LinuxTarget =
        configureOrCreate(
            name,
            factories.getByName("linuxMipsel32") as CMakeTargetFactory<LinuxTarget>,
            configure
        )


    fun linuxMipsel32() = linuxMipsel32("linuxMipsel32") { }
    fun linuxMipsel32(name: String) = linuxMipsel32(name) { }
    fun linuxMipsel32(name: String, configure: Action<LinuxTarget>) = linuxMipsel32(name) { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun msvcX86(
        name: String = "msvcX86",
        configure: MSVCTarget.() -> Unit = { }
    ): MSVCTarget =
        configureOrCreate(
            name,
            factories.getByName("msvcX86") as CMakeTargetFactory<MSVCTarget>,
            configure
        )

    fun msvcX86() = msvcX86("msvcX86") { }
    fun msvcX86(name: String) = msvcX86(name) { }
    fun msvcX86(name: String, configure: Action<MSVCTarget>) = msvcX86(name) { configure.execute(this) }
    fun msvcX86(configure: Action<MSVCTarget>) = msvcX86("msvcX86") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun msvcX64(
        name: String = "msvcX64",
        configure: MSVCTarget.() -> Unit = { }
    ): MSVCTarget =
        configureOrCreate(
            name,
            factories.getByName("msvcX64") as CMakeTargetFactory<MSVCTarget>,
            configure
        )

    fun msvcX64() = msvcX64("msvcX64") { }
    fun msvcX64(name: String) = msvcX64(name) { }
    fun msvcX64(name: String, configure: Action<MSVCTarget>) = msvcX64(name) { configure.execute(this) }
    fun msvcX64(configure: Action<MSVCTarget>) = msvcX64("msvcX64") { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun mingwX86(
        name: String = "mingwX86",
        configure: MinGWTarget.() -> Unit = { }
    ): MinGWTarget =
        configureOrCreate(
            name,
            factories.getByName("mingwX86") as CMakeTargetFactory<MinGWTarget>,
            configure
        )

    fun mingwX86() = mingwX86("mingwX86") { }

    fun mingwX86(name: String) = mingwX86(name) { }

    fun mingwX86(name: String, configure: Action<MinGWTarget>) = mingwX86(name) { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun mingwX64(
        name: String = "mingwX64",
        configure: MinGWTarget.() -> Unit = { }
    ): MinGWTarget =
        configureOrCreate(
            name,
            factories.getByName("mingwX64") as CMakeTargetFactory<MinGWTarget>,
            configure
        )

    fun mingwX64() = mingwX64("mingwX64") { }
    fun mingwX64(name: String) = mingwX64(name) { }
    fun mingwX64(name: String, configure: Action<MinGWTarget>) = mingwX64(name) { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun macosX64(
        name: String = "macosX64",
        configure: DarwinTarget.() -> Unit = { }
    ): DarwinTarget =
        configureOrCreate(
            name,
            factories.getByName("macosX64") as CMakeTargetFactory<DarwinTarget>,
            configure
        )

    fun macosX64() = macosX64("macosX64") { }
    fun macosX64(name: String) = macosX64(name) { }
    fun macosX64(name: String, configure: Action<DarwinTarget>) = macosX64(name) { configure.execute(this) }

    @Suppress("UNCHECKED_CAST")
    fun macosArm64(
        name: String = "macosArm64",
        configure: DarwinTarget.() -> Unit = { }
    ): DarwinTarget =
        configureOrCreate(
            name,
            factories.getByName("macosArm64") as CMakeTargetFactory<DarwinTarget>,
            configure
        )

    fun macosArm64() = macosArm64("macosArm64") { }
    fun macosArm64(name: String) = macosArm64(name) { }
    fun macosArm64(name: String, configure: Action<DarwinTarget>) = macosArm64(name) { configure.execute(this) }
}