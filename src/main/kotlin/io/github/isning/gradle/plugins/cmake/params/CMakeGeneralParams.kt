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
import io.github.isning.gradle.plugins.cmake.utils.warpQuotes

interface CMakeGeneralParamsParamsToBeRemovedRecorded : CMakeGeneralParams,
    CMakeParamsValueFilteredParamsToBeRemovedRecorded

interface CMakeGeneralParams : CMakeParams {
    val sourceDir: String?
    val buildDir: String?
    val initialCache: String?
    val generator: String?
    val toolset: String?
    val platform: String?
    val toolchainFile: String?
    val installPrefix: String?
    val developerWarnings: Boolean?
    val noDeveloperWarnings: Boolean?
    val developerWarningsAsErrors: Boolean?
    val noDeveloperWarningsAsErrors: Boolean?
    val deprecatedWarnings: Boolean?
    val noDeprecatedWarnings: Boolean?
    val deprecatedWarningsAsErrors: Boolean?
    val noDeprecatedWarningsAsErrors: Boolean?
    val preset: String?
    val listPresets: String?
    val listPresetsType: String?
    val cmakeCommandMode: Boolean?
    val listNonAdvancedCachedVariables: Boolean?
    val listNonAdvancedCachedVariablesAdvanced: Boolean?
    val listNonAdvancedCachedVariablesHelp: Boolean?
    val fresh: Boolean?
    val build: String?
    val install: String?
    val open: String?
    val viewModeOnly: Boolean?
    val processScriptMode: String?
    val legacyPkgConfigMode: Boolean?
    val graphviz: String?
    val systemInformation: String?
    val systemInformationFile: String?
    val logLevel: String?
    val logContext: Boolean?
    val debugTryCompile: Boolean?
    val debugOutput: Boolean?
    val debugFind: Boolean?
    val debugFindPkg: String?
    val debugFindVar: String?
    val trace: Boolean?
    val traceExpand: Boolean?
    val traceFormat: String?
    val traceSource: String?
    val traceRedirect: String?
    val warnUninitialized: Boolean?
    val noWarnUnusedCli: Boolean?
    val checkSystemVars: Boolean?
    val compileNoWarningAsError: Boolean?
    val profilingFormat: String?
    val profilingOutput: String?
}

interface ModifiableCMakeGeneralParams : CMakeGeneralParamsParamsToBeRemovedRecorded {
    override var sourceDir: String?
    override var buildDir: String?
    override var initialCache: String?
    override var generator: String?
    override var toolset: String?
    override var platform: String?
    override var toolchainFile: String?
    override var installPrefix: String?
    override var developerWarnings: Boolean?
    override var noDeveloperWarnings: Boolean?
    override var developerWarningsAsErrors: Boolean?
    override var noDeveloperWarningsAsErrors: Boolean?
    override var deprecatedWarnings: Boolean?
    override var noDeprecatedWarnings: Boolean?
    override var deprecatedWarningsAsErrors: Boolean?
    override var noDeprecatedWarningsAsErrors: Boolean?
    override var preset: String?
    override var listPresets: String?
    override var listPresetsType: String?
    override var cmakeCommandMode: Boolean?
    override var listNonAdvancedCachedVariables: Boolean?
    override var listNonAdvancedCachedVariablesAdvanced: Boolean?
    override var listNonAdvancedCachedVariablesHelp: Boolean?
    override var fresh: Boolean?
    override var build: String?
    override var install: String?
    override var open: String?
    override var viewModeOnly: Boolean?
    override var processScriptMode: String?
    override var legacyPkgConfigMode: Boolean?
    override var graphviz: String?
    override var systemInformation: String?
    override var systemInformationFile: String?
    override var logLevel: String?
    override var logContext: Boolean?
    override var debugTryCompile: Boolean?
    override var debugOutput: Boolean?
    override var debugFind: Boolean?
    override var debugFindPkg: String?
    override var debugFindVar: String?
    override var trace: Boolean?
    override var traceExpand: Boolean?
    override var traceFormat: String?
    override var traceSource: String?
    override var traceRedirect: String?
    override var warnUninitialized: Boolean?
    override var noWarnUnusedCli: Boolean?
    override var checkSystemVars: Boolean?
    override var compileNoWarningAsError: Boolean?
    override var profilingFormat: String?
    override var profilingOutput: String?
}

