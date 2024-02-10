/*
 * Copyright 2019 Marco Freudenberger
 * Copyright 2023 Welby Seely
 * Copyright 2024 ISNing
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.isning.gradle.plugins.cmake

import io.github.isning.gradle.plugins.cmake.params.*
import io.github.isning.gradle.plugins.cmake.params.entries.CustomCMakeCacheEntries
import io.github.isning.gradle.plugins.cmake.params.entries.asCMakeParams
import io.github.isning.gradle.plugins.cmake.params.entries.lang.ModifiableCEntriesImpl
import io.github.isning.gradle.plugins.cmake.params.entries.lang.ModifiableCXXEntriesImpl
import io.github.isning.gradle.plugins.cmake.params.entries.plus
import io.github.isning.gradle.plugins.cmake.utils.upperCamelCaseName
import io.github.isning.gradle.plugins.cmake.utils.warpQuotes
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.internal.Factory
import java.io.File

interface CMakeTarget : Named, CMakeConfiguration {
    val targetName: String
    val inheritedParents: List<CMakeConfiguration>
    val inheritedNames: List<String>
    val names: List<String>
        get() = inheritedNames + name
    val configurations: List<CMakeConfiguration>
        get() = inheritedParents + this
    val finalConfiguration: CMakeConfiguration
    override fun getName(): String = targetName
}

interface ModifiableCMakeTarget<C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams> :
    CMakeTarget, ModifiableCMakeConfiguration<C, B>

operator fun <T : CMakeParams> T.invoke(configure: T.() -> Unit): Unit = configure()

interface CMakeTargetHelper : CMakeTarget {
    val project: Project
    override val finalConfiguration: CMakeConfiguration
        get() = object : CMakeConfiguration {
            override val executable: String?
                get() = configurations.lastOrNull { it.executable != null }?.executable
            override val workingFolder: File?
                get() = configurations.lastOrNull { it.workingFolder != null }?.workingFolder
            override val configParams: CMakeParams
                get() = configurations.mapNotNull { it.configParams }.fold(emptyCMakeParams()) { acc, params ->
                    acc + params
                }
                    .replaceWith("{gradleProjectName}", project.name)
                    .replaceWith("{projectName}", inheritedNames.last())
                    .replaceWith("{targetName}", targetName)
            override val buildParams: CMakeParams
                get() = configurations.mapNotNull { it.buildParams }.fold(emptyCMakeParams()) { acc, params ->
                    acc + params
                }
                    .replaceWith("{gradleProjectName}", project.name)
                    .replaceWith("{projectName}", inheritedNames.last())
                    .replaceWith("{targetName}", targetName)
        }


}

open class CMakeTargetDelegatedWithOverlay<T : CMakeTarget>(
    override val delegate: T,
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
) : CMakeTarget, CMakeConfigurationDelegatedWithOverlay<T>(
    delegate,
    buildParamsInitialOverlayProvider,
    configParamsInitialOverlayProvider,
) {
    override val targetName: String
        get() = delegate.targetName
    override val inheritedParents: List<CMakeConfiguration>
        get() = delegate.inheritedParents
    override val inheritedNames: List<String>
        get() = delegate.inheritedNames
    override val finalConfiguration: CMakeConfiguration
        get() = delegate.finalConfiguration

    override fun getName(): String = delegate.name
}

fun <T : CMakeTarget> T.delegateWith(
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
) = CMakeTargetDelegatedWithOverlay(
    this,
    buildParamsInitialOverlayProvider,
    configParamsInitialOverlayProvider,
)

open class ModifiableCMakeTargetDelegatedWithOverlay<T : ModifiableCMakeTarget<C, B>, C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams>(
    override val delegate: T,
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
) : ModifiableCMakeTarget<C, B>, ModifiableCMakeConfigurationDelegatedWithOverlay<T, C, B>(
    delegate,
    buildParamsInitialOverlayProvider,
    configParamsInitialOverlayProvider,
) {
    override val targetName: String
        get() = delegate.targetName
    override val inheritedParents: List<CMakeConfiguration>
        get() = delegate.inheritedParents
    override val inheritedNames: List<String>
        get() = delegate.inheritedNames
    override val finalConfiguration: CMakeConfiguration
        get() = delegate.finalConfiguration

    override fun getName(): String = delegate.name
}

fun <T : ModifiableCMakeTarget<C, B>, C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams> T.delegateWith(
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
): ModifiableCMakeTarget<C, B> = ModifiableCMakeTargetDelegatedWithOverlay(
    this,
    buildParamsInitialOverlayProvider,
    configParamsInitialOverlayProvider,
)

abstract class AbstractCMakeTarget<C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams>(
    final override val project: Project,
    override val targetName: String,
    override val inheritedParents: List<CMakeConfiguration>,
    override val inheritedNames: List<String>,
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
) : CMakeConfigurationWithOverlay<C, B>(project, buildParamsInitialOverlayProvider, configParamsInitialOverlayProvider),
    ModifiableCMakeTarget<C, B>, CMakeTargetHelper {
    abstract override val cleanConfigParamsFactory: Factory<C>
    abstract override val cleanBuildParamsFactory: Factory<B>
    private val taskNameSuffix
        get() = upperCamelCaseName(*names.toTypedArray())

    val configureTask: CMakeConfigureTask = project.tasks.register(
        TASK_NAME_CMAKE_CONFIGURE + taskNameSuffix, CMakeConfigureTask::class.java
    ) {
        configureFromProvider {
            object : CMakeExecutionConfiguration {
                override val executable: String?
                    get() = finalConfiguration.executable
                override val workingFolder: File?
                    get() = finalConfiguration.workingFolder
                override val parameters: CMakeParams?
                    get() = finalConfiguration.configParams
            }
        }
    }.get()
    val buildTask: CMakeBuildTask = project.tasks.register(
        TASK_NAME_CMAKE_BUILD + taskNameSuffix, CMakeBuildTask::class.java
    ) {
        configureFromProvider {
            object : CMakeExecutionConfiguration {
                override val executable: String?
                    get() = finalConfiguration.executable
                override val workingFolder: File?
                    get() = finalConfiguration.workingFolder
                override val parameters: CMakeParams?
                    get() = finalConfiguration.buildParams

            }
        }
    }.get()

    init {
        buildTask.dependsOn(configureTask)
    }
}

open class CMakeTargetImpl<C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams>(
    project: Project, name: String,
    inheritedParents: List<CMakeConfiguration>, inheritedNames: List<String>,
    override val cleanConfigParamsFactory: Factory<C>,
    override val cleanBuildParamsFactory: Factory<B>,
    buildParamsInitialOverlayProvider: () -> CMakeParams?,
    configParamsInitialOverlayProvider: () -> CMakeParams?,
) : AbstractCMakeTarget<C, B>(
    project,
    name,
    inheritedParents, inheritedNames,
    buildParamsInitialOverlayProvider,
    configParamsInitialOverlayProvider,
) {
    constructor(
        project: Project, name: String,
        inheritedParents: List<CMakeConfiguration>,
        inheritedNames: List<String>,
        cleanConfigParamsFactory: Factory<C>,
        cleanBuildParamsFactory: Factory<B>,
        buildParamsInitialOverlay: CMakeParams? = null,
        configParamsInitialOverlay: CMakeParams? = null,
    ) : this(
        project,
        name,
        inheritedParents, inheritedNames,
        cleanConfigParamsFactory,
        cleanBuildParamsFactory,
        { buildParamsInitialOverlay },
        { configParamsInitialOverlay },
    )
}

fun ModifiableCMakeTarget<*, *>.autoProperties(project: Project) {
    autoSysRoot(project)
    autoGccInstallDir(project)
    autoGccToolchain(project)
    autoExtraCompilerFlags(project)
}

fun ModifiableCMakeTarget<*, *>.autoSysRoot(project: Project) {
    if (project.properties.containsKey("$targetName.sysRoot"))
        setSysRoot(project.properties["$targetName.sysRoot"] as String)
}

fun ModifiableCMakeTarget<*, *>.autoGccInstallDir(project: Project) {
    if (project.properties.containsKey("$targetName.gccInstallDir"))
        setGccInstallDir(project.properties["$targetName.gccInstallDir"] as String)
}

fun ModifiableCMakeTarget<*, *>.autoGccToolchain(project: Project) {
    if (project.properties.containsKey("$targetName.gccToolchain"))
        setGccToolchain(project.properties["$targetName.gccToolchain"] as String)
}

fun ModifiableCMakeTarget<*, *>.autoExtraCompilerFlags(project: Project) {
    if (project.properties.containsKey("$targetName.extraCompilerFlags"))
        addCompilerFlag(project.properties["$targetName.extraCompilerFlags"] as String)
}

fun ModifiableCMakeTarget<*, *>.useZigC() {
    configParams += (ModifiableCEntriesImpl().apply {
        compiler = "zig;cc"
    } + ModifiableCXXEntriesImpl().apply {
        compiler = "zig;c++"
    }).asCMakeParams
}

fun ModifiableCMakeTarget<*, *>.useClang() {
    configParams += (ModifiableCEntriesImpl().apply {
        compiler = "clang"
    } + ModifiableCXXEntriesImpl().apply {
        compiler = "clang++"
    }).asCMakeParams
}

fun ModifiableCMakeTarget<*, *>.setCompilerTarget(target: String?) {
    configParams += (ModifiableCEntriesImpl().apply {
        compilerTarget = target
    } + ModifiableCXXEntriesImpl().apply {
        compilerTarget = target
    }).asCMakeParams
}

fun ModifiableCMakeTarget<*, *>.setSysRoot(sysRoot: String) {
    configParams += CustomCMakeCacheEntries(mapOf("CMAKE_SYSROOT" to sysRoot)).asCMakeParams
}

/**
 * Set the path to the GCC install directory.
 * Only for Clang.
 *
 * For more information, see: [Clang Document](https://clang.llvm.org/docs/ClangCommandLineReference.html#cmdoption-clang-gcc-install-dir)
 */
