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
        fun <T : ModifiableCMakeTarget<*, *>> (Pair<String, (String) -> T?>).autoFactory(configure: T.() -> Unit):
                CMakeTargetFactory<T> = factory {
            autoProperties(project)
            configure()
        }

        fun String.hostFactory(configure: HostTarget.() -> Unit): CMakeTargetFactory<HostTarget> =
            (this to { name: String ->
                HostTarget(
                    project,
                    name,
                    inheritedParents,
                    inheritedNames
                )
            }).autoFactory(configure)

        fun String.androidFactory(configure: AndroidTarget.() -> Unit): CMakeTargetFactory<AndroidTarget> =
            (this to { name: String ->
                AndroidTarget(
                    project,
                    name,
                    inheritedParents,
                    inheritedNames
                )
            }).autoFactory(configure)

        fun String.iosFactory(configure: IOSTarget.() -> Unit): CMakeTargetFactory<IOSTarget> =
            (this to { name: String ->
                IOSTarget(
                    project,
                    name,
                    inheritedParents,
                    inheritedNames
                )
            }).autoFactory(configure)

        fun String.watchosFactory(configure: WatchOSTarget.() -> Unit): CMakeTargetFactory<WatchOSTarget> =
            (this to { name: String ->
                WatchOSTarget(
                    project,
                    name,
                    inheritedParents,
                    inheritedNames
                )
            }).autoFactory(configure)

        fun String.tvosFactory(configure: TvOSTarget.() -> Unit): CMakeTargetFactory<TvOSTarget> =
            (this to { name: String ->
                TvOSTarget(
                    project,
                    name,
                    inheritedParents,
                    inheritedNames
                )
            }).autoFactory(configure)

        fun String.linuxFactory(configure: LinuxTarget.() -> Unit): CMakeTargetFactory<LinuxTarget> =
            (this to { name: String ->
                LinuxTarget(
                    project,
                    name,
                    inheritedParents,
                    inheritedNames
                )
            }).autoFactory(configure)

        fun String.msvcFactory(configure: MSVCTarget.() -> Unit): CMakeTargetFactory<MSVCTarget> =
            (this to { name: String ->
                MSVCTarget(
                    project,
                    name,
                    inheritedParents,
                    inheritedNames
                )
            }).autoFactory(configure)

        fun String.mingwFactory(configure: MinGWTarget.() -> Unit): CMakeTargetFactory<MinGWTarget> =
            (this to { name: String ->
                MinGWTarget(
                    project,
                    name,
                    inheritedParents,
                    inheritedNames
                )
            }).autoFactory(configure)

        fun String.darwinFactory(configure: DarwinTarget.() -> Unit): CMakeTargetFactory<DarwinTarget> =
            (this to { name: String ->
                DarwinTarget(
                    project,
                    name,
                    inheritedParents,
                    inheritedNames
                )
            }).autoFactory(configure)

        fun AndroidTarget.configNdk(abi: String) {
            configParams {
                entries {
                    archAbi = abi
                }
            }
        }

        fun AbstractAppleTarget<*>.configXcode(arch: String) {
            configParams {
                entries {
                    osxArchitectures = arch
                }
            }
        }

        fun <T : ModifiableCMakeTarget<*, *>> T.targetToWithClang(target: String) {
            useClang()
            setCompilerTarget(target)
            forceUseLld()
        }

        fun <T : ModifiableCMakeTarget<*, *>> T.targetToWithZig(target: String) {
            useZigC()
            setCompilerTarget(target)
        }

        // No extra parameters added
        factories.add("host".hostFactory { })


        factories.add("android".androidFactory { })

        factories.add("androidX64.ndk".androidFactory {
            configNdk("x86_64")
        })
        factories.add("androidX86.ndk".androidFactory {
            configNdk("x86")
        })
        factories.add("androidArm32.ndk".androidFactory {
            configNdk("armeabi-v7a")
        })
        factories.add("androidArm64.ndk".androidFactory {
            configNdk("arm64-v8a")
        })

        factories.add("androidX64.clang".androidFactory {
            targetToWithClang("x86_64-linux-android")
        })
        factories.add("androidX86.clang".androidFactory {
            targetToWithClang("i686-linux-android")
        })
        factories.add("androidArm32.clang".androidFactory {
            targetToWithClang("armv7a-linux-androideabi")
        })
        factories.add("androidArm64.clang".androidFactory {
            targetToWithClang("aarch64-linux-android")
        })

        factories.add("androidX64.zig".androidFactory {
            targetToWithZig("x86_64-linux-android")
        })
        factories.add("androidX86.zig".androidFactory {
            targetToWithZig("i686-linux-android")
        })
        factories.add("androidArm32.zig".androidFactory {
            targetToWithZig("armv7a-linux-androideabi")
        })
        factories.add("androidArm64.zig".androidFactory {
            targetToWithZig("aarch64-linux-android")
        })


        factories.add("ios".iosFactory { })
        factories.add("watchos".watchosFactory { })
        factories.add("tvos".tvosFactory { })

        factories.add("iosArm32.xcode".iosFactory {
            configXcode("armv7")
        })
        factories.add("iosArm64.xcode".iosFactory {
            configXcode("arm64")
        })
        factories.add("iosX64.xcode".iosFactory {
            configXcode("x86_64")
        })
        factories.add("watchosArm32.xcode".watchosFactory {
            configXcode("armv7k")
        })
        factories.add("watchosArm64.xcode".watchosFactory {
            configXcode("arm64_32")
        })
        factories.add("watchosX86.xcode".watchosFactory {
            configXcode("i386")
        })
        factories.add("watchosX64.xcode".watchosFactory {
            configXcode("x86_64")
        })
        factories.add("tvosArm64.xcode".tvosFactory {
            configXcode("arm64")
        })
        factories.add("tvosX64.xcode".tvosFactory {
            configXcode("x86_64")
        })

        factories.add("iosArm32.clang".iosFactory {
            targetToWithClang("armv7-ios-none")
        })
        factories.add("iosArm64.clang".iosFactory {
            targetToWithClang("aaarch64-ios-none")
        })
        factories.add("iosX64.clang".iosFactory {
            targetToWithClang("x86_64-ios-none")
        })
        factories.add("watchosArm32.clang".watchosFactory {
            targetToWithClang("armv7k-watchos-none")
        })
        factories.add("watchosArm64.clang".watchosFactory {
            targetToWithClang("arm64_32-watchos-none")
        })
        factories.add("watchosX86.clang".watchosFactory {
            targetToWithClang("i386-watchos-none")
        })
        factories.add("watchosX64.clang".watchosFactory {
            targetToWithClang("x86_64-watchos-none")
        })
        factories.add("tvosArm64.clang".tvosFactory {
            targetToWithClang("aarch64-tvos-none")
        })
        factories.add("tvosX64.clang".tvosFactory {
            targetToWithClang("x86_64-tvos-none")
        })



        factories.add("iosArm32.zig".iosFactory {
            targetToWithZig("armv7-ios-none")
        })
        factories.add("iosArm64.zig".iosFactory {
            targetToWithZig("aarch64-ios-none")
        })
        factories.add("iosX64.zig".iosFactory {
            targetToWithZig("x86_64-ios-none")
        })
        factories.add("watchosArm32.zig".watchosFactory {
            targetToWithZig("armv7k-watchos-none")
        })
        factories.add("watchosArm64.zig".watchosFactory {
            targetToWithZig("aarch64_32-watchos-none")
        })
        factories.add("watchosX86.zig".watchosFactory {
            targetToWithZig("i386-watchos-none")
        })
        factories.add("watchosX64.zig".watchosFactory {
            targetToWithZig("x86_64-watchos-none")
        })
        factories.add("tvosArm64.zig".tvosFactory {
            targetToWithZig("aarch64-tvos-none")
        })
        factories.add("tvosX64.zig".tvosFactory {
            targetToWithZig("x86_64-tvos-none")
        })


        factories.add("linux".linuxFactory {})

        factories.add("linuxX64.clang".linuxFactory {
            targetToWithClang("x86_64-linux-gnu")
        })
        factories.add("linuxArm64.clang".linuxFactory {
            targetToWithClang("aarch64-linux-gnu")
        })
        factories.add("linuxArm32Hfp.clang".linuxFactory {
            targetToWithClang("armv7hf-linux-gnu")
        })
        factories.add("linuxMips32.clang".linuxFactory {
            targetToWithClang("mips-linux-gnu")
        })
        factories.add("linuxMipsel32.clang".linuxFactory {
            targetToWithClang("mipsel-linux-gnu")
        })

        factories.add("linuxX64.zig".linuxFactory {
            targetToWithZig("x86_64-linux-gnu")
        })
        factories.add("linuxArm64.zig".linuxFactory {
            targetToWithZig("aarch64-linux-gnu")
        })
        factories.add("linuxArm32Hfp.zig".linuxFactory {
            targetToWithZig("armv7hf-linux-gnu")
        })
        factories.add("linuxMips32.zig".linuxFactory {
            targetToWithZig("mips-linux-gnu")
        })
        factories.add("linuxMipsel32.zig".linuxFactory {
            targetToWithZig("mipsel-linux-gnu")
        })


        factories.add("msvc".msvcFactory { })

        factories.add("msvcX86.clang".msvcFactory {
            targetToWithClang("i686-windows-msvc")
        })
        factories.add("msvcX64.clang".msvcFactory {
            targetToWithClang("x86_64-windows-msvc")
        })
        factories.add("msvcArm64.clang".msvcFactory {
            targetToWithClang("aarch64-windows-msvc")
        })

        factories.add("msvcX86.zig".linuxFactory {
            targetToWithZig("i686-windows-msvc")
        })
        factories.add("msvcX64.zig".linuxFactory {
            targetToWithZig("x86_64-windows-msvc")
        })
        factories.add("msvcArm64.zig".linuxFactory {
            targetToWithZig("aarch64-windows-msvc")
        })


        factories.add("mingw".mingwFactory { })

        factories.add("mingwX86.clang".mingwFactory {
            configParams {
                entries {
                    // When using llvm toolchains, "Windows" target will cause the generation of invalid parameter,
                    // which will cause build failure, so we override system name to "Generic" here
                    systemName = "Generic"
                }
            }
            targetToWithClang("i686-w64-mingw32")
        })
        factories.add("mingwX64.clang".mingwFactory {
            configParams {
                entries {
                    // When using llvm toolchains, "Windows" target will cause the generation of invalid parameter,
                    // which will cause build failure, so we override system name to "Generic" here
                    systemName = "Generic"
                }
            }
            targetToWithClang("x86_64-w64-mingw32")
        })
        factories.add("mingwArm64.clang".mingwFactory {
            configParams {
                entries {
                    // When using llvm toolchains, "Windows" target will cause the generation of invalid parameter,
                    // which will cause build failure, so we override system name to "Generic" here
                    systemName = "Generic"
                }
            }
            targetToWithClang("aarch64-w64-mingw32")
        })
        factories.add("mingwX86.zig".mingwFactory {
            targetToWithZig("i686-windows-gnu")
        })
        factories.add("mingwX64.zig".mingwFactory {
            targetToWithZig("x86_64-windows-gnu")
        })
        factories.add("mingwArm64.zig".mingwFactory {
            targetToWithZig("aarch64-windows-gnu")
        })


        factories.add("macos".darwinFactory { })

        factories.add("macosX64.xcode".darwinFactory {
            configXcode("x86_64")
        })
        factories.add("macosArm64.xcode".darwinFactory {
            configXcode("arm64")
        })
        factories.add("macosX64.zig".darwinFactory {
            useZigC()
            setCompilerTarget("x86_64-macos-none")
        })
        factories.add("macosArm64.zig".darwinFactory {
            useZigC()
            setCompilerTarget("aarch64-macos-none")
        })
    }
}

