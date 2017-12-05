/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.BadRequestException;

public class CreateContainerTestCases extends AbstractTestCase<DockerConnector> {

    java.lang.String imageName = "busybox", imageTag = "latest";
    java.lang.Boolean removeVolumes = false, showSize = false, forceDelete = true;
    CreateContainerResponse createContainerResponse;

    public CreateContainerTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setp() throws InterruptedException {

        try {
            getConnector().pullImage(imageName, imageTag, null, null);
        } catch (Exception e) {
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().deleteContainer(createContainerResponse.getId(), true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void verifyDefault() {
        String jsonFilePath = null, containerName = "create-container-test";
        ;
        createContainerResponse = getConnector().createContainer(imageName, imageTag, containerName, jsonFilePath);
        assertNotNull(createContainerResponse.getId());
        InspectContainerResponse inspectContainerResponse = getConnector().inspectContainer(containerName, showSize);
        assertNotNull(inspectContainerResponse.getId());
    }

    @Test
    public void verifyWithAll() {
        String jsonFilePath = "src/test/resources/createContainer.json", containerName = null, imageName = null,
                imageTag = null;
        createContainerResponse = getConnector().createContainer(imageName, imageTag, containerName, jsonFilePath);
        assertNotNull(createContainerResponse.getId());
        InspectContainerResponse inspectContainerResponse = getConnector()
                .inspectContainer(createContainerResponse.getId(), showSize);
        assertNotNull(inspectContainerResponse.getId());
    }

    @Test(expected = BadRequestException.class)
    public void verifyWithNonExistingFile() {
        String jsonFilePath = "notAFile.json", containerName = null, imageName = null, imageTag = null;
        getConnector().createContainer(imageName, imageTag, containerName, jsonFilePath);
    }

}