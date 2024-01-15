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
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableVisionOSEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableVisionOSEntriesImpl
import io.github.isning.gradle.plugins.cmake.params.entries.platform.VisionOSEntries
import io.github.isning.gradle.plugins.cmake.params.entries.platform.VisionOSEntriesImpl
import org.gradle.internal.Factory

interface VisionOSParamsProps : AppleParamsProps

interface VisionOSParams : AppleParams, VisionOSParamsProps {
    override val entries: CMakeCacheEntries?
}

interface ModifiableVisionOSParams<T : VisionOSEntries> : ModifiableAppleParams<T>, VisionOSParams {
    override var entries: CMakeCacheEntries?
}

open class VisionOSParamsImpl : AbstractAppleParams(), VisionOSParams {
    override val entries: CMakeCacheEntries? = VisionOSEntriesImpl()
}

class ModifiableVisionOSParamsImpl : AbstractModifiableAppleParams<ModifiableVisionOSEntries>(),
    ModifiableVisionOSParams<ModifiableVisionOSEntries> {
    override var entries: CMakeCacheEntries? by recorder.observed(ModifiableVisionOSEntriesImpl())
    override val cleanEntriesFactory: Factory<ModifiableVisionOSEntries> = Factory { ModifiableVisionOSEntriesImpl() }
}
