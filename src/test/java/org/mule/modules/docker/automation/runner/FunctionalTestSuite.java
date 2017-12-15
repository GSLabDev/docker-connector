/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.functional.processors.BuildImageTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.CreateContainerTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.CreateVolumeTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.DeleteContainerTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.DockerInfoTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.InspectContainerTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.InspectImageTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.InspectVolumeTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.KillContainerTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.ListContainersTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.ListImageTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.ListVolumeTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.PauseContainerTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.PullImageTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.PushImageTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.RemoveImageTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.RemoveVolumeTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.RestartContainerTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.RunContainerTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.StartContainerTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.StopContainerTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.TagImageTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.UnpauseContainerTestCasesIT;
import org.mule.modules.docker.automation.functional.processors.WaitAContainerTestCasesIT;
import org.mule.modules.docker.automation.functional.sources.GetContainerLogsTestCasesIT;
import org.mule.modules.docker.automation.functional.sources.GetContainerStatsTestCasesIT;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Suite.class)
@SuiteClasses({
    BuildImageTestCasesIT.class,
    CreateVolumeTestCasesIT.class,
    DockerInfoTestCasesIT.class,
    InspectContainerTestCasesIT.class,
    StartContainerTestCasesIT.class,
    StopContainerTestCasesIT.class,
    RestartContainerTestCasesIT.class,
    KillContainerTestCasesIT.class,
    PauseContainerTestCasesIT.class,
    UnpauseContainerTestCasesIT.class,
    WaitAContainerTestCasesIT.class,
    DeleteContainerTestCasesIT.class,
    RunContainerTestCasesIT.class,
    ListContainersTestCasesIT.class,
    CreateContainerTestCasesIT.class,
    GetContainerLogsTestCasesIT.class,
    GetContainerStatsTestCasesIT.class,

    InspectImageTestCasesIT.class,
    InspectVolumeTestCasesIT.class,
    ListImageTestCasesIT.class,
    ListVolumeTestCasesIT.class,
    PullImageTestCasesIT.class,
    PushImageTestCasesIT.class,
    RemoveImageTestCasesIT.class,
    RemoveVolumeTestCasesIT.class,
    TagImageTestCasesIT.class
})

public class FunctionalTestSuite {

    @BeforeClass
    public static void initialiseSuite() {
        ConnectorTestContext.initialize(DockerConnector.class);
    }

    @AfterClass
    public static void shutdownSuite() {
        ConnectorTestContext.shutDown();
    }

}