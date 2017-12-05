/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
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

    CreateContainerResponse createContainerResponse = null;
    String signal = "SIGKILL";
    boolean removeVolumes = false;
    boolean showSize = false;
    int timeout = 0;
    String imageName = "busybox", imageTag = "latest", containerName = "created-test-restart";

    public RestartContainerTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        List<String> command = new ArrayList<String>();
        command.add("sleep");
        command.add("999");
        try {
            getConnector().pullImage(imageName, imageTag, null, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createContainerResponse = getConnector().runContainer(imageName, imageTag, containerName, command);
    }

    @After
    public void tearDown() {
        try {
            getConnector().killContainer(createContainerResponse.getId(), signal);
            getConnector().deleteContainer(containerName, true, removeVolumes);
        } catch (Exception e) {

        } finally {
            getConnector().removeImage(imageName, imageTag, true, false, null);
        }
    }

    @Test
    public void verifyDefault() {
        timeout = 0;
        assertNotNull(createContainerResponse.getId());
        InspectContainerResponse inspectContainerResponse = getConnector()
                .inspectContainer(createContainerResponse.getId(), showSize);
        String startTime = inspectContainerResponse.getState().getStartedAt();

        getConnector().restartContainer(createContainerResponse.getId(), timeout);

        InspectContainerResponse inspectContainerResponse2 = getConnector()
                .inspectContainer(createContainerResponse.getId(), showSize);
        String startTime2 = inspectContainerResponse2.getState().getStartedAt();
        assertNotSame(startTime, startTime2);
        assertTrue(inspectContainerResponse.getState().getRunning());
    }

    @Test
    public void verifyWithAll() {
        timeout = 10;
        assertNotNull(createContainerResponse.getId());
        InspectContainerResponse inspectContainerResponse = getConnector()
                .inspectContainer(createContainerResponse.getId(), showSize);
        String startTime = inspectContainerResponse.getState().getStartedAt();

        getConnector().restartContainer(createContainerResponse.getId(), timeout);

        InspectContainerResponse inspectContainerResponse2 = getConnector()
                .inspectContainer(createContainerResponse.getId(), showSize);
        String startTime2 = inspectContainerResponse2.getState().getStartedAt();
        assertNotSame(startTime, startTime2);
        assertTrue(inspectContainerResponse.getState().getRunning());
    }

}