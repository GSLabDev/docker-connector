/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class StopContainerTestCasesIT extends AbstractTestCase<DockerConnector> {

    CreateContainerResponse container = null;

    public StopContainerTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        container = getConnector().runContainer(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, TestsConstants.START_CONTAINER, TestsConstants.COMMAND);
    }

    @After
    public void tearDown() {
        try {
            getConnector().deleteContainer(TestsConstants.START_CONTAINER, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void verifyWithoutTimeout() {
        assertNotNull(container.getId());
        getConnector().stopContainer(container.getId(), 0);

        InspectContainerResponse inspectContainerResponse = getConnector().inspectContainer(container.getId(), false);

        assertFalse(inspectContainerResponse.getState().getRunning());
    }

    @Test
    public void verifyWithTimeout() {
        assertNotNull(container.getId());
        getConnector().stopContainer(container.getId(), TestsConstants.STOP_CONTAINER_TIMEOUT);
        InspectContainerResponse inspectContainerResponse = getConnector().inspectContainer(container.getId(), false);
        assertFalse(inspectContainerResponse.getState().getRunning());
    }

}