interface CMakeTargetContainerWithPresetFunctions : CMakeTargetContainerWithFactories {

    val host
        get() = default<HostTarget>("host")

    val android
        get() = default<AndroidTarget>("android")
    val androidX64
        get() = ndkWithClangWithZig<AndroidTarget>("androidX64")
    val androidX86
        get() = ndkWithClangWithZig<AndroidTarget>("androidX86")
    val androidArm32
        get() = ndkWithClangWithZig<AndroidTarget>("androidArm32")
    val androidArm64
        get() = ndkWithClangWithZig<AndroidTarget>("androidArm64")

    val ios
        get() = default<IOSTarget>("ios")
    val iosArm32
        get() = xcodeWithClangWithZig<IOSTarget>("iosArm32")
    val iosArm64
        get() = xcodeWithClangWithZig<IOSTarget>("iosArm64")
    val iosX64
        get() = xcodeWithClangWithZig<IOSTarget>("iosX64")

    val watchos
        get() = default<WatchOSTarget>("watchos")
    val watchosArm32
        get() = xcodeWithClangWithZig<WatchOSTarget>("watchosArm32")
    val watchosArm64
        get() = xcodeWithClangWithZig<WatchOSTarget>("watchosArm64")
    val watchosX86
        get() = xcodeWithClangWithZig<WatchOSTarget>("watchosX86")
    val watchosX64
        get() = xcodeWithClangWithZig<WatchOSTarget>("watchosX64")

