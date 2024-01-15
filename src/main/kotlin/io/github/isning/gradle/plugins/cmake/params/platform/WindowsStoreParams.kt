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
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableWindowsStoreEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableWindowsStoreEntriesImpl
import io.github.isning.gradle.plugins.cmake.params.entries.platform.WindowsStoreEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.WindowsStoreEntriesImpl
import org.gradle.internal.Factory

interface WindowsStoreParamsProps

interface WindowsStoreParams : PlatformParams, WindowsStoreParamsProps {
    override val entries: CMakeCacheEntries?
}

interface ModifiableWindowsStoreParams<T : WindowsStoreEntries> : ModifiablePlatformParams<T>, WindowsStoreParams {
    override var entries: CMakeCacheEntries?
}

open class WindowsStoreParamsImpl : AbstractPlatformParams(), WindowsStoreParams {
    override val entries: CMakeCacheEntries? = WindowsStoreEntriesImpl()
}

class ModifiableWindowsStoreParamsImpl : AbstractModifiablePlatformParams<ModifiableWindowsStoreEntries>(),
    ModifiableWindowsStoreParams<ModifiableWindowsStoreEntries> {
    override var entries: CMakeCacheEntries? by recorder.observed(ModifiableWindowsStoreEntriesImpl())
    override val cleanEntriesFactory: Factory<ModifiableWindowsStoreEntries> =
        Factory { ModifiableWindowsStoreEntriesImpl() }
}
