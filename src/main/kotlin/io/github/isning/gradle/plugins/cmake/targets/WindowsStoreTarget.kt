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

package io.github.isning.gradle.plugins.cmake.targets

import io.github.isning.gradle.plugins.cmake.CMakeConfiguration
import io.github.isning.gradle.plugins.cmake.CMakeTargetImpl
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeBuildParams
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeBuildParamsImpl
import io.github.isning.gradle.plugins.cmake.params.emptyCMakeParams
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableWindowsStoreEntries
import io.github.isning.gradle.plugins.cmake.params.platform.ModifiableWindowsStoreParams
import io.github.isning.gradle.plugins.cmake.params.platform.ModifiableWindowsStoreParamsImpl
import io.github.isning.gradle.plugins.cmake.params.platform.WindowsStoreParamsImpl
import org.gradle.api.Project

class WindowsStoreTarget(
    project: Project,
    name: String,
    inheritedParents: List<CMakeConfiguration>,
    inheritedNames: List<String>
) :
    CMakeTargetImpl<ModifiableWindowsStoreParams<ModifiableWindowsStoreEntries>,
            ModifiableCMakeBuildParams>(
        project, name, inheritedParents, inheritedNames,
        { ModifiableWindowsStoreParamsImpl() },
        { ModifiableCMakeBuildParamsImpl() },
        { emptyCMakeParams() },
        { WindowsStoreParamsImpl() },
    )
