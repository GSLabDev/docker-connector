/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DeleteContainerIT extends AbstractTestCase<DockerConnector> {

    com.github.dockerjava.api.command.CreateContainerResponse createContainerResponse = null;

    public DeleteContainerIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().pullImage(TestsConstants.CREATE_CONTAINERS_IMAGE, TestsConstants.CREATE_CONTAINERS_IMAGE_TAG, null, null);
            createContainerResponse = getConnector().createContainer(TestsConstants.CREATE_CONTAINERS_IMAGE, TestsConstants.CREATE_CONTAINERS_IMAGE_TAG,
                    TestsConstants.DELETE_CONTAINER, null);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Test
    public void verifyDeleteContainer() {
        int containerCount = getConnector().dockerInfo().getContainers();
        getConnector().deleteContainer(TestsConstants.DELETE_CONTAINER, TestsConstants.DELETE_CONTAINER_FORCE_REMOVE, TestsConstants.DELETE_CONTAINER_REMOVE_VOLUME);
        assertEquals(containerCount, (getConnector().dockerInfo().getContainers() + 1));
    }

}