fun ModifiableCMakeTarget<*, *>.setGccInstallDir(path: String) = addCompilerFlag("--gcc-install-dir=$path")

/**
 * Set the path to the GCC toolchain directory.
 * Only for Clang.
 *
 * For more information, see: [Clang Document](https://clang.llvm.org/docs/ClangCommandLineReference.html#cmdoption-clang-gcc-toolchain)
 */
fun ModifiableCMakeTarget<*, *>.setGccToolchain(path: String) = addCompilerFlag("--gcc-toolchain=$path")

fun ModifiableCMakeTarget<*, *>.addCompilerFlag(flag: String) {
    configParams += (ModifiableCEntriesImpl().apply {
        flags = listOfNotNull(configParams?.entries?.flags, flag).joinToString(" ")
    } + ModifiableCXXEntriesImpl().apply {
        flags = listOfNotNull(configParams?.entries?.flags, flag).joinToString(" ")
    }).asCMakeParams
}

fun ModifiableCMakeTarget<*, *>.setLinkerFlags(flags: String) {
    configParams += CustomCMakeCacheEntries(
        mapOf(
            "CMAKE_EXE_LINKER_FLAGS_INIT" to flags,
            "CMAKE_MODULE_LINKER_FLAGS_INIT" to flags,
            "CMAKE_SHARED_LINKER_FLAGS_INIT" to flags,
        )
    ).asCMakeParams
}

fun ModifiableCMakeTarget<*, *>.forceUseLd(ld: String) {
    // Much better solution: CustomCMakeCacheEntries(mapOf("CMAKE_LINKER_TYPE" to "LLD"))
    // However, it's not released, wait for 3.29: https://cmake.org/cmake/help/git-master/variable/CMAKE_LINKER_TYPE.html

    // Note: There is no need to handle CMAKE_STATIC_LINKER_FLAGS_INIT (for static libraries) here
    // because the archiver is invoked, rather than the linker.
    // From: https://stackoverflow.com/a/68066428/18799127
    setLinkerFlags("-fuse-ld=${ld.warpQuotes()}")
}

fun ModifiableCMakeTarget<*, *>.forceUseLld() {
    forceUseLd("lld")
}
