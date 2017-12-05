/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class KillContainerTestCases extends AbstractTestCase<DockerConnector> {
    CreateContainerResponse container = null;
    String imageName = "busybox";
    String imageTag = "latest";
    String containerName = "created-test-kill";
    String signal = "SIGKILL";
    boolean removeVolumes = false;
    boolean showSize = false;

    public KillContainerTestCases() {
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
            getConnector().deleteContainer(containerName, true, removeVolumes);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Test
    public void verify() {
        getConnector().killContainer(containerName, signal);
        InspectContainerResponse inspectContainerResponse = getConnector().inspectContainer(container.getId(),
                showSize);
        assertFalse(inspectContainerResponse.getState().getRunning());
        assertThat(inspectContainerResponse.getState().getExitCode(), not(equalTo(0)));
    }

}