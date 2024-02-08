package io.github.isning.gradle.plugins.cmake.utils

fun String.parsePairs(): Map<String, String> =
    splitByWhiteSpaceRespectingQuotes().associate { it.split("=").let { (k, v) -> k to v } }

fun List<String>.parsePairs(): Map<String, String> =
    fold(emptyMap()) { acc, s -> acc + s.parsePairs() }
