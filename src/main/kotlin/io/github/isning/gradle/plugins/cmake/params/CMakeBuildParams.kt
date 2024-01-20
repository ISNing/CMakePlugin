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

package io.github.isning.gradle.plugins.cmake.params

import io.github.isning.gradle.plugins.cmake.utils.ExplicitlyModifiedElementsRecorderImpl
import io.github.isning.gradle.plugins.cmake.utils.findParameterValue
import io.github.isning.gradle.plugins.cmake.utils.warpQuotes

interface CMakeBuildParamsParamsToBeRemovedRecorded : CMakeBuildParams,
    CMakeParamsValueFilteredParamsToBeRemovedRecorded

interface CMakeBuildParams : CMakeParams {
    val buildDir: String?
    val preset: String?
    val listPresets: Boolean?
    val listPresetsType: String?
    val parallel: String?
    val target: String?
    val config: String?
    val cleanFirst: Boolean?
    val resolvePackageReferences: String?
    val verbose: Boolean?
    val nativeOptions: String?
}

abstract class AbstractCMakeBuildParams : CMakeBuildParams {
    abstract override val buildDir: String?
    override val preset: String? = null
    override val listPresets: Boolean? = null
    override val listPresetsType: String? = null
    override val parallel: String? = null
    override val target: String? = null
    override val config: String? = null
    override val cleanFirst: Boolean? = null
    override val resolvePackageReferences: String? = null
    override val verbose: Boolean? = null
    override val nativeOptions: String? = null

    final override val value: List<String>
        get() = listOfNotNull(
            buildDir?.let { "--build ${it.warpQuotes()}" },
            preset?.let { "--preset ${it.warpQuotes()}" },
            listPresets?.let { listPresetsType?.let { "\"--list-presets=$it\"" } ?: "--list-presets" },
            parallel?.let { "--parallel ${it.warpQuotes()}" },
            target?.let { "--target ${it.warpQuotes()}" },
            config?.let { "--config ${it.warpQuotes()}" },
            cleanFirst?.let { "--clean-first" },
            resolvePackageReferences?.let { "\"--resolve-package-references=$it\"" },
            verbose?.let { "--verbose" },
            nativeOptions?.let { "-- ${it.warpQuotes()}" }
        )
}

interface ModifiableCMakeBuildParams : CMakeBuildParamsParamsToBeRemovedRecorded {
    override var buildDir: String?
    override var preset: String?
    override var listPresets: Boolean?
    override var listPresetsType: String?
    override var parallel: String?
    override var target: String?
    override var config: String?
    override var cleanFirst: Boolean?
    override var resolvePackageReferences: String?
    override var verbose: Boolean?
    override var nativeOptions: String?
}

open class ModifiableCMakeBuildParamsImpl() : AbstractCMakeBuildParams(), ModifiableCMakeBuildParams {
    constructor(init: ModifiableCMakeBuildParamsImpl.() -> Unit) : this() {
        init()
    }

    private val recorder = ExplicitlyModifiedElementsRecorderImpl {
        buildParamsRegexMap[name] ?: error("Unknown property name: $name")
    }
    final override val explicitlyRemovedElements: Set<String> = recorder.explicitlyRemovedElements

    override var buildDir: String? by recorder.observed(null)
    override var preset: String? by recorder.observed(null)
    override var listPresets: Boolean? by recorder.observed(null)
    override var listPresetsType: String? by recorder.observed(null)
    override var parallel: String? by recorder.observed(null)
    override var target: String? by recorder.observed(null)
    override var config: String? by recorder.observed(null)
    override var cleanFirst: Boolean? by recorder.observed(null)
    override var resolvePackageReferences: String? by recorder.observed(null)
    override var verbose: Boolean? by recorder.observed(null)
    override var nativeOptions: String? by recorder.observed(null)
}

internal val buildParamsRegexMap: Map<String, String> = mapOf(
    "buildDir" to """--build(\s+)?([^\s]+)?""",
    "preset" to """--preset\s+([^\s]+)""",
    "listPresets" to """--list-presets(?:=([^\s]+))?""",
    "parallel" to """--parallel\s+([^\s]+)""",
    "target" to """--target\s+([^\s]+)""",
    "config" to """--config\s+([^\s]+)""",
    "cleanFirst" to """--clean-first""",
    "resolvePackageReferences" to """--resolve-package-references(?:=([^\s]+))?""",
    "verbose" to """--verbose""",
    "nativeOptions" to """--\s+(.+)""",
)

val CMakeParams.buildDirForBuild: String?
    get() = filteredValue.findParameterValue("--build")
