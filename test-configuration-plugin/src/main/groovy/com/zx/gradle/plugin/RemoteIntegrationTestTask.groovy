package com.zx.gradle.plugin


import org.gradle.api.tasks.testing.Test

class RemoteIntegrationTestTask extends Test {
    RemoteIntegrationTestTask() {
        useTestNG() {
            includeGroups("remoteIntegrationTest")
        }
    }
}
