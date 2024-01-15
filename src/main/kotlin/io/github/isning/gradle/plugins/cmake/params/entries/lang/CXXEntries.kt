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

package io.github.isning.gradle.plugins.cmake.params.entries.lang

import io.github.isning.gradle.plugins.cmake.params.entries.CMakeCacheEntriesWithValueAutoGenerated

interface CXXEntriesProps {
    val compileFeatures: String?
}

interface CXXEntries : CXXEntriesProps, LanguageSpecifiedEntries

interface ModifiableCXXEntries : CXXEntries, ModifiableLanguageSpecifiedEntries {
    override var compileFeatures: String?
}

interface CXXEntriesAutoGen : CXXEntries, CMakeCacheEntriesWithValueAutoGenerated {
    override val propertyNameToKey: Map<String, String>
        get() = mapOf(
            "compileFeatures" to "CXX_COMPILE_FEATURES",
        )
}

open class CXXEntriesImpl : LanguageSpecifiedEntriesImpl("CXX"), CXXEntriesAutoGen {
    override val compileFeatures: String? = null

    override val propertyNameToKey: Map<String, String>
        get() = super<LanguageSpecifiedEntriesImpl>.propertyNameToKey + super<CXXEntriesAutoGen>.propertyNameToKey
}

class ModifiableCXXEntriesImpl : ModifiableLanguageSpecifiedEntriesImpl("CXX"), ModifiableCXXEntries,
    CXXEntriesAutoGen {
    override var compileFeatures: String? by recorder.observed(null)

    override val propertyNameToKey: Map<String, String>
        get() = super<ModifiableLanguageSpecifiedEntriesImpl>.propertyNameToKey +
                super<CXXEntriesAutoGen>.propertyNameToKey
}