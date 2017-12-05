/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class PauseContainerTestCases extends AbstractTestCase<DockerConnector> {

    CreateContainerResponse container = null;
    String containerName = "created-test-pause";
    int timeout = 10;
    boolean removeVolumes = false;
    boolean showSize = false;

    public PauseContainerTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        List<String> command = new ArrayList<String>();
        command.add("sleep");
        command.add("999");
        container = getConnector().runContainer("busybox", "latest", containerName, command);
    }

    @After
    public void tearDown() {
        getConnector().unpauseContainer(containerName);
        getConnector().stopContainer(containerName, timeout);
        getConnector().deleteContainer(containerName, true, removeVolumes);
    }

    @Test
    public void verify() {
        final Logger logger = LogManager.getLogger(DockerConnector.class.getName());
        logger.info("Created container: " + container);
        assertNotNull(container.getId());
        getConnector().pauseContainer(container.getId());
        InspectContainerResponse inspect = getConnector().inspectContainer(container.getId(), showSize);
        logger.info("State is : " + inspect.getState());
        assertTrue(inspect.getState().getPaused());
    }

}