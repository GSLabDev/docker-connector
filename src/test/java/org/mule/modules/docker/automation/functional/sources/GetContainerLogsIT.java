/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.sources;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;

public class GetContainerLogsIT extends AbstractTestCase<DockerConnector> {

    public GetContainerLogsIT() {
        super(DockerConnector.class);
    }

    int pollingPeriod = 1000;
    public static final List<String> GET_CONTAINER_LOG_COMMAND = Collections.unmodifiableList(Arrays.asList(new String[] {
        "ping",
        "127.0.0.1"
    }));

    Random random = new Random();

    @Before
    public void setUp() throws Throwable {
        getConnector().pullImage(TestsConstants.GET_CONTAINER_LOG_IMAGE, TestsConstants.GET_CONTAINER_LOG_IMAGE_TAG, null, null);
    }

    @Test
    public void testSourceWithTailFollow() throws Throwable {
        CreateContainerResponse createContainerRes = null;
        try {
            createContainerRes = getConnector().runContainer(TestsConstants.GET_CONTAINER_LOG_IMAGE, TestsConstants.GET_CONTAINER_LOG_IMAGE_TAG,
                    TestsConstants.GET_CONTAINER_LOG + random.nextInt(50), GET_CONTAINER_LOG_COMMAND);
            while (!getConnector().inspectContainer(createContainerRes.getId(), false).getState().getRunning()) {
                Thread.sleep(100);
            }
            Object[] signature = {
                null,
                createContainerRes.getId(),
                TestsConstants.GET_CONTAINER_LOG_SHOW_TIME_STAMP,
                TestsConstants.GET_CONTAINER_LOG_STANDARD_OUT,
                TestsConstants.GET_CONTAINER_LOG_STANDARD_ERROR,
                TestsConstants.GET_CONTAINER_LOG_SHOW_SINCE,
                TestsConstants.GET_CONTAINER_LOG_TAIL,
                TestsConstants.GET_CONTAINER_LOG_FOLLOW_LOGS
            };
            getDispatcher().initializeSource("getContainerLogs", signature);
            // Wait till dispatcher collect logs
            Thread.sleep(pollingPeriod * 3);
            List<Object> events = getDispatcher().getSourceMessages("getContainerLogs");
            assertTrue(events.toString().contains("127.0.0.1"));

        } finally {
            getDispatcher().shutDownSource("getContainerLogs");
            if (createContainerRes != null) {
                getConnector().deleteContainer(createContainerRes.getId(), true, true);
            }
        }
    }

    @Test
    public void testSourceWithoutTailFollow() throws Throwable {
        CreateContainerResponse createContainerRes = null;
        try {
            createContainerRes = getConnector().runContainer(TestsConstants.GET_CONTAINER_LOG_IMAGE, TestsConstants.GET_CONTAINER_LOG_IMAGE_TAG,
                    TestsConstants.GET_CONTAINER_LOG + random.nextInt(50), GET_CONTAINER_LOG_COMMAND);
            while (!getConnector().inspectContainer(createContainerRes.getId(), false).getState().getRunning()) {
                Thread.sleep(100);
            }
            Object[] signature = {
                null,
                createContainerRes.getId(),
                true,
                true,
                true,
                0,
                0,
                false
            };
            getDispatcher().initializeSource("getContainerLogs", signature);
            // Wait till dispatcher collect logs
            Thread.sleep(pollingPeriod * 3);
            List<Object> events = getDispatcher().getSourceMessages("getContainerLogs");
            assertTrue(events.toString().contains("127.0.0.1"));

        } finally {
            getDispatcher().shutDownSource("getContainerLogs");
            if (createContainerRes != null) {
                getConnector().deleteContainer(createContainerRes.getId(), true, true);
            }
        }
    }
}