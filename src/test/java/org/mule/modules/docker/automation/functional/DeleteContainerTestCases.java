/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DeleteContainerTestCases extends AbstractTestCase<DockerConnector> {

    java.lang.String containerName = "Created-Test-delete";
    java.lang.String imageName = "busybox", imageTag = "latest";
    boolean removeVolumes = false;
    com.github.dockerjava.api.command.CreateContainerResponse createContainerResponse = null;

    public DeleteContainerTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().pullImage(imageName, imageTag, null, null);
            createContainerResponse = getConnector().createContainer(imageName, imageTag, containerName, null);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void verify() {
        int containerCount = getConnector().dockerInfo().getContainers();
        assertTrue(containerCount > 1);
        getConnector().deleteContainer(containerName, true, removeVolumes);
        assertEquals(containerCount, (getConnector().dockerInfo().getContainers() + 1));
    }

}