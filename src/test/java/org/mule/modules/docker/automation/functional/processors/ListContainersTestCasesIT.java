/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
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
import com.github.dockerjava.api.model.Info;

public class ListContainersTestCasesIT extends AbstractTestCase<DockerConnector> {

    public static final String CONTAINERS_NAME = "created-in-list-container";
    CreateContainerResponse container = null;

    public ListContainersTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().pullImage(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, null, null);
            getConnector().runContainer(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, CONTAINERS_NAME, TestsConstants.COMMAND);
        } catch (Exception e) {
        }
    }

    @After
    public void tearDown() {
        try {
            if (getConnector().inspectContainer(CONTAINERS_NAME, false).getState().getRunning()) {
                getConnector().killContainer(CONTAINERS_NAME, TestsConstants.KILL_CONTAINER_SIGNAL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getConnector().deleteContainer(CONTAINERS_NAME, true, true);
        }

    }

    @Test
    public void verifyWithDefaultValues() {
        java.util.List<com.github.dockerjava.api.model.Container> containers = getConnector().listContainers(true, null, 0, false, null, null);
        assertNotNull(containers);
        Info info = getConnector().dockerInfo();
        assertTrue(containers.size() == info.getContainers());
    }

    @Test
    public void verifyWithAll() {
        java.util.List<com.github.dockerjava.api.model.Container> containers = getConnector().listContainers(TestsConstants.LIST_CONTAINERS_SHOW_ALL,
                TestsConstants.LIST_CONTAINERS_SHOW_BEFORE_CONTAINER, TestsConstants.LIST_CONTAINERS_LIMIT, TestsConstants.LIST_CONTAINERS_SHOW_SIZE,
                TestsConstants.LIST_CONTAINERS_WITH_STATUS, TestsConstants.LIST_CONTAINERS_WITH_LABELS);
        assertTrue(containers.size() <= 0);
        Info info = getConnector().dockerInfo();
        assertTrue(containers.size() <= info.getContainers());
    }

}