open class CMakeGeneralParamsImpl : CMakeGeneralParams {
    override val sourceDir: String? = null
    override val buildDir: String? = null
    override val initialCache: String? = null
    override val generator: String? = null
    override val toolset: String? = null
    override val platform: String? = null
    override val toolchainFile: String? = null
    override val installPrefix: String? = null
    override val developerWarnings: Boolean? = null
    override val noDeveloperWarnings: Boolean? = null
    override val developerWarningsAsErrors: Boolean? = null
    override val noDeveloperWarningsAsErrors: Boolean? = null
    override val deprecatedWarnings: Boolean? = null
    override val noDeprecatedWarnings: Boolean? = null
    override val deprecatedWarningsAsErrors: Boolean? = null
    override val noDeprecatedWarningsAsErrors: Boolean? = null
    override val preset: String? = null
    override val listPresets: String? = null
    override val listPresetsType: String? = null
    override val cmakeCommandMode: Boolean? = null
    override val listNonAdvancedCachedVariables: Boolean? = null
    override val listNonAdvancedCachedVariablesAdvanced: Boolean? = null
    override val listNonAdvancedCachedVariablesHelp: Boolean? = null
    override val fresh: Boolean? = null
    override val build: String? = null
    override val install: String? = null
    override val open: String? = null
    override val viewModeOnly: Boolean? = null
    override val processScriptMode: String? = null
    override val legacyPkgConfigMode: Boolean? = null
    override val graphviz: String? = null
    override val systemInformation: String? = null
    override val systemInformationFile: String? = null
    override val logLevel: String? = null
    override val logContext: Boolean? = null
    override val debugTryCompile: Boolean? = null
    override val debugOutput: Boolean? = null
    override val debugFind: Boolean? = null
    override val debugFindPkg: String? = null
    override val debugFindVar: String? = null
    override val trace: Boolean? = null
    override val traceExpand: Boolean? = null
    override val traceFormat: String? = null
    override val traceSource: String? = null
    override val traceRedirect: String? = null
    override val warnUninitialized: Boolean? = null
    override val noWarnUnusedCli: Boolean? = null
    override val checkSystemVars: Boolean? = null
    override val compileNoWarningAsError: Boolean? = null
    override val profilingFormat: String? = null
    override val profilingOutput: String? = null

    override val value: List<String>
        get() = listOfNotNull(
            sourceDir?.let { "-S ${it.warpQuotes()}" },
            buildDir?.let { "-B ${it.warpQuotes()}" },
            initialCache?.let { "-C ${it.warpQuotes()}" },
            generator?.let { "-G ${it.warpQuotes()}" },
            toolset?.let { "-T ${it.warpQuotes()}" },
            platform?.let { "-A ${it.warpQuotes()}" },
            toolchainFile?.let { "--toolchain ${it.warpQuotes()}" },
            installPrefix?.let { "--install-prefix ${it.warpQuotes()}" },
            developerWarnings?.let { if (it) "-Wdev" else null },
            noDeveloperWarnings?.let { if (it) "-Wno-dev" else null },
            developerWarningsAsErrors?.let { if (it) "-Werror=dev" else null },
            noDeveloperWarningsAsErrors?.let { if (it) "-Wno-error=dev" else null },
            deprecatedWarnings?.let { if (it) "-Wdeprecated" else null },
            noDeprecatedWarnings?.let { if (it) "-Wno-deprecated" else null },
            deprecatedWarningsAsErrors?.let { if (it) "-Werror=deprecated" else null },
            noDeprecatedWarningsAsErrors?.let { if (it) "-Wno-error=deprecated" else null },
            preset?.let { "--preset ${it.warpQuotes()}" },
            listPresets?.let { "--list-presets${if (listPresetsType != null) "=$listPresetsType" else ""}" },
            cmakeCommandMode?.let { if (it) "-E" else null },
            listNonAdvancedCachedVariables?.let { if (it) "-L" else null }?.run {
                this + listNonAdvancedCachedVariablesAdvanced?.let { if (it) "A" else null }
            }?.run {
                this + listNonAdvancedCachedVariablesHelp?.let { if (it) "H" else null }
            },
            fresh?.let { if (it) "--fresh" else null },
            build?.let { "--build ${it.warpQuotes()}" },
            install?.let { "--install ${it.warpQuotes()}" },
            open?.let { "--open ${it.warpQuotes()}" },
            viewModeOnly?.let { if (it) "-N" else null },
            processScriptMode?.let { "-P ${it.warpQuotes()}" },
            legacyPkgConfigMode?.let { if (it) "--find-package" else null },
            graphviz?.let { "\"--graphviz=$it\"" },
            systemInformation?.let { "--system-information${if (systemInformationFile != null) "=$systemInformationFile" else ""}" },
            logLevel?.let { "\"--log-level=$it\"" },
            logContext?.let { if (it) "--log-context" else null },
            debugTryCompile?.let { if (it) "--debug-trycompile" else null },
            debugOutput?.let { if (it) "--debug-output" else null },
            debugFind?.let { if (it) "--debug-find" else null },
            debugFindPkg?.let { "\"--debug-find-pkg=$it\"" },
            debugFindVar?.let { "\"--debug-find-var=$it\"" },
            trace?.let { if (it) "--trace" else null },
            traceExpand?.let { if (it) "--trace-expand" else null },
            traceFormat?.let { "\"--trace-format=$it\"" },
            traceSource?.let { "\"--trace-source=$it\"" },
            traceRedirect?.let { "\"--trace-redirect=$it\"" },
            warnUninitialized?.let { if (it) "--warn-uninitialized" else null },
            noWarnUnusedCli?.let { if (it) "--no-warn-unused-cli" else null },
            checkSystemVars?.let { if (it) "--check-system-vars" else null },
            compileNoWarningAsError?.let { if (it) "--compile-no-warning-as-error" else null },
            profilingFormat?.let { "\"--profiling-format=$it\"" },
            profilingOutput?.let { "\"--profiling-output=$it\"" },
        )
}

