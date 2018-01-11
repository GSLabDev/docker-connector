/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class WaitAContainerIT extends AbstractTestCase<DockerConnector> {
    CreateContainerResponse container = null;

    public WaitAContainerIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        List<String> command = new ArrayList<String>();
        command.add("true");
        container = getConnector().runContainer(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, TestsConstants.WAIT_A_CONTAINER, command);
    }

    @After
    public void tearDown() {
        try {
            getConnector().killContainer(container.getId(), TestsConstants.KILL_CONTAINER_SIGNAL);
        } catch (Exception e) {
        } finally {
            getConnector().deleteContainer(container.getId(), true, true);
        }
    }

    @Test
    public void verify() {
        assertNotNull(container.getId());
        getConnector().waitAContainer(container.getId());

        InspectContainerResponse inspectContainerResponse = getConnector().inspectContainer(container.getId(), false);

        assertFalse(inspectContainerResponse.getState().getRunning());
        assertTrue(inspectContainerResponse.getState().getExitCode() == 0);
    }

}