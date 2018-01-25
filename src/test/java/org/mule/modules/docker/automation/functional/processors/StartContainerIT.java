/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class StartContainerIT extends AbstractTestCase<DockerConnector> {

    CreateContainerResponse container = null;
    InspectContainerResponse inspectContainerResponse = null;

    public StartContainerIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        container = getConnector().createContainer(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, TestsConstants.START_CONTAINER, null);
    }

    @After
    public void tearDown() {
        try {

            if (getConnector().inspectContainer(TestsConstants.START_CONTAINER, false).getState().getRunning()) {
                getConnector().killContainer(TestsConstants.START_CONTAINER, TestsConstants.KILL_CONTAINER_SIGNAL);
            }
        } catch (Exception e) {

        } finally {
            getConnector().deleteContainer(TestsConstants.START_CONTAINER, true, true);
        }
    }

    @Test
    public void verifyStartContainer() {
        assertNotNull(container.getId());
        getConnector().startContainer(container.getId());
        inspectContainerResponse = getConnector().inspectContainer(container.getId(), false);

        assertNotNull(inspectContainerResponse.getConfig());
        assertNotNull(inspectContainerResponse.getId());

        assertNotNull(inspectContainerResponse.getImageId());
        assertNotNull(inspectContainerResponse.getState());
        System.out.println("inspectContainerResponse.getState():" + inspectContainerResponse.getState());
        if (!inspectContainerResponse.getState().getRunning()) {
            assertSame(inspectContainerResponse.getState().getExitCode(), 0);
        }
    }

}