package com.zx.gradle.plugin


import org.gradle.api.tasks.testing.Test

class TestNgBasedTestTask extends Test {
    TestNgBasedTestTask() {
        useTestNG() {
            excludeGroups("localIntegrationTest", "remoteIntegrationTest")
        }
    }
}