    val tvos
        get() = default<TvOSTarget>("tvos")
    val tvosArm64
        get() = xcodeWithClangWithZig<TvOSTarget>("tvosArm64")
    val tvosX64
        get() = xcodeWithClangWithZig<TvOSTarget>("tvosX64")

    val linux
        get() = default<LinuxTarget>("linux")
    val linuxX64
        get() = clangWithZig<LinuxTarget>("linuxX64")
    val linuxArm64
        get() = clangWithZig<LinuxTarget>("linuxArm64")
    val linuxArm32Hfp
        get() = clangWithZig<LinuxTarget>("linuxArm32Hfp")
    val linuxMips32
        get() = clangWithZig<LinuxTarget>("linuxMips32")
    val linuxMipsel32
        get() = clangWithZig<LinuxTarget>("linuxMipsel32")

    val msvc
        get() = default<MSVCTarget>("msvc")
    val msvcX86
        get() = clangWithZig<MSVCTarget>("msvcX86")
    val msvcX64
        get() = clangWithZig<MSVCTarget>("msvcX64")
    val msvcArm64
        get() = clangWithZig<MSVCTarget>("msvcArm64")

    val mingw
        get() = default<MinGWTarget>("mingw")
    val mingwX86
        get() = clangWithZig<MinGWTarget>("mingwX86")
    val mingwX64
        get() = clangWithZig<MinGWTarget>("mingwX64")
    val mingwArm64
        get() = clangWithZig<MinGWTarget>("mingwArm64")

