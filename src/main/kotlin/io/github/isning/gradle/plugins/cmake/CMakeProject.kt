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

import io.github.isning.gradle.plugins.cmake.params.*
import io.github.isning.gradle.plugins.cmake.utils.delegateItemsTo
import io.github.isning.gradle.plugins.cmake.utils.runIfIs
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.internal.Factory
import java.io.File

interface CMakeProject : Named, CMakeConfiguration {
    val rawTargets: Set<CMakeTarget>
}

interface ModifiableCMakeProject : CMakeProject,
    ModifiableCMakeConfiguration<ModifiableCMakeGeneralParams, ModifiableCMakeBuildParams> {
    override val rawTargets: MutableSet<CMakeTarget>
}

class CMakeProjectImpl(
    val project: Project, val projectName: String,
    buildParamsInitialOverlayProvider: () -> CMakeParams?,
    configParamsInitialOverlayProvider: () -> CMakeParams?
) : Named,
    CMakeConfigurationWithOverlay<ModifiableCMakeGeneralParams, ModifiableCMakeBuildParams>
        (project, buildParamsInitialOverlayProvider, configParamsInitialOverlayProvider),
    TasksRegister, CMakeTargetContainerWithFactoriesRegisterer, ModifiableCMakeProject {

    constructor(
        project: Project,
        projectName: String,
        buildParamsInitialOverlay: CMakeParams? = null,
        configParamsInitialOverlay: CMakeParams? = null
    ) : this(project, projectName, { buildParamsInitialOverlay }, { configParamsInitialOverlay })

    override fun getName(): String = projectName

    override val cleanConfigParamsFactory: Factory<ModifiableCMakeGeneralParams> = Factory {
        ModifiableCMakeGeneralParamsImpl()
    }
    override val cleanBuildParamsFactory: Factory<ModifiableCMakeBuildParams> = Factory {
        ModifiableCMakeBuildParamsImpl()
    }

    override val factories: NamedDomainObjectCollection<CMakeTargetFactory<*>> =
        project.container(CMakeTargetFactory::class.java)

    override val rawTargets: NamedDomainObjectContainer<CMakeTarget> =
        project.container(CMakeTarget::class.java)

    @Suppress("UNCHECKED_CAST")
    val targets: NamedDomainObjectContainer<CMakeTargetImpl<ModifiableCMakeGeneralParamsImpl, ModifiableCMakeBuildParamsImpl>> =
        project.container(CMakeTarget::class.java) { name: String ->
            CMakeTargetImpl(project, name, {
                ModifiableCMakeGeneralParamsImpl()
            }, {
                ModifiableCMakeBuildParamsImpl()
            })
        }.also { it.delegateItemsTo(rawTargets) }
                as NamedDomainObjectContainer<CMakeTargetImpl<ModifiableCMakeGeneralParamsImpl, ModifiableCMakeBuildParamsImpl>>

    override val configParamsInitialOverlay: CMakeParams
        get() = ModifiableCMakeGeneralParamsImpl {
            sourceDir = project.layout.projectDirectory.dir("src/main/cpp").asFile.absolutePath
            buildDir = project.layout.buildDirectory.dir("cmake").get().asFile.absolutePath
        } + super.configParamsInitialOverlay.orEmpty

    init {
        registerFactories(project)
    }

    override fun registerTasks(inheritedConfigurations: List<CMakeConfiguration>, inheritedNames: List<String>) {
        rawTargets.forEach { target ->
            target.runIfIs<TasksRegister, _, _> { origin ->
                registerTasks((inheritedConfigurations + origin).map { configuration ->
                    object : CMakeConfiguration {
                        override val executable: String? = configuration.executable
                        override val workingFolder: File? = configuration.workingFolder
                        override val configParams: CMakeParams? = configuration.configParams
                            ?.replaceWith("{projectName}", projectName)
                        override val buildParams: CMakeParams? = configuration.buildParams
                            ?.replaceWith("{projectName}", projectName)
                    }

                }, inheritedNames + projectName)
            }
        }
    }
}