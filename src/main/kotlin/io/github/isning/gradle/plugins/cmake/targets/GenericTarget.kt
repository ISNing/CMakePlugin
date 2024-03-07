package io.github.isning.gradle.plugins.cmake.targets

import io.github.isning.gradle.plugins.cmake.CMakeConfiguration
import io.github.isning.gradle.plugins.cmake.CMakeTargetImpl
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeBuildParams
import io.github.isning.gradle.plugins.cmake.params.ModifiableCMakeBuildParamsImpl
import io.github.isning.gradle.plugins.cmake.params.entries.platform.ModifiableGenericEntries
import io.github.isning.gradle.plugins.cmake.params.platform.ModifiableGenericParams
import io.github.isning.gradle.plugins.cmake.params.platform.ModifiableGenericParamsImpl
import org.gradle.api.Project

class GenericTarget(
    project: Project,
    name: String,
    inheritedParents: List<CMakeConfiguration>,
    inheritedNames: List<String>
) :
    CMakeTargetImpl<ModifiableGenericParams<ModifiableGenericEntries>, ModifiableCMakeBuildParams>(
        project, name, inheritedParents, inheritedNames,
        { ModifiableGenericParamsImpl() },
        { ModifiableCMakeBuildParamsImpl() }
    )
