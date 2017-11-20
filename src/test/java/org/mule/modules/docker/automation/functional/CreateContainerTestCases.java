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

public class CreateContainerTestCases extends AbstractTestCase<DockerConnector> {

    java.lang.String imageName = "busybox";
    java.lang.String imageTag = "latest";
    java.lang.String containerName = "Created-test";
    CreateContainerResponse container = null;

    public CreateContainerTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setp() throws InterruptedException {

        try {
            getConnector().pullImage(imageName, imageTag);
        } catch (Exception e) {
        }
    }

    @After
    public void tearDown() {
        getConnector().deleteContainer(container.getId(), true);
    }

    @Test
    public void verify() {

        container = getConnector().createContainer(imageName, imageTag, containerName);
        assertNotNull(container.getId());
        InspectContainerResponse inspectContainerResponse = getConnector().inspectContainer(containerName);
        assertNotNull(inspectContainerResponse.getId());
    }

}