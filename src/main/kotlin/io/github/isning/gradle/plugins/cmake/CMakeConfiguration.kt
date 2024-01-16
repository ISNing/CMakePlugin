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

import io.github.isning.gradle.plugins.cmake.params.CMakeParams
import io.github.isning.gradle.plugins.cmake.params.plus
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

interface CMakeExecutionConfiguration {
    val executable: String?
    val workingFolder: File?
    val parameters: CMakeParams?
}