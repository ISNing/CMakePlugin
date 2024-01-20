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
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.internal.Factory
import java.io.File

interface CMakeConfiguration {
    val executable: String?
    val workingFolder: File?
    val configParams: CMakeParams?
    val buildParams: CMakeParams?
}

interface ModifiableCMakeConfiguration<C : CMakeParams?, B : CMakeParams?> : CMakeConfiguration {
    override var configParams: CMakeParams?
    override var buildParams: CMakeParams?
    val cleanConfigParamsFactory: Factory<C>
    val cleanBuildParamsFactory: Factory<B>

    val configParamsInitialOverlay: CMakeParams?
        get() = null
    val buildParamsInitialOverlay: CMakeParams?
        get() = null

    fun configParams(configure: C.() -> Unit) =
        cleanConfigParamsFactory.create()!!.apply(configure).let { new ->
            configParams = configParams?.let { it + new } ?: new
        }

    fun buildParams(configure: B.() -> Unit) =
        cleanBuildParamsFactory.create()!!.apply(configure).let { new ->
            buildParams = buildParams?.let { it + new } ?: new
        }
}

abstract class AbstractCMakeConfiguration<C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams>(val project: Project) :
    ModifiableCMakeConfiguration<C, B> {
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
    override var configParams: CMakeParams?
        get() = configParamsInitialOverlay.orEmpty + configParamsProp.orNull.orEmpty
        set(value) = configParamsProp.set(value)
    override var buildParams: CMakeParams?
        get() = buildParamsInitialOverlay.orEmpty + buildParamsProp.orNull.orEmpty
        set(value) = buildParamsProp.set(value)
}

interface CMakeExecutionConfiguration {
    val executable: String?
    val workingFolder: File?
    val parameters: CMakeParams?
}