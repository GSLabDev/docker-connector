/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class RunContainerTestCases extends AbstractTestCase<DockerConnector> {

    com.github.dockerjava.api.command.CreateContainerResponse expected = null;
    java.lang.String imageName = "busybox";
    java.lang.String imageTag = "latest";
    java.lang.String containerName = "created-test-run";


    public RunContainerTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {

        try {
            getConnector().pullImage(imageName, imageTag);
        } catch (Exception e) {
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().stopContainer(containerName);
            getConnector().deleteContainer(containerName, true);
        } catch (Exception e) {
            getConnector().killContainer(containerName);
        }

    }

    @Test
    public void verify() {
        List<String> command = new ArrayList<String>();
        command.add("sleep");
        command.add("999");
        CreateContainerResponse container = getConnector().runContainer(imageName, imageTag, containerName, command);
        InspectContainerResponse inspect = getConnector().inspectContainer(container.getId());
        assertTrue(inspect.getState().getRunning());
    }

}