    val macos
        get() = default<DarwinTarget>("macos")
    val macosX64
        get() = xcodeWithClangWithZig<DarwinTarget>("macosX64")
    val macosArm64
        get() = xcodeWithClangWithZig<DarwinTarget>("macosArm64")
}


interface HasBaseFactoryName {
    val baseFactoryName: String
}

interface HasDefaultTargetName {
    val defaultTargetName: String
}

interface HasInlinedHelperFunctions<T : CMakeTarget> {
    fun inlinedConfigureOrCreate(
        targetName: String,
        factoryName: String,
        configure: T.() -> Unit,
    ): T
}

interface FunctionsBase<T : CMakeTarget> : HasBaseFactoryName, HasDefaultTargetName, HasInlinedHelperFunctions<T>

interface DefaultFunctions<T : CMakeTarget> : FunctionsBase<T> {
    operator fun invoke(
        name: String = defaultTargetName,
        configure: T.() -> Unit = { }
    ): T =
        inlinedConfigureOrCreate(
            name,
            baseFactoryName,
            configure
        )

    operator fun invoke() = invoke(defaultTargetName) { }
    operator fun invoke(name: String) = invoke(name) { }
    operator fun invoke(name: String, configure: Action<T>) = invoke(name) { configure.execute(this) }
    operator fun invoke(configure: Action<T>) = invoke(defaultTargetName) { configure.execute(this) }
}

interface NdkFunctions<T : CMakeTarget> : FunctionsBase<T> {
    fun ndk(
        name: String = defaultTargetName,
        configure: T.() -> Unit = { }
    ): T =
        inlinedConfigureOrCreate(
            name,
            "$baseFactoryName.ndk",
            configure
        )

    fun ndk() = ndk(defaultTargetName) { }
    fun ndk(name: String) = ndk(name) { }
    fun ndk(name: String, configure: Action<T>) = ndk(name) { configure.execute(this) }
    fun ndk(configure: Action<T>) = ndk(defaultTargetName) { configure.execute(this) }
}

interface XCodeFunctions<T : CMakeTarget> : FunctionsBase<T> {
    fun xcode(
        name: String = defaultTargetName,
        configure: T.() -> Unit = { }
    ): T =
        inlinedConfigureOrCreate(
            name,
            "$baseFactoryName.xcode",
            configure
        )

    fun xcode() = xcode(defaultTargetName) { }
    fun xcode(name: String) = xcode(name) { }
    fun xcode(name: String, configure: Action<T>) = xcode(name) { configure.execute(this) }
    fun xcode(configure: Action<T>) = xcode(defaultTargetName) { configure.execute(this) }
}

