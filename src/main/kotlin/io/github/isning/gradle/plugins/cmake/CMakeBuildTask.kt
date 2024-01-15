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
import io.github.isning.gradle.plugins.cmake.params.filteredValue
import io.github.isning.gradle.plugins.cmake.utils.findParameterValue
import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.gradle.api.tasks.OutputDirectory

/**
 * Build a configured Build with CMake
 */
open class CMakeBuildTask : AbstractCMakeExecuteTask(), Task {
    override val parameters: Property<CMakeParams> = project.objects.property(CMakeParams::class.java)

    @get:OutputDirectory
    val buildDir: String
        get() = parameters.orNull?.filteredValue?.findParameterValue("--build")
            ?: error("CMake Build Task must have a build directory specified")

    init {
        group = TASK_GROUP_CMAKE
        description = "Build a configured Build with CMake"
    }
}
