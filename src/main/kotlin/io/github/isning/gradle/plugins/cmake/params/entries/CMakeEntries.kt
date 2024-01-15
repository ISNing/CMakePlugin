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

package io.github.isning.gradle.plugins.cmake.params.entries

import io.github.isning.gradle.plugins.cmake.params.CMakeParams
import io.github.isning.gradle.plugins.cmake.utils.ElementsToBeRemovedRecorded
import io.github.isning.gradle.plugins.cmake.utils.ExplicitlyRemovedElementsRecorderImpl
import io.github.isning.gradle.plugins.cmake.utils.warpQuotes
import java.io.Serializable
import kotlin.reflect.full.memberProperties

/**
 * This interface represents the entries for the CMake build system.
 * It provides a map of key-value pairs representing the CMake entries.
 */
interface CMakeCacheEntries : Serializable {
    /**
     * This map represents the key-value pairs of the CMake entries.
     * The keys are the CMake entry names and the values are the corresponding values.
     */
    val value: Map<String, String>
}

interface CMakeCacheEntriesWithValueAutoGenerated : CMakeCacheEntries {
    override val value: Map<String, String>
        get() = this::class.memberProperties.associate {
            when (it.name) {
                "value" -> null to null
                "propertyNameToKey" -> null to null
                else -> propertyNameToKey[it.name] to it.getter.call(this)?.toString()?.warpQuotes()
            }
        }.filter { it.key != null && it.value != null }.map { it.key!! to it.value!! }.toMap()

    val propertyNameToKey: Map<String, String>
}

interface IHyperCMakeCacheEntries : CMakeCacheEntriesWithValueAutoGenerated,
    CMakeEntriesEntriesToBeRemovedRecorded {
    val recorder: ExplicitlyRemovedElementsRecorderImpl<String>
    override val explicitlyRemovedElements: Set<String>
}

abstract class HyperCMakeCacheEntries : IHyperCMakeCacheEntries {
    override val recorder = ExplicitlyRemovedElementsRecorderImpl {
        propertyNameToKey[name]!!
    }

    final override val explicitlyRemovedElements: Set<String>
        get() = recorder.explicitlyRemovedElements
}

interface CMakeEntriesValueFilteredEntriesToBeRemovedRecorded : CMakeEntriesEntriesToBeRemovedRecorded
interface CMakeEntriesEntriesToBeRemovedRecorded : CMakeCacheEntries, EntriesToBeRemovedRecorded
interface EntriesToBeRemovedRecorded : ElementsToBeRemovedRecorded<String>

/**
 * This converts the CMake entries to CMake parameters.
 * It returns a CMakeParams object where each entry is represented as a string in the format "-Dkey=value".
 */
val CMakeCacheEntries.asCMakeParams: CMakeParams
    get() = object : CMakeParams {
        override val value: List<String>
            get() = this@asCMakeParams.filteredValue.map { "-D${it.key}=${it.value}" }
    }

operator fun <T : CMakeCacheEntries> T.invoke(configure: T.() -> Unit): Unit = configure()

operator fun CMakeCacheEntries.plus(params: CMakeCacheEntries): CMakeCacheEntries =
    CustomExplicitlyRemovedEntriesRecordedCMakeEntries(
        filteredValue.filterOutBy(params.explicitlyRemovedElements) + params.filteredValue,
        explicitlyRemovedElements + params.explicitlyRemovedElements
    )

val CMakeCacheEntries.explicitlyRemovedElements: Set<String>
    get() = if (this is CMakeEntriesEntriesToBeRemovedRecorded) explicitlyRemovedElements
    else emptySet()

val CMakeCacheEntries.filteredValue: Map<String, String>
    get() = if (this is CMakeEntriesValueFilteredEntriesToBeRemovedRecorded) value
    else value.filterOutBy(explicitlyRemovedElements)

fun Map<String, String>.filterOutBy(keys: Iterable<String>): Map<String, String> = filterNot { keys.contains(it.key) }