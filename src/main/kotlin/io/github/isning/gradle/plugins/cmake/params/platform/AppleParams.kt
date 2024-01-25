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
import io.github.isning.gradle.plugins.cmake.params.entries.platform.AppleEntries

interface AppleParamsProps

interface AppleParams : PlatformParams, AppleParamsProps {
    override val entries: CMakeCacheEntries?
}

interface ModifiableAppleParams<T : AppleEntries> : ModifiablePlatformParams<T>, AppleParams {
    override var entries: CMakeCacheEntries?
}

abstract class AbstractAppleParams : AbstractPlatformParams(), AppleParams {
    abstract override val entries: CMakeCacheEntries?
}

abstract class AbstractModifiableAppleParams<T : AppleEntries> : AbstractModifiablePlatformParams<T>(),
    ModifiableAppleParams<T> {
    abstract override var entries: CMakeCacheEntries?
}
