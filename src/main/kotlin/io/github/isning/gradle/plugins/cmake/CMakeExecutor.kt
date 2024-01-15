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

package io.github.isning.gradle.plugins.cmake

import io.github.isning.gradle.plugins.cmake.utils.splitByParameterPattern
import org.gradle.api.GradleException
import org.gradle.api.GradleScriptException
import org.slf4j.Logger
import java.io.*
import java.util.concurrent.*

class CMakeExecutor internal constructor(
    private val logger: Logger, private val taskName: String
) {
    @Throws(GradleException::class)
    internal fun exec(cmdLine: List<String?>, workingFolder: File) {
        val splitCmdLine = cmdLine.splitByParameterPattern()
        logger.info("Executing command line: ${splitCmdLine.joinToString(" ")}")

        val pb = ProcessBuilder(splitCmdLine)
        pb.directory(workingFolder)

        val executor = Executors.newFixedThreadPool(2)

        try {
            if (!workingFolder.exists()) workingFolder.mkdirs()

            val process = pb.start()

            val stdoutFuture = executor.submit<Unit> {
                readStream(process.inputStream, true)
            }
            val stderrFuture = executor.submit<Unit> {
                readStream(process.errorStream, false)
            }

            val retCode = process.waitFor()
            warnIfTimeout(
                stdoutFuture,
                "Warning: timed out waiting for stdout to be closed."
            )
            warnIfTimeout(
                stderrFuture,
                "Warning: timed out waiting for stderr to be closed."
            )
            if (retCode != 0) {
                throw GradleException("Error: CMake returned $retCode")
            }
        } catch (e: IOException) {
            throw GradleScriptException("Error: Got an exception while running $taskName", e)
        } catch (e: InterruptedException) {
            throw GradleScriptException("Error: Got an exception while running $taskName", e)
        } catch (e: ExecutionException) {
            throw GradleScriptException("Error: Got an exception while running $taskName", e)
        } finally {
            executor.shutdown()
        }
    }

    private fun readStream(inputStream: InputStream, isStdOut: Boolean) {
        val lines = BufferedReader(InputStreamReader(inputStream)).lines()
        if (isStdOut) {
            lines.forEach { s: String? -> logger.info(s) }
        } else {
            lines.forEach { s: String? -> logger.error(s) }
        }
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    private fun warnIfTimeout(future: Future<Unit>, message: String) {
        try {
            future[3, TimeUnit.SECONDS]
        } catch (e: TimeoutException) {
            logger.warn(message)
        }
    }
}

