package com.zx.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * (1) Reconfigures the 'test' task to use testNG, and to exclude the groups, localIntegrationTest and remoteIntegrationTest,
 * and (2) adds additional testNG tasks by the same name as the aforementioned two groups, each of which
 * will only run tests annotated as: @Test(groups = Array("<local|remote>IntegrationTest")). The project unit test
 * provides explicit examples.
 */
class TestConfigurationPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.tasks.create('localIntegrationTest', LocalIntegrationTestTask) { }
        project.tasks.create('remoteIntegrationTest', RemoteIntegrationTestTask) { }
        project.tasks.replace("test", TestNgBasedTestTask)
    }
}
