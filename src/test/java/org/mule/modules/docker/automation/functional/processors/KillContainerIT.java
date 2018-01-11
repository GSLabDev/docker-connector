/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class KillContainerTestCasesIT extends AbstractTestCase<DockerConnector> {

    CreateContainerResponse createContainerResponse = null;

    public KillContainerTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        createContainerResponse = getConnector().runContainer(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, TestsConstants.KILL_CONTAINER, TestsConstants.COMMAND);
    }

    @After
    public void tearDown() {
        try {
            getConnector().deleteContainer(TestsConstants.KILL_CONTAINER, true, true);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Test
    public void verify() {
        getConnector().killContainer(TestsConstants.KILL_CONTAINER, TestsConstants.KILL_CONTAINER_SIGNAL);
        InspectContainerResponse inspectContainerResponse = getConnector().inspectContainer(createContainerResponse.getId(), false);
        assertFalse(inspectContainerResponse.getState().getRunning());
        assertNotSame(inspectContainerResponse.getState().getExitCode(), 0);
    }

}