/*
 * Copyright 2019 Marco Freudenberger
 * Copyright 2023 Welby Seely
 * Copyright 2024 ISNing
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
import io.github.isning.gradle.plugins.cmake.utils.delegateItemsTo
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.internal.Factory
import java.io.File

const val EXTENSION_NAME_CMAKE = "cmake"

open class CMakePluginExtension(val project: Project) :
    ModifiableCMakeConfiguration<ModifiableCMakeGeneralParams, ModifiableCMakeBuildParams>, TasksRegister {
    // parameters used by config and build step
    val executableProp: Property<String> = project.objects.property(String::class.java)
    val workingFolderProp: DirectoryProperty = project.objects.directoryProperty()
    val configParamsProp: Property<CMakeParams> = project.objects.property(CMakeParams::class.java)
    val buildParamsProp: Property<CMakeParams> = project.objects.property(CMakeParams::class.java)

    override val cleanConfigParamsFactory: Factory<ModifiableCMakeGeneralParams> = Factory {
        ModifiableCMakeGeneralParamsImpl()
    }
    override val cleanBuildParamsFactory: Factory<ModifiableCMakeBuildParams> = Factory {
        ModifiableCMakeBuildParamsImpl()
    }

    final override var executable: String?
        get() = executableProp.orNull
        set(value) = executableProp.set(value)

    final override var workingFolder: File?
        get() = workingFolderProp.orNull?.asFile
        set(value) = workingFolderProp.set(value)

    final override var configParams: CMakeParams?
        get() = configParamsProp.orNull
        set(value) = configParamsProp.set(value)

    final override var buildParams: CMakeParams?
        get() = buildParamsProp.orNull
        set(value) = buildParamsProp.set(value)

    val rawProjects: NamedDomainObjectContainer<CMakeProject> =
        project.container(CMakeProject::class.java)

    val projects: NamedDomainObjectContainer<CMakeProjectImpl> =
        project.container(CMakeProjectImpl::class.java) { name: String ->
            CMakeProjectImpl(project, name)
        }.also { it.delegateItemsTo(rawProjects) }

    init {
        executable = "cmake"
        workingFolder = project.layout.buildDirectory.dir("cmake").get().asFile
        configParams = ModifiableCMakeGeneralParamsImpl().let { emptyParams ->
            configParamsInitialOverlay?.plus(emptyParams)
        }
        buildParams = ModifiableCMakeBuildParamsImpl().let { emptyParams ->
            buildParamsInitialOverlay?.plus(emptyParams) ?: emptyParams
        }
    }

    override fun registerTasks(inheritedConfigurations: List<CMakeConfiguration>, inheritedNames: List<String>) {
        for (project in rawProjects) {
            if (project is TasksRegister) project.registerTasks((inheritedConfigurations + this).map {
                object : CMakeConfiguration {
                    override val executable: String? = it.executable
                    override val workingFolder: File? = it.workingFolder
                    override val configParams: CMakeParams? = it.configParams
                        ?.replaceWith("{gradleProjectName}", project.name)
                    override val buildParams: CMakeParams? = it.buildParams
                        ?.replaceWith("{gradleProjectName}", project.name)
                }
            }, emptyList())
        }
    }
}

val Project.cmakeExtension
    get() = extensions.getByName(EXTENSION_NAME_CMAKE) as CMakePluginExtension