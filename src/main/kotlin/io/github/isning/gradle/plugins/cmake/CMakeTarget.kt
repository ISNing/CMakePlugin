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

import io.github.isning.gradle.plugins.cmake.params.CMakeParams
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeBuildParams
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeGeneralParams
import io.github.isning.gradle.plugins.cmake.params.replaceWith
import io.github.isning.gradle.plugins.cmake.utils.upperCamelCaseName
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.internal.Factory
import java.io.File

interface CMakeTarget : Named, CMakeConfiguration

interface ModifiableCMakeTarget<C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams> :
    CMakeTarget, ModifiableCMakeConfiguration<C, B>

operator fun <T : CMakeParams> T.invoke(configure: T.() -> Unit): Unit = configure()

interface CMakeTargetHelper : CMakeTarget, TasksRegister, Named {
    val project: Project
    val targetName: String

    override fun getName(): String = targetName

    override fun registerTasks(inheritedConfigurations: List<CMakeConfiguration>, inheritedNames: List<String>) {
        val taskNameSuffix = upperCamelCaseName(*(inheritedNames + targetName).toTypedArray())
        val configureTask = project.tasks.register(
            TASK_NAME_CMAKE_CONFIGURE + taskNameSuffix, CMakeConfigureTask::class.java
        ) {
            configureFrom((inheritedConfigurations + this@CMakeTargetHelper).map {
                object : CMakeExecutionConfiguration {
                    override val executable: String? = it.executable
                    override val workingFolder: File? = it.workingFolder
                    override val parameters: CMakeParams? = it.configParams
                        ?.replaceWith("{targetName}", targetName)
                }
            })
        }.get()
        val buildTask = project.tasks.register(
            TASK_NAME_CMAKE_BUILD + taskNameSuffix, CMakeBuildTask::class.java
        ) {
            configureFrom((inheritedConfigurations + this@CMakeTargetHelper).map {
                object : CMakeExecutionConfiguration {
                    override val executable: String? = it.executable
                    override val workingFolder: File? = it.workingFolder
                    override val parameters: CMakeParams? = it.buildParams
                        ?.replaceWith("{targetName}", targetName)
                }
            })
        }.get()
        buildTask.dependsOn(configureTask)
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
    override val project: Project,
    override val targetName: String,
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
) : CMakeConfigurationWithOverlay<C, B>(project, buildParamsInitialOverlayProvider, configParamsInitialOverlayProvider),
    ModifiableCMakeTarget<C, B>, CMakeTargetHelper {
    abstract override val cleanConfigParamsFactory: Factory<C>
    abstract override val cleanBuildParamsFactory: Factory<B>
}

open class CMakeTargetImpl<C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams>(
    project: Project, name: String,
    override val cleanConfigParamsFactory: Factory<C>,
    override val cleanBuildParamsFactory: Factory<B>,
    buildParamsInitialOverlayProvider: () -> CMakeParams?,
    configParamsInitialOverlayProvider: () -> CMakeParams?,
) : AbstractCMakeTarget<C, B>(
    project,
    name,
    buildParamsInitialOverlayProvider,
    configParamsInitialOverlayProvider,
) {
    constructor(
        project: Project, name: String,
        cleanConfigParamsFactory: Factory<C>,
        cleanBuildParamsFactory: Factory<B>,
        buildParamsInitialOverlay: CMakeParams? = null,
        configParamsInitialOverlay: CMakeParams? = null,
    ) : this(
        project,
        name,
        cleanConfigParamsFactory,
        cleanBuildParamsFactory,
        { buildParamsInitialOverlay },
        { configParamsInitialOverlay },
    )
}