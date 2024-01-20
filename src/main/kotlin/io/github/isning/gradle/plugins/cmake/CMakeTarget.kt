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
import io.github.isning.gradle.plugins.cmake.utils.upperCamelCaseName
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.internal.Factory
import java.io.File

interface CMakeTarget : Named, CMakeConfiguration

interface ModifiableCMakeTarget<C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams> :
    CMakeTarget, ModifiableCMakeConfiguration<C, B>

operator fun <T : CMakeParams> T.invoke(configure: T.() -> Unit): Unit = configure()

abstract class AbstractCMakeTarget<C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams>(
    val project: Project,
    val targetName: String,
) : TasksRegister, ModifiableCMakeTarget<C, B> {
    val executableProp: Property<String> = project.objects.property(String::class.java)
    val workingFolderProp: DirectoryProperty = project.objects.directoryProperty()

    val configParamsProp: Property<CMakeParams> = project.objects.property(CMakeParams::class.java)

    val buildParamsProp: Property<CMakeParams> = project.objects.property(CMakeParams::class.java)
    override var executable: String?
        get() = executableProp.orNull
        set(value) = executableProp.set(value)
    override var workingFolder: File?
        get() = workingFolderProp.orNull?.asFile
        set(value) = workingFolderProp.set(value)
    final override var configParams: CMakeParams?
        get() = configParamsProp.orNull
        set(value) = configParamsProp.set(value)
    final override var buildParams: CMakeParams?
        get() = buildParamsProp.orNull
        set(value) = buildParamsProp.set(value)

    init {
        configParams = ModifiableCMakeGeneralParamsImpl().let { emptyParams ->
            configParamsInitialOverlay?.plus(emptyParams)
        }
        buildParams = ModifiableCMakeBuildParamsImpl().let { emptyParams ->
            buildParamsInitialOverlay?.plus(emptyParams) ?: emptyParams
        }
    }

    override fun getName(): String = targetName

    override fun registerTasks(inheritedConfigurations: List<CMakeConfiguration>, inheritedNames: List<String>) {
        val taskNameSuffix = upperCamelCaseName(*(inheritedNames + targetName).toTypedArray())
        val configureTask = project.tasks.register(
            TASK_NAME_CMAKE_CONFIGURE + taskNameSuffix, CMakeConfigureTask::class.java
        ) {
            configureFrom((inheritedConfigurations + this@AbstractCMakeTarget).map {
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
            configureFrom((inheritedConfigurations + this@AbstractCMakeTarget).map {
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

open class CMakeTargetImpl<C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams>(
    project: Project, name: String,
    override val cleanConfigParamsFactory: Factory<C>,
    override val cleanBuildParamsFactory: Factory<B>,
    override val buildParamsInitialOverlay: CMakeParams? = null,
    override val configParamsInitialOverlay: CMakeParams? = null,
) : AbstractCMakeTarget<C, B>(
    project,
    name,
)