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
import io.github.isning.gradle.plugins.cmake.params.CMakeParams
import io.github.isning.gradle.plugins.cmake.params.emptyCMakeParams
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableTvOSEntries
import io.github.isning.gradle.plugins.cmake.params.platform.ModifiableTvOSParams
import io.github.isning.gradle.plugins.cmake.params.platform.ModifiableTvOSParamsImpl
import io.github.isning.gradle.plugins.cmake.params.platform.TvOSParamsImpl
import io.github.isning.gradle.plugins.cmake.params.plus
import org.gradle.api.Project
import org.gradle.internal.Factory

open class TvOSTarget(
    project: Project,
    name: String,
    inheritedParents: List<CMakeConfiguration>,
    inheritedNames: List<String>,
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
) :
    AbstractAppleTarget<ModifiableTvOSParams<ModifiableTvOSEntries>>(
        project, name, inheritedParents, inheritedNames,
        { emptyCMakeParams() + buildParamsInitialOverlayProvider() },
        { TvOSParamsImpl() + configParamsInitialOverlayProvider() }
    ) {
    override val cleanConfigParamsFactory: Factory<ModifiableTvOSParams<ModifiableTvOSEntries>> = Factory {
        ModifiableTvOSParamsImpl()
    }
}

class TvOSSimulatorTarget(
    project: Project,
    name: String,
    inheritedParents: List<CMakeConfiguration>,
    inheritedNames: List<String>,
) : TvOSTarget(project, name, inheritedParents, inheritedNames, { emptyCMakeParams() }, {
    ModifiableTvOSParamsImpl().apply {
        entries { osxSysroot = "appletvsimulator" }
    }
})
