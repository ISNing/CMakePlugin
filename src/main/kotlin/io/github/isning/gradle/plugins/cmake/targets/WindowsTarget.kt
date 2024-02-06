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

import io.github.isning.gradle.plugins.cmake.AbstractCMakeTarget
import io.github.isning.gradle.plugins.cmake.CMakeConfiguration
import io.github.isning.gradle.plugins.cmake.CMakeTargetImpl
import io.github.isning.gradle.plugins.cmake.params.CMakeParams
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeBuildParams
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeBuildParamsImpl
import io.github.isning.gradle.plugins.cmake.params.emptyCMakeParams
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableWindowsEntries
import io.github.isning.gradle.plugins.cmake.params.platform.ModifiableWindowsParams
import io.github.isning.gradle.plugins.cmake.params.platform.ModifiableWindowsParamsImpl
import io.github.isning.gradle.plugins.cmake.params.platform.WindowsParamsImpl
import org.gradle.api.Project
import org.gradle.internal.Factory

interface WindowsTarget

class WindowsTargetImpl(
    project: Project,
    name: String,
    inheritedParents: List<CMakeConfiguration>,
    inheritedNames: List<String>,
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
) :
    CMakeTargetImpl<ModifiableWindowsParams<ModifiableWindowsEntries>, ModifiableCMakeBuildParams>(
        project, name, inheritedParents, inheritedNames,
        { ModifiableWindowsParamsImpl() },
        { ModifiableCMakeBuildParamsImpl() },
        { emptyCMakeParams() },
        { WindowsParamsImpl() },
    ), WindowsTarget


abstract class AbstractWindowsTarget<T : ModifiableWindowsParams<*>>(
    project: Project,
    name: String,
    inheritedParents: List<CMakeConfiguration>,
    inheritedNames: List<String>,
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
) :
    AbstractCMakeTarget<T, ModifiableCMakeBuildParams>(
        project, name, inheritedParents, inheritedNames,
        buildParamsInitialOverlayProvider, configParamsInitialOverlayProvider
    ), WindowsTarget {
    override val cleanBuildParamsFactory: Factory<ModifiableCMakeBuildParams> = Factory {
        ModifiableCMakeBuildParamsImpl()
    }
}
