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

package io.github.isning.gradle.plugins.cmake.params.entries.platform

import io.github.isning.gradle.plugins.cmake.params.entries.*

interface PlatformEntriesProps : BasicCMakeEntriesProps

interface PlatformEntries : PlatformEntriesProps, BasicCMakeEntries

interface ModifiablePlatformEntries : PlatformEntries, ModifiableBasicCMakeEntries

/**
 * This abstract class represents the platform-specific entries for the CMake build system.
 * It extends the BasicCMakeEntries class.
 *
 * @property systemName The name of the system. This should be overridden in the classes that extend this class.
 */
abstract class AbstractPlatformEntries : AbstractBasicCMakeEntries(), PlatformEntries {
    abstract override val systemName: String?
}

open class ModifiablePlatformEntriesImpl : ModifiableBasicCMakeEntriesImpl(), ModifiablePlatformEntries {
    override var systemName: String? by recorder.observed(null)
}
