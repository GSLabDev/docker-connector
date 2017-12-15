/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.sources;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class GetContainerStatsTestCasesIT extends AbstractTestCase<DockerConnector> {

    int pollingPeriod = 1000;

    public GetContainerStatsTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setUp() throws Throwable {
        Object[] signature = {
            null,
            TestsConstants.GET_CONTAINER_STATS
        };
        try {
            getConnector().pullImage(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, null, null);
            getConnector().runContainer(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, TestsConstants.GET_CONTAINER_STATS, TestsConstants.COMMAND);
        } catch (Exception e) {
        }

        while (!getConnector().inspectContainer(TestsConstants.GET_CONTAINER_STATS, false).getState().getRunning()) {
            Thread.sleep(100);
        }
        getDispatcher().initializeSource("getContainerStatistics", signature);
        // Wait till dispatcher collect statistics
        Thread.sleep(pollingPeriod * 3);
    }

    @Test
    public void testGetContainerStats() {
        List<Object> events = getDispatcher().getSourceMessages("getContainerStatistics");
        assertNotNull(events);
        assertTrue(events.size() > 0);
        assertTrue(events.toString().contains("read"));

    }

    @After
    public void tearDown() throws Throwable {
        getDispatcher().shutDownSource("getContainerStatistics");
        try {
            if (getConnector().inspectContainer(TestsConstants.GET_CONTAINER_STATS, false).getState().getRunning()) {
                getConnector().killContainer(TestsConstants.GET_CONTAINER_STATS, "SIGKILL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getConnector().deleteContainer(TestsConstants.GET_CONTAINER_STATS, true, true);
        }
    }
}