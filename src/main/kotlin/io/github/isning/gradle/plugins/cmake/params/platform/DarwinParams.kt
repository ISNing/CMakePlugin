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
import io.github.isning.gradle.plugins.cmake.params.entries.platform.DarwinEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.DarwinEntriesImpl
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableDarwinEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableDarwinEntriesImpl
import org.gradle.internal.Factory

interface DarwinParamsProps : AppleParamsProps

interface DarwinParams : AppleParams, DarwinParamsProps {
    override val entries: CMakeCacheEntries?
}

interface ModifiableDarwinParams<T : DarwinEntries> : ModifiableAppleParams<T>, DarwinParams {
    override var entries: CMakeCacheEntries?
}

open class DarwinParamsImpl : AbstractAppleParams(), DarwinParams {
    override val entries: CMakeCacheEntries? = DarwinEntriesImpl()
}

class ModifiableDarwinParamsImpl : AbstractModifiableAppleParams<ModifiableDarwinEntries>(),
    ModifiableDarwinParams<ModifiableDarwinEntries> {
    override var entries: CMakeCacheEntries? by recorder.observed(ModifiableDarwinEntriesImpl())
    override val cleanEntriesFactory: Factory<ModifiableDarwinEntries> = Factory { ModifiableDarwinEntriesImpl() }
}
