/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.docker.automation.functional.BuildImageTestCases;
import org.mule.modules.docker.automation.functional.CreateContainerTestCases;
import org.mule.modules.docker.automation.functional.CreateVolumeTestCases;
import org.mule.modules.docker.automation.functional.DeleteContainerTestCases;
import org.mule.modules.docker.automation.functional.DockerInfoTestCases;
import org.mule.modules.docker.automation.functional.GetContainerLogsTestCases;
import org.mule.modules.docker.automation.functional.GetContainerStatsTestCases;
import org.mule.modules.docker.automation.functional.InspectContainerTestCases;
import org.mule.modules.docker.automation.functional.InspectImageTestCases;
import org.mule.modules.docker.automation.functional.InspectVolumeTestCases;
import org.mule.modules.docker.automation.functional.KillContainerTestCases;
import org.mule.modules.docker.automation.functional.ListContainersTestCases;
import org.mule.modules.docker.automation.functional.ListImageTestCases;
import org.mule.modules.docker.automation.functional.ListVolumeTestCases;
import org.mule.modules.docker.automation.functional.PauseContainerTestCases;
import org.mule.modules.docker.automation.functional.PullImageTestCases;
import org.mule.modules.docker.automation.functional.PushImageTestCases;
import org.mule.modules.docker.automation.functional.RemoveImageTestCases;
import org.mule.modules.docker.automation.functional.RemoveVolumeTestCases;
import org.mule.modules.docker.automation.functional.RestartContainerTestCases;
import org.mule.modules.docker.automation.functional.RunContainerTestCases;
import org.mule.modules.docker.automation.functional.StartContainerTestCases;
import org.mule.modules.docker.automation.functional.StopContainerTestCases;
import org.mule.modules.docker.automation.functional.TagImageTestCases;
import org.mule.modules.docker.automation.functional.UnpauseContainerTestCases;
import org.mule.modules.docker.automation.functional.WaitAContainerTestCases;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Suite.class)
@SuiteClasses({ BuildImageTestCases.class, CreateContainerTestCases.class, CreateVolumeTestCases.class,
        DeleteContainerTestCases.class, DockerInfoTestCases.class, InspectContainerTestCases.class,
        InspectImageTestCases.class, InspectVolumeTestCases.class, KillContainerTestCases.class,
        GetContainerLogsTestCases.class, GetContainerStatsTestCases.class, ListContainersTestCases.class,
        ListImageTestCases.class, ListVolumeTestCases.class, PauseContainerTestCases.class, PullImageTestCases.class,
        PushImageTestCases.class, RemoveImageTestCases.class, RemoveVolumeTestCases.class,
        RestartContainerTestCases.class, RunContainerTestCases.class, StartContainerTestCases.class,
        StopContainerTestCases.class, TagImageTestCases.class, UnpauseContainerTestCases.class,
        WaitAContainerTestCases.class })

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