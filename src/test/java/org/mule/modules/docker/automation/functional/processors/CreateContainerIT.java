/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.BadRequestException;

public class CreateContainerIT extends AbstractTestCase<DockerConnector> {
    private CreateContainerResponse createContainerResponse;

    public CreateContainerIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setp() throws InterruptedException {

        try {
            getConnector().pullImage(TestsConstants.CREATE_CONTAINERS_IMAGE, TestsConstants.CREATE_CONTAINERS_IMAGE_TAG, null, null);
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
    public void verifyWithoutJsonFile() {
        createContainerResponse = getConnector().createContainer(TestsConstants.CREATE_CONTAINERS_IMAGE, TestsConstants.CREATE_CONTAINERS_IMAGE_TAG, TestsConstants.CREATE_CONTAINER, null);
        assertNotNull(createContainerResponse.getId());
        InspectContainerResponse inspectContainerResponse = getConnector().inspectContainer(TestsConstants.CREATE_CONTAINER, true);
        assertNotNull(inspectContainerResponse.getId());
    }

    @Test
    public void verifyWithJsonFile() {
        try {
            createContainerResponse = getConnector().createContainer(null, null, null, TestsConstants.CREATE_CONTAINERS_JSON_FILE_PATH);
        } catch (Exception e) {
            System.out.println("verifyWithJsonFile error:" + e.getMessage() + "\n" +e);
        }
        assertNotNull(createContainerResponse.getId());
        InspectContainerResponse inspectContainerResponse = getConnector()
                .inspectContainer(createContainerResponse.getId(), true);
        assertNotNull(inspectContainerResponse.getId());
    }

    @Test(expected = BadRequestException.class)
    public void verifyWithNonExistingFile() {
        getConnector().createContainer(null, null, null, "notAFile.json");
    }

}