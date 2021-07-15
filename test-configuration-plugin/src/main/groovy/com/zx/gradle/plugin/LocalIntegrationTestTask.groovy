package com.zx.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.testing.Test
import org.gradle.internal.impldep.org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutorService

class LocalIntegrationTestTask extends Test {
    LocalIntegrationTestTask() {
        useTestNG() {
            includeGroups("localIntegrationTest")
        }
    }
}
