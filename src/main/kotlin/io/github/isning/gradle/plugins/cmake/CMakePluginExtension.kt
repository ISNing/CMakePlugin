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

import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeBuildParams
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeBuildParamsImpl
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeGeneralParams
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeGeneralParamsImpl
import io.github.isning.gradle.plugins.cmake.utils.delegateItemsTo
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.internal.Factory

const val EXTENSION_NAME_CMAKE = "cmake"

open class CMakePluginExtension(project: Project) :
    CMakeConfigurationWithOverlay<ModifiableCMakeGeneralParams, ModifiableCMakeBuildParams>(project) {

    override val cleanConfigParamsFactory: Factory<ModifiableCMakeGeneralParams> = Factory {
        ModifiableCMakeGeneralParamsImpl()
    }
    override val cleanBuildParamsFactory: Factory<ModifiableCMakeBuildParams> = Factory {
        ModifiableCMakeBuildParamsImpl()
    }

    val rawProjects: NamedDomainObjectContainer<CMakeProject> =
        project.container(CMakeProject::class.java)

    val projects: NamedDomainObjectContainer<CMakeProjectImpl> =
        project.container(CMakeProjectImpl::class.java) { name: String ->
            CMakeProjectImpl(project, name, listOf(this), emptyList())
        }.also { it.delegateItemsTo(rawProjects) }

    init {
        executable = "cmake"
        workingFolder = project.layout.buildDirectory.dir("cmake").get().asFile
    }
}

val Project.cmakeExtension
    get() = extensions.getByName(EXTENSION_NAME_CMAKE) as CMakePluginExtension