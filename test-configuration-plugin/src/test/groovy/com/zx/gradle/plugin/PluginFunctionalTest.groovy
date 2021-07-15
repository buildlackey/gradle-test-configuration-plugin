package com.zx.gradle.plugin

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class PluginFunctionalTest extends Specification {
    @Rule
    TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            plugins {
                id 'scala'
                id 'com.zx.test-configuration'
            }
            
            
            repositories {
                mavenCentral()
            }
                    
            dependencies {
                implementation 'org.scala-lang:scala-library:2.13.2'
                
                testImplementation group: "org.testng", name: "testng", version: "6.8"
                testImplementation 'org.scalatest:scalatest_2.13:3.1.2'
                
            }
            
        """


        File scalaTestDir = new File(testProjectDir.getRoot().canonicalPath +  "/src/test/scala")
        scalaTestDir.mkdirs()


        File unitTest = new File(scalaTestDir.getAbsolutePath(), "UnitTest.scala")
        unitTest << """
            import org.testng.annotations.Test
            class UnitTest {
              @Test def test(): Unit = {
                  if ( "true" == System.getenv("testShouldFail") ) {
                     assert(2> 10)
                  } else {
                     assert(2> 1)
                 }
               }
            }
            """

        File localIntegrationTest = new File(scalaTestDir.getAbsolutePath(), "LocalIntegrationTest.scala")
        localIntegrationTest << """
            import org.testng.annotations.Test
            class LocalIntegrationTest {
              @Test(groups = Array("localIntegrationTest")) def test(): Unit = {
                  if ( "true" == System.getenv("localIntegrationTestShouldFail") ) {
                     assert(2> 10)
                  } else {
                     assert(2> 1)
                 }
               }
            }
            """


        File remoteIntegrationTest = new File(scalaTestDir.getAbsolutePath(), "RemoteIntegrationTest.scala")
        remoteIntegrationTest << """
            import org.testng.annotations.Test
            class RemoteIntegrationTest {
              @Test(groups = Array("remoteIntegrationTest")) def test(): Unit = {
                  if ( "true" == System.getenv("remoteIntegrationTestShouldFail") ) {
                     assert(2> 10)
                  } else {
                     assert(2> 1)
                 }
               }
            }
            """
    }

    def "unitTest passes as expected"() {
        given:

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('test')
                .withEnvironment(
                        [
                                "testShouldFail": "true",
                                "remoteIntegrationTestShouldFail": "true",
                                "localIntegrationTestShouldFail": "true"
                        ])
                .withPluginClasspath()
                .buildAndFail()

        then:
        result.task(":test").outcome != SUCCESS
    }

    def "unitTest fails as expected"() {
        given:

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('test')
                .withEnvironment(
                        [
                                "testShouldFail": "false",
                                "remoteIntegrationTestShouldFail": "false",
                                "localIntegrationTestShouldFail": "false"
                        ])
                .withPluginClasspath()
                .build()

        then:
        result.task(":test").outcome == SUCCESS
    }

    def "localIntegrationTest passes as expected due exclusion configured in over-ridden test task"() {
        given:

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('test')  // Invocation w/ target=="localIntegrationTest would cause failure
                .withEnvironment(
                        [
                                "localIntegrationTestShouldFail": "true"
                        ])
                .withPluginClasspath()
                .build()

        then:
        result.task(":test").outcome == SUCCESS
    }

    def "localIntegrationTest fails as expected "() {
        given:

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('localIntegrationTest')
                .withEnvironment(
                        [
                                "localIntegrationTestShouldFail": "true"
                        ])
                .withPluginClasspath()
                .buildAndFail()

        then:
        result.task(":localIntegrationTest").outcome != SUCCESS
    }

    def "remoteIntegrationTest passes as expected"() {
        given:

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('remoteIntegrationTest')
                .withEnvironment(
                        [
                                "testShouldFail": "true",
                                "remoteIntegrationTestShouldFail": "false",
                                "localIntegrationTestShouldFail": "true"
                        ])
                .withPluginClasspath()
                .build()

        then:
        result.task(":remoteIntegrationTest").outcome == SUCCESS
    }
}