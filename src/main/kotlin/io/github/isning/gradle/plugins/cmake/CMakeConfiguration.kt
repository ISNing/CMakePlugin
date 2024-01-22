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

import io.github.isning.gradle.plugins.cmake.params.*
import io.github.isning.gradle.plugins.cmake.utils.Delegated
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.internal.Factory
import java.io.File
import java.io.Serializable

interface CMakeConfiguration {
    val executable: String?
    val workingFolder: File?
    val configParams: CMakeParams?
    val buildParams: CMakeParams?
}

interface ModifiableCMakeConfiguration<C : CMakeParams?, B : CMakeParams?> : CMakeConfiguration {
    override var executable: String?
    override var workingFolder: File?
    override var configParams: CMakeParams?
    override var buildParams: CMakeParams?
    val cleanConfigParamsFactory: Factory<C>
    val cleanBuildParamsFactory: Factory<B>

    fun configParams(configure: C.() -> Unit) =
        cleanConfigParamsFactory.create()!!.apply(configure).let { new ->
            configParams = configParams?.let { it + new } ?: new
        }

    fun buildParams(configure: B.() -> Unit) =
        cleanBuildParamsFactory.create()!!.apply(configure).let { new ->
            buildParams = buildParams?.let { it + new } ?: new
        }
}

class CustomCMakeConfiguration(
    override val executable: String?,
    override val workingFolder: File?,
    override val configParams: CMakeParams?,
    override val buildParams: CMakeParams?,
) : CMakeConfiguration

abstract class AbstractCMakeConfiguration<C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams>(
    val project: Project,
) :
    ModifiableCMakeConfiguration<C, B> {
    val executableProp: Property<String> = project.objects.property(String::class.java)
    val workingFolderProp: DirectoryProperty = project.objects.directoryProperty()
    val configParamsProp: Property<CMakeParams> = project.objects.property(CMakeParams::class.java)
    val buildParamsProp: Property<CMakeParams> = project.objects.property(CMakeParams::class.java)
    override var executable: String?
        get() = executableProp.orNull
        set(value) = executableProp.set(value)
    override var workingFolder: File?
        get() = workingFolderProp.orNull?.asFile
        set(value) = workingFolderProp.set(value)

    override var configParams: CMakeParams?
        get() = configParamsProp.orNull
        set(value) = configParamsProp.set(value)
    override var buildParams: CMakeParams?
        get() = buildParamsProp.orNull
        set(value) = buildParamsProp.set(value)
}

open class CMakeConfigurationDelegatedWithOverlay<T : CMakeConfiguration>(
    override val delegate: T,
    private val buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    private val configParamsInitialOverlayProvider: () -> CMakeParams? = { null }
) : CMakeConfiguration, Delegated<T> {
    override val executable: String?
        get() = delegate.executable
    override val workingFolder: File?
        get() = delegate.workingFolder
    override val configParams: CMakeParams?
        get() = configParamsInitialOverlay.orEmpty + delegate.configParams.orEmpty
    override val buildParams: CMakeParams?
        get() = buildParamsInitialOverlay.orEmpty + delegate.buildParams.orEmpty
    open val configParamsInitialOverlay: CMakeParams?
        get() = configParamsInitialOverlayProvider()
    open val buildParamsInitialOverlay: CMakeParams?
        get() = buildParamsInitialOverlayProvider()
}

fun <T : CMakeConfiguration> T.delegateWith(
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
): CMakeConfiguration = CMakeConfigurationDelegatedWithOverlay(
    this,
    buildParamsInitialOverlayProvider,
    configParamsInitialOverlayProvider,
)

open class ModifiableCMakeConfigurationDelegatedWithOverlay<T : ModifiableCMakeConfiguration<C, B>, C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams>(
    override val delegate: T,
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
) : CMakeConfigurationDelegatedWithOverlay<T>(
    delegate,
    buildParamsInitialOverlayProvider,
    configParamsInitialOverlayProvider,
), ModifiableCMakeConfiguration<C, B> {
    override var configParams: CMakeParams?
        get() = super.configParams
        set(value) {
            delegate.configParams = value
        }
    override var buildParams: CMakeParams?
        get() = super.buildParams
        set(value) {
            delegate.buildParams = value
        }
    override var executable: String?
        get() = delegate.executable
        set(value) {
            delegate.executable = value
        }
    override var workingFolder: File?
        get() = delegate.workingFolder
        set(value) {
            delegate.workingFolder = value
        }

    override val cleanConfigParamsFactory: Factory<C>
        get() = delegate.cleanConfigParamsFactory
    override val cleanBuildParamsFactory: Factory<B>
        get() = delegate.cleanBuildParamsFactory
}

fun <T : ModifiableCMakeConfiguration<C, B>, C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams> T.delegateWith(
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
): ModifiableCMakeConfiguration<C, B> = ModifiableCMakeConfigurationDelegatedWithOverlay(
    this,
    buildParamsInitialOverlayProvider,
    configParamsInitialOverlayProvider,
)

open class CMakeConfigurationWithOverlay<C : ModifiableCMakeGeneralParams, B : ModifiableCMakeBuildParams>(
    project: Project,
    buildParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    configParamsInitialOverlayProvider: () -> CMakeParams? = { null },
    cleanConfigParamsFactory: Factory<C> = Factory {
        error("cleanConfigParamsFactory is not set")
    },
    cleanBuildParamsFactory: Factory<B> = Factory {
        error("cleanBuildParamsFactory is not set")
    },
) : ModifiableCMakeConfigurationDelegatedWithOverlay<ModifiableCMakeConfiguration<C, B>, C, B>(
    object : AbstractCMakeConfiguration<C, B>(project) {
        override val cleanConfigParamsFactory: Factory<C> = cleanConfigParamsFactory
        override val cleanBuildParamsFactory: Factory<B> = cleanBuildParamsFactory
    },
    buildParamsInitialOverlayProvider,
    configParamsInitialOverlayProvider
)

interface CMakeExecutionConfiguration {
    val executable: String?
    val workingFolder: File?
    val parameters: CMakeParams?
}

class CustomCMakExecutionConfiguration(
    override val executable: String? = null,
    override val workingFolder: File? = null,
    override val parameters: CMakeParams? = null,
) : CMakeExecutionConfiguration

operator fun CMakeExecutionConfiguration.plus(other: CMakeExecutionConfiguration) = CustomCMakExecutionConfiguration(
    executable = other.executable ?: executable,
    workingFolder = other.workingFolder ?: workingFolder,
    parameters = other.parameters ?: parameters?.let { other.parameters?.let { o -> o + it } ?: it },
)