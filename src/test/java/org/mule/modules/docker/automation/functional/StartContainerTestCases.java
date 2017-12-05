/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class StartContainerTestCases extends AbstractTestCase<DockerConnector> {

    CreateContainerResponse container = null;
    InspectContainerResponse inspectContainerResponse = null;
    java.lang.String imageName = "busybox", imageTag = "latest", containerName = "created-test-start", command = "top";
    boolean showSize = false;
    String signal = "SIGKILL";
    boolean removeVolumes = false;

    public StartContainerTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        container = getConnector().createContainer(imageName, imageTag, containerName, null);
        System.out.println("Created container " + container);
    }

    @After
    public void tearDown() {
        try {

            if (getConnector().inspectContainer(containerName,showSize).getState().getRunning()) {
                getConnector().killContainer(containerName, signal);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            getConnector().deleteContainer(containerName, true, removeVolumes);
        }
    }

    @Test
    public void verify() {
        assertNotNull(container.getId());
        getConnector().startContainer(container.getId());
        inspectContainerResponse = getConnector().inspectContainer(container.getId(), showSize);

        assertNotNull(inspectContainerResponse.getConfig());
        assertNotNull(inspectContainerResponse.getId());

        assertNotNull(inspectContainerResponse.getImageId());
        assertNotNull(inspectContainerResponse.getState());
        if (!inspectContainerResponse.getState().getRunning()) {
            assertSame(inspectContainerResponse.getState().getExitCode(), 0);
        }
    }

}