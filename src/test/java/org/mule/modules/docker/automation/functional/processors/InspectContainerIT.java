/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.InspectContainerResponse;

public class InspectContainerIT extends AbstractTestCase<DockerConnector> {

    InspectContainerResponse inspectContainerResponse = null;

    public InspectContainerIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().pullImage(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, null, null);
            getConnector().createContainer(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, TestsConstants.INSPECT_CONTAINER, null);
            getConnector().startContainer(TestsConstants.INSPECT_CONTAINER);
        } catch (Exception e) {

        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().deleteContainer(TestsConstants.INSPECT_CONTAINER, true, true);
        } catch (Exception e) {
        }
    }

    @Test
    public void verifyInspectContainer() {
        inspectContainerResponse = getConnector().inspectContainer(TestsConstants.INSPECT_CONTAINER, TestsConstants.INSPECT_CONTAINER_SHOW_SIZE);
        assertNotNull(inspectContainerResponse.getId());
        assertEquals(inspectContainerResponse.getName(), "/" + TestsConstants.INSPECT_CONTAINER);
    }

}