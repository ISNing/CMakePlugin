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
import io.github.isning.gradle.plugins.cmake.params.entries.platform.AndroidEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.AndroidEntriesImpl
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableAndroidEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableAndroidEntriesImpl
import org.gradle.internal.Factory

interface AndroidParamsProps

interface AndroidParams : PlatformParams, AndroidParamsProps {
    override val entries: CMakeCacheEntries?
}

interface ModifiableAndroidParams<T : AndroidEntries> : ModifiablePlatformParams<T>, AndroidParams {
    override var entries: CMakeCacheEntries?
}

open class AndroidParamsImpl : AbstractPlatformParams(), AndroidParams {
    override val entries: CMakeCacheEntries? = AndroidEntriesImpl()
    override val generator: String? = "Ninja"
}

class ModifiableAndroidParamsImpl : AbstractModifiablePlatformParams<ModifiableAndroidEntries>(),
    ModifiableAndroidParams<ModifiableAndroidEntries> {
    override var entries: CMakeCacheEntries? by recorder.observed(ModifiableAndroidEntriesImpl())
    override val cleanEntriesFactory: Factory<ModifiableAndroidEntries> = Factory { ModifiableAndroidEntriesImpl() }
    override var generator: String? = "Ninja"
}
