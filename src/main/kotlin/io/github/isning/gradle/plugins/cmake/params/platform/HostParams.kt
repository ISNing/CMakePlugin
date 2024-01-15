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
import io.github.isning.gradle.plugins.cmake.params.entries.platform.HostEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.HostEntriesImpl
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableHostEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableHostEntriesImpl
import org.gradle.internal.Factory

interface HostParamsProps

interface HostParams : PlatformParams, HostParamsProps {
    override val entries: CMakeCacheEntries?
}

interface ModifiableHostParams<T : HostEntries> : ModifiablePlatformParams<T>, HostParams {
    override var entries: CMakeCacheEntries?
}

open class HostParamsImpl : AbstractPlatformParams(), HostParams {
    override val entries: CMakeCacheEntries? = HostEntriesImpl()
}

class ModifiableHostParamsImpl : AbstractModifiablePlatformParams<ModifiableHostEntries>(),
    ModifiableHostParams<ModifiableHostEntries> {
    override var entries: CMakeCacheEntries? by recorder.observed(ModifiableHostEntriesImpl())
    override val cleanEntriesFactory: Factory<ModifiableHostEntries> = Factory { ModifiableHostEntriesImpl() }
}
