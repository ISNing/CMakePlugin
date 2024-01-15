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

interface LinuxEntriesProps : PlatformEntriesProps

interface LinuxEntries : LinuxEntriesProps, PlatformEntries

interface ModifiableLinuxEntries : LinuxEntries, ModifiablePlatformEntries

/**
 * This class represents the Linux specific entries for the CMake build system.
 * It extends the AbstractPlatformEntries class and sets the systemName property to "Linux".
 */
open class LinuxEntriesImpl : AbstractPlatformEntries(), LinuxEntries {
    final override val systemName: String = "Linux"
}

class ModifiableLinuxEntriesImpl : ModifiablePlatformEntriesImpl(), ModifiableLinuxEntries {
    override var systemName: String? by recorder.observed("Linux")
}
