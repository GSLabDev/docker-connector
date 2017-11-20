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

public class InspectContainerTestCases extends AbstractTestCase<DockerConnector> {

    com.github.dockerjava.api.command.InspectContainerResponse inspectContainerResponse = null;
    java.lang.String containerName = "Created-test-inspect", imageName = "busybox", imageTag = "latest";

    public InspectContainerTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().pullImage(imageName, imageTag);
            getConnector().createContainer(imageName, imageTag, containerName);
            getConnector().startContainer(containerName);
        } catch (Exception e) {

        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().deleteContainer(containerName, true);
        } catch (Exception e) {
        }
    }

    @Test
    public void verify() {
        inspectContainerResponse = getConnector().inspectContainer(containerName);
        assertNotNull(inspectContainerResponse.getId());
        assertEquals(inspectContainerResponse.getName(), "/" + containerName);
    }

}