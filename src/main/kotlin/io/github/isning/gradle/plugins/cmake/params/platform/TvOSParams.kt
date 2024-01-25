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

package io.github.isning.gradle.plugins.cmake.params.platform

import io.github.isning.gradle.plugins.cmake.params.entries.CMakeCacheEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableTvOSEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableTvOSEntriesImpl
import io.github.isning.gradle.plugins.cmake.params.entries.platform.TvOSEntriesImpl
import org.gradle.internal.Factory

interface TvOSParamsProps : AppleParamsProps

interface TvOSParams : AppleParams, TvOSParamsProps {
    override val entries: CMakeCacheEntries?
}

interface ModifiableTvOSParams<T : ModifiableTvOSEntries> : ModifiableAppleParams<T>, TvOSParams {
    override var entries: CMakeCacheEntries?
}

open class TvOSParamsImpl : AbstractAppleParams(), TvOSParams {
    override val entries: CMakeCacheEntries? = TvOSEntriesImpl()
}

class ModifiableTvOSParamsImpl : AbstractModifiableAppleParams<ModifiableTvOSEntries>(),
    ModifiableTvOSParams<ModifiableTvOSEntries> {
    override var entries: CMakeCacheEntries? by recorder.observed(ModifiableTvOSEntriesImpl())
    override val cleanEntriesFactory: Factory<ModifiableTvOSEntries> = Factory { ModifiableTvOSEntriesImpl() }
}
