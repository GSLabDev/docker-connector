/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class RestartContainerTestCases extends AbstractTestCase<DockerConnector> {

    CreateContainerResponse container = null;
    String imageName = "busybox", imageTag = "latest", containerName = "created-test-restart";

    public RestartContainerTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        List<String> command = new ArrayList<String>();
        command.add("sleep");
        command.add("999");
        container = getConnector().runContainer(imageName, imageTag, containerName, command);
    }

    @After
    public void tearDown() {
        try {
            getConnector().killContainer(container.getId());
            getConnector().deleteContainer(containerName, true);
        } catch (Exception e) {

        }
    }

    @Test
    public void verify() {
        assertNotNull(container.getId());
        InspectContainerResponse inspectContainerResponse = getConnector().inspectContainer(container.getId());
        String startTime = inspectContainerResponse.getState().getStartedAt();

        getConnector().restartContainer(container.getId());

        InspectContainerResponse inspectContainerResponse2 = getConnector().inspectContainer(container.getId());
        String startTime2 = inspectContainerResponse2.getState().getStartedAt();
        assertNotSame(startTime, startTime2);
        assertTrue(inspectContainerResponse.getState().getRunning());
    }

}