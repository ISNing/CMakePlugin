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
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableWatchOSEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableWatchOSEntriesImpl
import io.github.isning.gradle.plugins.cmake.params.entries.platform.WatchOSEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.WatchOSEntriesImpl
import org.gradle.internal.Factory

interface WatchOSParamsProps : AppleParamsProps

interface WatchOSParams : AppleParams, WatchOSParamsProps {
    override val entries: CMakeCacheEntries?
}

interface ModifiableWatchOSParams<T : WatchOSEntries> : ModifiableAppleParams<T>, WatchOSParams {
    override var entries: CMakeCacheEntries?
}

open class WatchOSParamsImpl : AbstractAppleParams(), WatchOSParams {
    override val entries: CMakeCacheEntries? = WatchOSEntriesImpl()
}

class ModifiableWatchOSParamsImpl : AbstractModifiableAppleParams<ModifiableWatchOSEntries>(),
    ModifiableWatchOSParams<ModifiableWatchOSEntries> {
    override var entries: CMakeCacheEntries? by recorder.observed(ModifiableWatchOSEntriesImpl())
    override val cleanEntriesFactory: Factory<ModifiableWatchOSEntries> = Factory { ModifiableWatchOSEntriesImpl() }
}
