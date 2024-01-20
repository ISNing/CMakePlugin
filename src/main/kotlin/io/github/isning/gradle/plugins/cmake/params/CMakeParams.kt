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

import io.github.isning.gradle.plugins.cmake.utils.ElementsToBeRemovedRecorded
import io.github.isning.gradle.plugins.cmake.utils.matchesNoneOf
import java.io.Serializable

interface CMakeParams : Serializable {
    val value: List<String>
}

interface CMakeParamsValueFilteredParamsToBeRemovedRecorded : CMakeParamsParamsToBeRemovedRecorded

interface CMakeParamsParamsToBeRemovedRecorded : CMakeParams, ParamsToBeRemovedRecorded
interface ParamsToBeRemovedRecorded : ElementsToBeRemovedRecorded<String>

operator fun CMakeParams.plus(params: CMakeParams): CMakeParamsValueFilteredParamsToBeRemovedRecorded =
    CustomExplicitlyRemovedParamsRecordedCMakeParams(
        filteredValue.filterOutBy(params.explicitlyRemovedElements) + params.filteredValue,
        explicitlyRemovedElements + params.explicitlyRemovedElements
    )

val CMakeParams.explicitlyRemovedElements: Set<String>
    get() = if (this is CMakeParamsParamsToBeRemovedRecorded) explicitlyRemovedElements
    else emptySet()

val CMakeParams.filteredValue: List<String>
    get() = if (this is CMakeParamsValueFilteredParamsToBeRemovedRecorded) value
    else value.filterOutBy(explicitlyRemovedElements)

fun CMakeParams.replaceWith(original: String, replacement: String): CMakeParams =
    CustomExplicitlyRemovedParamsRecordedCMakeParams(
        filteredValue.map { it.replace(original, replacement) },
        explicitlyRemovedElements
    )

fun Iterable<String>.filterOutBy(regexps: Iterable<String>): List<String> = filter { it.matchesNoneOf(regexps) }

fun emptyCMakeParams(): CMakeParams = CustomCMakeParams(emptyList())

val CMakeParams?.orEmpty: CMakeParams
    get() = this ?: emptyCMakeParams()