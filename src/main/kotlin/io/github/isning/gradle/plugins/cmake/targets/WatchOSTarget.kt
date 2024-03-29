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
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableWatchOSEntries
import io.github.isning.gradle.plugins.cmake.params.platform.ModifiableWatchOSParams
import io.github.isning.gradle.plugins.cmake.params.platform.ModifiableWatchOSParamsImpl
import io.github.isning.gradle.plugins.cmake.params.platform.WatchOSParamsImpl
import io.github.isning.gradle.plugins.cmake.params.plus
import org.gradle.api.Project
import org.gradle.internal.Factory

open class WatchOSTarget(
    project: Project,
    name: String,
    inheritedParents: List<CMakeConfiguration>,
    inheritedNames: List<String>,
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
) :
    AbstractAppleTarget<ModifiableWatchOSParams<ModifiableWatchOSEntries>>(
        project, name, inheritedParents, inheritedNames,
        { emptyCMakeParams() + buildParamsInitialOverlayProvider() },
        { WatchOSParamsImpl() + configParamsInitialOverlayProvider() }
    ) {
    override val cleanConfigParamsFactory: Factory<ModifiableWatchOSParams<ModifiableWatchOSEntries>> = Factory {
        ModifiableWatchOSParamsImpl()
    }
}

class WatchOSSimulatorTarget(
    project: Project,
    name: String,
    inheritedParents: List<CMakeConfiguration>,
    inheritedNames: List<String>,
) : WatchOSTarget(project, name, inheritedParents, inheritedNames, { emptyCMakeParams() }, {
    ModifiableWatchOSParamsImpl().apply {
        entries { osxSysroot = "watchsimulator" }
    }
})
