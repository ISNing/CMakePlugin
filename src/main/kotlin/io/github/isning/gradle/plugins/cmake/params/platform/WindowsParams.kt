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
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableWindowsEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableWindowsEntriesImpl
import io.github.isning.gradle.plugins.cmake.params.entries.platform.WindowsEntriesImpl
import org.gradle.internal.Factory

interface WindowsParamsProps

interface WindowsParams : PlatformParams, WindowsParamsProps {
    override val entries: CMakeCacheEntries?
}

interface ModifiableWindowsParams<T : ModifiableWindowsEntries> : ModifiablePlatformParams<T>, WindowsParams {
    override var entries: CMakeCacheEntries?
}

open class WindowsParamsImpl : AbstractPlatformParams(), WindowsParams {
    override val entries: CMakeCacheEntries? = WindowsEntriesImpl()
}

abstract class AbstractWindowsParams : AbstractPlatformParams(), WindowsParams {
    override val entries: CMakeCacheEntries? = WindowsEntriesImpl()
}

class ModifiableWindowsParamsImpl : AbstractModifiablePlatformParams<ModifiableWindowsEntries>(),
    ModifiableWindowsParams<ModifiableWindowsEntries> {
    override var entries: CMakeCacheEntries? by recorder.observed(ModifiableWindowsEntriesImpl())
    override val cleanEntriesFactory: Factory<ModifiableWindowsEntries> = Factory { ModifiableWindowsEntriesImpl() }
}

abstract class AbstractModifiableWindowsParams<T : ModifiableWindowsEntries> : AbstractModifiablePlatformParams<T>(),
    ModifiableWindowsParams<T> {
    abstract override var entries: CMakeCacheEntries?
}