interface ClangFunctions<T : CMakeTarget> : FunctionsBase<T> {
    fun clang(
        name: String = defaultTargetName,
        configure: T.() -> Unit = { }
    ): T =
        inlinedConfigureOrCreate(
            name,
            "$baseFactoryName.clang",
            configure
        )

    fun clang() = clang(defaultTargetName) { }
    fun clang(name: String) = clang(name) { }
    fun clang(name: String, configure: Action<T>) = clang(name) { configure.execute(this) }
    fun clang(configure: Action<T>) = clang(defaultTargetName) { configure.execute(this) }
}

interface ZigFunctions<T : CMakeTarget> : FunctionsBase<T> {
    fun zig(
        name: String = defaultTargetName,
        configure: T.() -> Unit = { }
    ): T =
        inlinedConfigureOrCreate(
            name,
            "$baseFactoryName.zig",
            configure
        )

    fun zig() = zig(defaultTargetName) { }
    fun zig(name: String) = zig(name) { }
    fun zig(name: String, configure: Action<T>) = zig(name) { configure.execute(this) }
    fun zig(configure: Action<T>) = zig(defaultTargetName) { configure.execute(this) }
}

interface ClangWithZig<T : CMakeTarget> : ClangFunctions<T>, ZigFunctions<T>
interface XCodeWithClangWithZig<T : CMakeTarget> : XCodeFunctions<T>, ClangWithZig<T>
interface NdkWithClangWithZig<T : CMakeTarget> : NdkFunctions<T>, ClangWithZig<T>

private inline fun <reified T : CMakeTarget> CMakeTargetContainerWithFactories.default(
    baseFactoryName: String,
    defaultTargetName: String = baseFactoryName
) =
    object :
        DefaultFunctions<T> {
        override val baseFactoryName: String = baseFactoryName
        override val defaultTargetName: String = defaultTargetName

    @Suppress("UNCHECKED_CAST")
    override fun inlinedConfigureOrCreate(
        targetName: String,
        factoryName: String,
        configure: T.() -> Unit
    ): T = configureOrCreate(
        targetName,
        factories.getByName(factoryName) as CMakeTargetFactory<T>,
        configure
    )
    }

private inline fun <reified T : CMakeTarget> CMakeTargetContainerWithFactories.ndkWithClangWithZig(
    baseFactoryName: String,
    defaultTargetName: String = baseFactoryName
) =
    object :
        NdkWithClangWithZig<T> {
        override val baseFactoryName: String = baseFactoryName
        override val defaultTargetName: String = defaultTargetName

    @Suppress("UNCHECKED_CAST")
    override fun inlinedConfigureOrCreate(
        targetName: String,
        factoryName: String,
        configure: T.() -> Unit
    ): T = configureOrCreate(
        targetName,
        factories.getByName(factoryName) as CMakeTargetFactory<T>,
        configure
    )
    }

private inline fun <reified T : CMakeTarget> CMakeTargetContainerWithFactories.xcodeWithClangWithZig(
    baseFactoryName: String,
    defaultTargetName: String = baseFactoryName
) =
    object :
        XCodeWithClangWithZig<T> {
        override val baseFactoryName: String = baseFactoryName
        override val defaultTargetName: String = defaultTargetName

    @Suppress("UNCHECKED_CAST")
    override fun inlinedConfigureOrCreate(
        targetName: String,
        factoryName: String,
        configure: T.() -> Unit
    ): T = configureOrCreate(
        targetName,
        factories.getByName(factoryName) as CMakeTargetFactory<T>,
        configure
    )
    }

private inline fun <reified T : CMakeTarget> CMakeTargetContainerWithFactories.clangWithZig(
    baseFactoryName: String,
    defaultTargetName: String = baseFactoryName
) =
    object :
        ClangWithZig<T> {
        override val baseFactoryName: String = baseFactoryName
        override val defaultTargetName: String = defaultTargetName

    @Suppress("UNCHECKED_CAST")
    override fun inlinedConfigureOrCreate(
        targetName: String,
        factoryName: String,
        configure: T.() -> Unit
    ): T = configureOrCreate(
        targetName,
        factories.getByName(factoryName) as CMakeTargetFactory<T>,
        configure
    )
}
