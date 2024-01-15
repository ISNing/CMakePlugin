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

import io.github.isning.gradle.plugins.cmake.params.CMakeParams
import io.github.isning.gradle.plugins.cmake.params.CustomCMakeParams
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.slf4j.Logger
import java.io.File

const val TASK_GROUP_CMAKE = "cmake"
const val TASK_NAME_CMAKE_LIST_GENERATORS = "cmakeListGenerators"
const val TASK_NAME_CMAKE_VERSION = "cmakeVersion"
const val TASK_NAME_CMAKE_HELP = "cmakeHelp"
const val TASK_NAME_CMAKE_CONFIGURE = "cmakeConfigure"
const val TASK_NAME_CMAKE_BUILD = "cmakeBuild"

class CMakePlugin : Plugin<Project> {
    private fun deleteDirectory(directoryToBeDeleted: File): Boolean {
        val allContents = directoryToBeDeleted.listFiles()
        if (allContents != null) {
            for (file in allContents) {
                deleteDirectory(file)
            }
        }
        return directoryToBeDeleted.delete()
    }

    override fun apply(project: Project) {
        project.plugins.apply("base")
        val extension = project.extensions
            .create("cmake", CMakePluginExtension::class.java, project)

        project.task("cmakeClean") {
            group = TASK_GROUP_CMAKE
            description = "Clean CMake configuration"
        }.doFirst {
            val workingFolder = extension.workingFolder?.absoluteFile
            if (workingFolder != null) {
                if (workingFolder.exists()) {
                    project.logger.info("Deleting folder $workingFolder")
                    if (!deleteDirectory(workingFolder)) {
                        throw GradleException("Could not delete working folder $workingFolder")
                    }
                }
            }
        }

        project.tasks.register(TASK_NAME_CMAKE_LIST_GENERATORS, CMakeExecuteTask::class.java) {
            group = TASK_GROUP_CMAKE
            description = "List available CMake generators"
            neverUpToDate = true
            interceptedLogger = object : Logger by logger {
                var foundGenerators = false
                override fun info(message: String?) {
                    message?.let {
                        if (it.startsWith("Generators")) {
                            foundGenerators = true
                        }
                        if (foundGenerators) {
                            logger.log(LogLevel.QUIET, it)
                        }
                    }
                }
            }
            configureFrom(object : CMakeExecutionConfiguration {
                override val executable: String?
                    get() = extension.executable
                override val workingFolder: File?
                    get() = extension.workingFolder
                override val parameters: CMakeParams =
                    CustomCMakeParams(listOf("--help"))
            })
        }

        project.tasks.register(TASK_NAME_CMAKE_VERSION, CMakeExecuteTask::class.java) {
            group = TASK_GROUP_CMAKE
            description = "Print CMake version"
            neverUpToDate = true
            interceptedLogger = object : Logger by logger {
                override fun info(message: String?) {
                    message?.let {
                        logger.log(LogLevel.QUIET, it)
                    }
                }
            }
            configureFrom(object : CMakeExecutionConfiguration {
                override val executable: String?
                    get() = extension.executable
                override val workingFolder: File?
                    get() = extension.workingFolder
                override val parameters: CMakeParams =
                    CustomCMakeParams(listOf("--version"))
            })
        }

        project.tasks.register(TASK_NAME_CMAKE_HELP, CMakeExecuteTask::class.java) {
            group = TASK_GROUP_CMAKE
            description = "Print CMake help"
            neverUpToDate = true
            interceptedLogger = object : Logger by logger {
                override fun info(message: String?) {
                    message?.let {
                        logger.log(LogLevel.QUIET, it)
                    }
                }
            }
            configureFrom(object : CMakeExecutionConfiguration {
                override val executable: String?
                    get() = extension.executable
                override val workingFolder: File?
                    get() = extension.workingFolder
                override val parameters: CMakeParams =
                    CustomCMakeParams(listOf("--help"))
            })
        }

        project.afterEvaluate {
            val tasks = project.tasks
            project.cmakeExtension.registerTasks(listOf(), emptyList())

            tasks.named("clean").configure { dependsOn("cmakeClean") }
            tasks.named("build").configure {
                dependsOn(
                    tasks.withType(
                        CMakeBuildTask::class.java
                    )
                )
            }
        }
    }
}