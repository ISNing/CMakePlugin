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
import io.github.isning.gradle.plugins.cmake.params.filteredValue
import io.github.isning.gradle.plugins.cmake.params.plus
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.assign
import org.slf4j.Logger
import java.util.*

abstract class AbstractCMakeExecuteTask : DefaultTask() {
    @get:Internal
    var neverUpToDate: Boolean = false

    @get:Input
    val neverUpToDateHacking: String
        get() = if (neverUpToDate) Date().time.toString() else ""

    @get:Input
    val executable: Property<String> = project.objects.property(String::class.java)

    @get:OutputDirectory
    val workingFolder: DirectoryProperty = project.objects.directoryProperty()

    @get:Input
    open val parameters: Property<CMakeParams> = project.objects.property(CMakeParams::class.java)

    @get:Internal
    open var interceptedLogger: Logger = logger

    init {
        group = TASK_GROUP_CMAKE
        description = "Execute CMake with specified parameters"
    }

    open fun configureFrom(configurations: List<CMakeExecutionConfiguration>) {
        for (configuration in configurations)
            configureFrom(configuration)
    }

    open fun configureFrom(configuration: CMakeExecutionConfiguration) {
        configuration.executable?.let { executable = it }
        configuration.workingFolder?.let { workingFolder = it }
        configuration.parameters?.let { parameters = parameters.orNull?.plus(it) ?: it }
    }

    @get:Input
    val cmdLine: List<String>
        get() = listOf(executable.getOrElse("cmake")).let {
            parameters.orNull?.filteredValue?.let { parameters -> it + parameters } ?: it
        }

    open fun doBeforeExecute() = Unit

    @TaskAction
    fun execute() {
        doBeforeExecute()
        val executor = CMakeExecutor(interceptedLogger, name)
        executor.exec(cmdLine, workingFolder.asFile.get())
        doAfterExecute()
    }

    open fun doAfterExecute() = Unit
}

open class CMakeExecuteTask : AbstractCMakeExecuteTask() {

    override val parameters: Property<CMakeParams> = project.objects.property(CMakeParams::class.java)

    private var doBeforeExecute: (() -> Unit)? = null
    private var doAfterExecute: (() -> Unit)? = null

    fun doBeforeExecute(func: (() -> Unit)?) {
        doBeforeExecute = func
    }

    fun doAfterExecute(func: (() -> Unit)?) {
        doAfterExecute = func
    }

    override fun doBeforeExecute() {
        doBeforeExecute?.invoke()
    }

    override fun doAfterExecute() {
        doAfterExecute?.invoke()
    }
}
