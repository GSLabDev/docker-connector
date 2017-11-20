/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class GetContainerStatsTestCases extends AbstractTestCase<DockerConnector> {

    java.lang.String containerName = "created-test-get-stats", imageName = "busybox", imageTag = "latest";
    int pollingPeriod = 1000;

    public GetContainerStatsTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setUp() throws Throwable {
        Object[] signature = { null, containerName };
        List<String> command = new ArrayList<String>();
        command.add("sleep");
        command.add("999999");
        try {
            getConnector().pullImage(imageName, imageTag);
            getConnector().runContainer(imageName, imageTag, containerName, command);
        } catch (Exception e) {
        }

        while (!getConnector().inspectContainer(containerName).getState().getRunning()) {
            Thread.sleep(100);
        }
        getDispatcher().initializeSource("getContainerStatistics", signature);
        Thread.sleep(pollingPeriod * 3);
    }

    @Test
    public void testSource() {
        List<Object> events = getDispatcher().getSourceMessages("getContainerStatistics");
        assertNotNull(events);
        assertTrue(events.size() > 0);
        assertTrue(events.toString().contains("read"));

    }

    @After
    public void tearDown() throws Throwable {
        getDispatcher().shutDownSource("getContainerStatistics");
        try {
            if (getConnector().inspectContainer(containerName).getState().getRunning()) {
                getConnector().killContainer(containerName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getConnector().deleteContainer(containerName, true);
        }
    }
}