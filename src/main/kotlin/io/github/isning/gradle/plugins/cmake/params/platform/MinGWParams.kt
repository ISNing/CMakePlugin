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
import io.github.isning.gradle.plugins.cmake.params.entries.platform.MinGWEntriesImpl
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableMinGWEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableMinGWEntriesImpl
import org.gradle.internal.Factory

interface MinGWParamsProps : WindowsParamsProps

interface MinGWParams : WindowsParams, MinGWParamsProps {
    override val entries: CMakeCacheEntries?
}

interface ModifiableMinGWParams<T : ModifiableMinGWEntries> : ModifiableWindowsParams<T>, MinGWParams {
    override var entries: CMakeCacheEntries?
}

open class MinGWParamsImpl : AbstractWindowsParams(), MinGWParams {
    override val entries: CMakeCacheEntries? = MinGWEntriesImpl()
}

class ModifiableMinGWParamsImpl : AbstractModifiableWindowsParams<ModifiableMinGWEntries>(),
    ModifiableMinGWParams<ModifiableMinGWEntries> {
    override var entries: CMakeCacheEntries? by recorder.observed(ModifiableMinGWEntriesImpl())
    override val cleanEntriesFactory: Factory<ModifiableMinGWEntries> = Factory { ModifiableMinGWEntriesImpl() }
}
