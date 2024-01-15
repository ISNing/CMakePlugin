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

package io.github.isning.gradle.plugins.cmake.targets

import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableVisionOSEntries
import io.github.isning.gradle.plugins.cmake.params.platform.ModifiableVisionOSParams
import io.github.isning.gradle.plugins.cmake.params.platform.ModifiableVisionOSParamsImpl
import org.gradle.api.Project
import org.gradle.internal.Factory

class VisionOSTarget(project: Project, name: String) :
    AbstractAppleTarget<ModifiableVisionOSParams<ModifiableVisionOSEntries>>(project, name) {
    override val cleanConfigParamsFactory: Factory<ModifiableVisionOSParams<ModifiableVisionOSEntries>> = Factory {
        ModifiableVisionOSParamsImpl()
    }
}
