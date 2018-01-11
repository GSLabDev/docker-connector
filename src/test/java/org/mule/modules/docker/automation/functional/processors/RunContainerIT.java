/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class RunContainerIT extends AbstractTestCase<DockerConnector> {

    com.github.dockerjava.api.command.CreateContainerResponse expected = null;

    public RunContainerIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {

        try {
            getConnector().pullImage(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, null, null);
        } catch (Exception e) {
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().killContainer(TestsConstants.RESTART_CONTAINER, TestsConstants.KILL_CONTAINER_SIGNAL);
        } catch (Exception e) {
        } finally {
            getConnector().deleteContainer(TestsConstants.RESTART_CONTAINER, true, true);
        }

    }

    @Test
    public void verifyRunContainer() {
        CreateContainerResponse container = getConnector().runContainer(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, TestsConstants.RESTART_CONTAINER,
                TestsConstants.COMMAND);
        InspectContainerResponse inspect = getConnector().inspectContainer(container.getId(), false);
        assertTrue(inspect.getState().getRunning());
    }

}