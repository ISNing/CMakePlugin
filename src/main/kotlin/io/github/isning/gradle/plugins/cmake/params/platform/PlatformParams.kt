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

import io.github.isning.gradle.plugins.cmake.params.CMakeGeneralParams
import io.github.isning.gradle.plugins.cmake.params.CMakeGeneralParamsImpl
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeGeneralParams
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeGeneralParamsImpl
import io.github.isning.gradle.plugins.cmake.params.entries.CMakeCacheEntries
import io.github.isning.gradle.plugins.cmake.params.entries.asCMakeParams
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiablePlatformEntriesImpl
import io.github.isning.gradle.plugins.cmake.params.entries.platform.PlatformEntries
import io.github.isning.gradle.plugins.cmake.params.entries.plus
import org.gradle.internal.Factory

interface PlatformParams : CMakeGeneralParams {
    val entries: CMakeCacheEntries?
}

interface ModifiablePlatformParams<T : PlatformEntries> : PlatformParams, ModifiableCMakeGeneralParams {
    val cleanEntriesFactory: Factory<out T>
    override var entries: CMakeCacheEntries?

    fun entries(configure: T.() -> Unit) =
        cleanEntriesFactory.create()!!.apply(configure).let {
            entries = if (entries == null) it else entries!! + it
        }
}

abstract class AbstractPlatformParams : CMakeGeneralParamsImpl(), PlatformParams {
    override val entries: CMakeCacheEntries? = null

    override val value: List<String>
        get() = entries?.asCMakeParams?.value?.let { super.value + it } ?: super.value
}

abstract class AbstractModifiablePlatformParams<T : PlatformEntries> : ModifiableCMakeGeneralParamsImpl(),
    ModifiablePlatformParams<T> {
    abstract override var entries: CMakeCacheEntries?

    override val regexMap: Map<String, String>
        get() = super.regexMap + ("entries" to "-D.*")

    override val value: List<String>
        get() = entries?.asCMakeParams?.value?.let { super.value + it } ?: super.value
}

class ModifiablePlatformParamsImpl : AbstractModifiablePlatformParams<PlatformEntries>() {
    override var entries: CMakeCacheEntries? by recorder.observed(null)
    override val cleanEntriesFactory: Factory<PlatformEntries> = Factory { ModifiablePlatformEntriesImpl() }
}