open class ModifiableCMakeGeneralParamsImpl() : CMakeGeneralParamsImpl(), ModifiableCMakeGeneralParams,
    ParamsToBeRemovedRecorded {
    constructor(init: ModifiableCMakeGeneralParamsImpl.() -> Unit) : this() {
        init()
    }

    val recorder = ExplicitlyModifiedElementsRecorderImpl {
        regexMap[name] ?: error("Unknown property name: $name")
    }
    override val explicitlyRemovedElements: Set<String> = recorder.explicitlyRemovedElements

    internal open val regexMap: Map<String, String>
        get() = generalParamsRegexMap

    override var sourceDir: String? by recorder.observed(null)
    override var buildDir: String? by recorder.observed(null)
    override var initialCache: String? by recorder.observed(null)
    override var generator: String? by recorder.observed(null)
    override var toolset: String? by recorder.observed(null)
    override var platform: String? by recorder.observed(null)
    override var toolchainFile: String? by recorder.observed(null)
    override var installPrefix: String? by recorder.observed(null)
    override var developerWarnings: Boolean? by recorder.observed(null)
    override var noDeveloperWarnings: Boolean? by recorder.observed(null)
    override var developerWarningsAsErrors: Boolean? by recorder.observed(null)
    override var noDeveloperWarningsAsErrors: Boolean? by recorder.observed(null)
    override var deprecatedWarnings: Boolean? by recorder.observed(null)
    override var noDeprecatedWarnings: Boolean? by recorder.observed(null)
    override var deprecatedWarningsAsErrors: Boolean? by recorder.observed(null)
    override var noDeprecatedWarningsAsErrors: Boolean? by recorder.observed(null)
    override var preset: String? by recorder.observed(null)
    override var listPresets: String? by recorder.observed(null)
    override var listPresetsType: String? by recorder.observed(null)
    override var cmakeCommandMode: Boolean? by recorder.observed(null)
    override var listNonAdvancedCachedVariables: Boolean? by recorder.observed(null)
    override var listNonAdvancedCachedVariablesAdvanced: Boolean? by recorder.observed(null)
    override var listNonAdvancedCachedVariablesHelp: Boolean? by recorder.observed(null)
    override var fresh: Boolean? by recorder.observed(null)
    override var build: String? by recorder.observed(null)
    override var install: String? by recorder.observed(null)
    override var open: String? by recorder.observed(null)
    override var viewModeOnly: Boolean? by recorder.observed(null)
    override var processScriptMode: String? by recorder.observed(null)
    override var legacyPkgConfigMode: Boolean? by recorder.observed(null)
    override var graphviz: String? by recorder.observed(null)
    override var systemInformation: String? by recorder.observed(null)
    override var systemInformationFile: String? by recorder.observed(null)
    override var logLevel: String? by recorder.observed(null)
    override var logContext: Boolean? by recorder.observed(null)
    override var debugTryCompile: Boolean? by recorder.observed(null)
    override var debugOutput: Boolean? by recorder.observed(null)
    override var debugFind: Boolean? by recorder.observed(null)
    override var debugFindPkg: String? by recorder.observed(null)
    override var debugFindVar: String? by recorder.observed(null)
    override var trace: Boolean? by recorder.observed(null)
    override var traceExpand: Boolean? by recorder.observed(null)
    override var traceFormat: String? by recorder.observed(null)
    override var traceSource: String? by recorder.observed(null)
    override var traceRedirect: String? by recorder.observed(null)
    override var warnUninitialized: Boolean? by recorder.observed(null)
    override var noWarnUnusedCli: Boolean? by recorder.observed(null)
    override var checkSystemVars: Boolean? by recorder.observed(null)
    override var compileNoWarningAsError: Boolean? by recorder.observed(null)
    override var profilingFormat: String? by recorder.observed(null)
    override var profilingOutput: String? by recorder.observed(null)
}

