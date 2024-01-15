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

package io.github.isning.gradle.plugins.cmake.utils

import org.apache.commons.text.StringEscapeUtils

fun List<String>.findParameterValue(startWith: String): String? =
    this.splitByParameterPattern().let { split ->
        split.indexOfFirst { it.startsWith(startWith) }.let {
            if (it == -1) null
            else split.getOrNull(it + 1)
        }
    }


fun String.splitByParameterPattern(): List<String> =
    splitBy("""('[^'\\]*(?:\\.[^'\\]*)*')|("[^"\\]*[?:\\.[^"\\]*]*")|(\S+)""".toRegex()).unWarpQuotes()
        .flatBy("""(^-\w)(.*)|(--[\S]+)|(^[^-]\S+)""".toRegex()).unWarpQuotes()

fun String.splitBy(regex: Regex): List<String> =
    regex.findAll(this).flatMap { it.groupValues.drop(1).noEmpty() }.toList()

fun Iterable<String>.flatBy(regex: Regex): List<String> =
    flatMap { it.splitBy(regex) }

fun Iterable<String>.noEmpty(): Iterable<String> =
    filter { it.isNotEmpty() }

fun Iterable<String>.unWarpQuotes(): List<String> =
    map { it.unWarpQuotes() }

fun String.unWarpQuotes() =
    removeSurrounding("\"", "\"").takeIf { it != this }
        ?.let { StringEscapeUtils.unescapeXSI(it).trim() } ?: removeSurrounding("'", "'").takeIf { it != this } ?: this

fun String.warpQuotes() = "\"${StringEscapeUtils.escapeXSI(this)}\""

fun Iterable<String?>.splitByParameterPattern(): List<String> = flatMap { it?.splitByParameterPattern() ?: emptyList() }
