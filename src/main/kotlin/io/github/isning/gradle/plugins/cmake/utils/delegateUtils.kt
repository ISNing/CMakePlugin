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

interface Delegated<T> {
    val delegate: T
}

val <T> Delegated<T>.rootDelegate: T
    get() {
        var currentDelegate = delegate
        while (currentDelegate is Delegated<*>) {
            @Suppress("UNCHECKED_CAST")
            currentDelegate = currentDelegate.delegate as? T ?: break
        }
        return currentDelegate
    }

val <T> Delegated<T>.delegates: Sequence<*>
    get() = sequence {
        var currentDelegate: Any? = delegate
        while (currentDelegate is Delegated<*>) {
            currentDelegate = currentDelegate.delegate
            yield(currentDelegate)
        }
    }

inline fun <reified T, O, R> O.runIfIs(block: T.(O) -> R): R? =
    when (this) {
        is T -> {
            block(this)
        }

        is Delegated<*> -> {
            delegates.mapNotNull { if (it is T) it else null }.first().run { block(this@runIfIs) }
        }

        else -> null
    }