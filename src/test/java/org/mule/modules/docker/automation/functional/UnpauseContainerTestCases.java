/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class UnpauseContainerTestCases extends AbstractTestCase<DockerConnector> {

    CreateContainerResponse container = null;

    public UnpauseContainerTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        List<String> command = new ArrayList<String>();
        command.add("sleep");
        command.add("999");
        container = getConnector().runContainer("busybox", "latest", "created-test-unpause", command);
    }

    @After
    public void tearDown() {
        try {
            getConnector().stopContainer(container.getId());
            getConnector().deleteContainer(container.getId(), true);
        } catch (Exception e) {
            getConnector().killContainer(container.getId());
        }
    }

    @Test
    public void verify() {
        assertNotNull(container.getId());
        getConnector().pauseContainer(container.getId());

        getConnector().unpauseContainer(container.getId());
        InspectContainerResponse inspect = getConnector().inspectContainer(container.getId());
        assertFalse(inspect.getState().getPaused());
        assertTrue(inspect.getState().getRunning());
    }

}