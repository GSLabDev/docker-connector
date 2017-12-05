/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class GetContainerLogsTestCases extends AbstractTestCase<DockerConnector> {

    String containerName = "created-test-log", imageName = "busybox", imageTag = "latest";
    boolean showSize = false, removeVolumes = false, showTimeStamp = true, standardOut = true, standardError = true,
            followStream = false;
    int pollingPeriod = 1000, showSince = 10, tail = 10;

    public GetContainerLogsTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setUp() throws Throwable {
        List<String> command = new ArrayList<String>();
        command.add("ping");
        command.add("127.0.0.1");

        getConnector().runContainer(imageName, imageTag, containerName, command);
        while (!getConnector().inspectContainer(containerName, showSize).getState().getRunning()) {
            Thread.sleep(100);
        }
    }

    @Test
    public void testSourceWithDefault() throws Throwable {
        Object[] signature = { null, containerName, showTimeStamp, standardOut, standardError, showSince, tail,
                followStream };

        getDispatcher().initializeSource("getContainerLogs", signature);
        Thread.sleep(pollingPeriod * 3);

        List<Object> events = getDispatcher().getSourceMessages("getContainerLogs");
        assertTrue(events.size() > 0 && events.toString().contains("PING"));
        assertTrue(events.toString().contains("127.0.0.1"));
    }

    @Test
    public void testSourceWithValues() throws Throwable {

        boolean showTimeStamp = false, standardOut = true, standardError = false, followStream = true;
        int showSince = 0, tail = 0;
        Object[] signature = { null, containerName, showTimeStamp, standardOut, standardError, showSince, tail,
                followStream };

        getDispatcher().initializeSource("getContainerLogs", signature);
        Thread.sleep(pollingPeriod * 3);

        List<Object> events = getDispatcher().getSourceMessages("getContainerLogs");
        assertTrue(events.size() > 0 && events.toString().contains("PING"));
        assertTrue(events.toString().contains("127.0.0.1"));
    }

    @After
    public void tearDown() throws Throwable {
        getDispatcher().shutDownSource("getContainerLogs");
        Thread.sleep(pollingPeriod * 2);
        getConnector().deleteContainer(containerName, true, removeVolumes);
    }
}