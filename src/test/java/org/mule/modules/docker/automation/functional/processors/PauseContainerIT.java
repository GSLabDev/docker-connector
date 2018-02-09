/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class PauseContainerIT extends AbstractTestCase<DockerConnector> {

    CreateContainerResponse container = null;

    public PauseContainerIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() throws InterruptedException {
        getConnector().pullImage(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, null, null);
        container = getConnector().runContainer(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, TestsConstants.PAUSE_CONTAINER, TestsConstants.COMMAND);
    }

    @After
    public void tearDown() {
        getConnector().unpauseContainer(TestsConstants.PAUSE_CONTAINER);
        getConnector().stopContainer(TestsConstants.PAUSE_CONTAINER, 0);
        getConnector().deleteContainer(TestsConstants.PAUSE_CONTAINER, true, true);
    }

    @Test
    public void verifyPauseContainer() {
        assertNotNull(container.getId());
        getConnector().pauseContainer(container.getId());
        InspectContainerResponse inspect = getConnector().inspectContainer(container.getId(), false);
        assertTrue(inspect.getState().getPaused());
    }

}