internal val generalParamsRegexMap: Map<String, String> = mapOf(
    "sourceDir" to """-S\s+([^\s]+)""",
    "buildDir" to """-B\s+([^\s]+)""",
    "initialCache" to """-C\s+([^\s]+)""",
    "generator" to """-G\s+([^\s]+)""",
    "toolset" to """-T\s+([^\s]+)""",
    "platform" to """-A\s+([^\s]+)""",
    "toolchainFile" to """--toolchain\s+([^\s]+)""",
    "installPrefix" to """--install-prefix\s+([^\s]+)""",
    "developerWarnings" to """-Wdev""",
    "noDeveloperWarnings" to """-Wno-dev""",
    "developerWarningsAsErrors" to """-Werror=dev""",
    "noDeveloperWarningsAsErrors" to """-Wno-error=dev""",
    "deprecatedWarnings" to """-Wdeprecated""",
    "noDeprecatedWarnings" to """-Wno-deprecated""",
    "deprecatedWarningsAsErrors" to """-Werror=deprecated""",
    "noDeprecatedWarningsAsErrors" to """-Wno-error=deprecated""",
    "preset" to """--preset\s+([^\s]+)""",
    "listPresets" to """--list-presets(?:=([^\s]+))?""",
    "cmakeCommandMode" to """-E""",
    "listNonAdvancedCachedVariables" to """-L""",
    "listNonAdvancedCachedVariablesAdvanced" to """-LA""",
    "listNonAdvancedCachedVariablesHelp" to """-LH""",
    "fresh" to """--fresh""",
    "build" to """--build\s+([^\s]+)""",
    "install" to """--install\s+([^\s]+)""",
    "open" to """--open\s+([^\s]+)""",
    "viewModeOnly" to """-N""",
    "processScriptMode" to """-P\s+([^\s]+)""",
    "legacyPkgConfigMode" to """--find-package""",
    "graphviz" to """--graphviz=([^\s]+)""",
    "systemInformation" to """--system-information(?:=([^\s]+))?""",
    "logLevel" to """--log-level=([^\s]+)""",
    "logContext" to """--log-context""",
    "debugTryCompile" to """--debug-trycompile""",
    "debugOutput" to """--debug-output""",
    "debugFind" to """--debug-find""",
    "debugFindPkg" to """--debug-find-pkg=([^\s]+)""",
    "debugFindVar" to """--debug-find-var=([^\s]+)""",
    "trace" to """--trace""",
    "traceExpand" to """--trace-expand""",
    "traceFormat" to """--trace-format=([^\s]+)""",
    "traceSource" to """--trace-source=([^\s]+)""",
    "traceRedirect" to """--trace-redirect=([^\s]+)""",
    "warnUninitialized" to """--warn-uninitialized""",
    "noWarnUnusedCli" to """--no-warn-unused-cli""",
    "checkSystemVars" to """--check-system-vars""",
    "compileNoWarningAsError" to """--compile-no-warning-as-error""",
    "profilingFormat" to """--profiling-format=([^\s]+)""",
    "profilingOutput" to """--profiling-output=([^\s]+)""",
)