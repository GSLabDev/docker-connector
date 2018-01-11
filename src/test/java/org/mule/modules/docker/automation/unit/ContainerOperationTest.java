/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mule.modules.docker.DockerContainerOperations;
import org.mule.modules.docker.automation.util.TestsConstants;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.command.WaitContainerResultCallback;

public class ContainerOperationTest extends DockerConnectorAbstractTestCase {

    DockerContainerOperations dockerContainerOperations;

    @Before
    public void setUpContainerOperations() {
        dockerContainerOperations = new DockerContainerOperations(mockDockerClient);
    }

    @Test
    public void verifyStartContainer() {
        dockerContainerOperations.startContainerImpl(TestsConstants.START_CONTAINER);
        Mockito.verify(mockDockerClient).startContainerCmd(TestsConstants.START_CONTAINER);
        Mockito.verify(mockStartContainerCmd).exec();
    }

    @Test
    public void verifyWaitAContainer() {
        dockerContainerOperations.waitAContainerImpl(TestsConstants.WAIT_A_CONTAINER);
        Mockito.verify(mockDockerClient).waitContainerCmd(TestsConstants.WAIT_A_CONTAINER);
        Mockito.verify(mockWaitCallback).awaitStatusCode();
        assertTrue(0 == (int) mockWaitCallback.awaitStatusCode());
        Mockito.verify(mockWaitContainerCmd).exec(Mockito.any(WaitContainerResultCallback.class));
    }

    @Test
    public void verifyUnpauseContainer() {
        dockerContainerOperations.unpauseContainerImpl(TestsConstants.UNPAUSE_CONTAINER);
        Mockito.verify(mockDockerClient).unpauseContainerCmd(TestsConstants.UNPAUSE_CONTAINER);
        Mockito.verify(mockUnpauseContainerCmd).exec();
    }

    @Test
    public void verifyStopContainerWithoutTimeOut() {
        dockerContainerOperations.stopContainerImpl(TestsConstants.STOP_CONTAINER, 0);
        Mockito.verify(mockDockerClient).stopContainerCmd(TestsConstants.STOP_CONTAINER);
        Mockito.verify(mockStopContainerCmd).exec();
    }

    @Test
    public void verifyStopContainerWithTimeOut() {
        dockerContainerOperations.stopContainerImpl(TestsConstants.STOP_CONTAINER, TestsConstants.STOP_CONTAINER_TIMEOUT);
        Mockito.verify(mockStopContainerCmd).withTimeout(TestsConstants.STOP_CONTAINER_TIMEOUT);
        Mockito.verify(mockDockerClient).stopContainerCmd(TestsConstants.STOP_CONTAINER);
        Mockito.verify(mockStopContainerCmd).exec();
    }

    @Test
    public void verifyRunContainer() {
        CreateContainerResponse response = dockerContainerOperations.runContainerImpl(TestsConstants.IMAGE_NAME, TestsConstants.RUN_CONTAINER_IMAGE_TAG,
                TestsConstants.RUN_CONTAINER, TestsConstants.RUN_CONTAINER_COMMAND);
        Mockito.verify(mockDockerClient).createContainerCmd(TestsConstants.IMAGE_NAME + ":" + TestsConstants.RUN_CONTAINER_IMAGE_TAG);
        Mockito.verify(mockCreateContainerCmd).withName(TestsConstants.RUN_CONTAINER);
        Mockito.verify(mockCreateContainerCmd).withCmd(TestsConstants.RUN_CONTAINER_COMMAND);
        assertNotNull(response.getId());
        Mockito.verify(mockCreateContainerCmd).exec();
        Mockito.verify(mockStartContainerCmd).exec();
    }

    @Test
    public void verifyRestartWithTimeOut() {
        dockerContainerOperations.restartContainerImpl(TestsConstants.RESTART_CONTAINER, TestsConstants.RESTART_CONTAINER_TIMEOUT);
        Mockito.verify(mockDockerClient).restartContainerCmd(TestsConstants.RESTART_CONTAINER);
        Mockito.verify(mockRestartContainerCmd).withtTimeout(TestsConstants.RESTART_CONTAINER_TIMEOUT);
        Mockito.verify(mockRestartContainerCmd).exec();
    }

    @Test
    public void verifyPauseContainer() {
        dockerContainerOperations.pauseContainerImpl(TestsConstants.PAUSE_CONTAINER);
        Mockito.verify(mockDockerClient).pauseContainerCmd(TestsConstants.PAUSE_CONTAINER);
        Mockito.verify(mockPauseContainerCmd).exec();
    }

    @Test
    public void verifyListContainerWithDefaultValues() {
        java.util.List<Container> containers = dockerContainerOperations.listContainerImpl(false, null, 0, false, null, null);
        assertNotNull(containers);
        Mockito.verify(mockListContainersCmd, Mockito.never()).withBefore(Mockito.anyString());
        Mockito.verify(mockListContainersCmd, Mockito.never()).withStatusFilter(Mockito.anyString());
        Mockito.verify(mockListContainersCmd, Mockito.never()).withLabelFilter(Mockito.anyString());
        Mockito.verify(mockListContainersCmd).withShowAll(false);
        Mockito.verify(mockListContainersCmd).withShowSize(false);
    }

    @Test
    public void verifyListContainerWithParamsValues() {
        java.util.List<Container> containers = dockerContainerOperations.listContainerImpl(TestsConstants.LIST_CONTAINERS_SHOW_ALL,
                TestsConstants.LIST_CONTAINERS_SHOW_BEFORE_CONTAINER, TestsConstants.LIST_CONTAINERS_LIMIT, TestsConstants.LIST_CONTAINERS_SHOW_SIZE,
                TestsConstants.LIST_CONTAINERS_WITH_STATUS, TestsConstants.LIST_CONTAINERS_WITH_LABELS);
        assertNotNull(containers);
        Mockito.verify(mockListContainersCmd).withBefore(TestsConstants.LIST_CONTAINERS_SHOW_BEFORE_CONTAINER);
        Mockito.verify(mockListContainersCmd).withStatusFilter(TestsConstants.LIST_CONTAINERS_WITH_STATUS);
        Mockito.verify(mockListContainersCmd).withLabelFilter(TestsConstants.LIST_CONTAINERS_WITH_LABELS);
        Mockito.verify(mockListContainersCmd).withShowAll(TestsConstants.LIST_CONTAINERS_SHOW_ALL);
        Mockito.verify(mockListContainersCmd).withShowSize(TestsConstants.LIST_CONTAINERS_SHOW_SIZE);
    }

    @Test
    public void verifyInspectContainer() {
        InspectContainerResponse inspectContainerResponse = dockerContainerOperations.inspectContainerImpl(TestsConstants.INSPECT_CONTAINER, true);
        assertNotNull(inspectContainerResponse.getId());
        Mockito.verify(mockDockerClient).inspectContainerCmd(TestsConstants.INSPECT_CONTAINER);
        Mockito.verify(mockInspectContainerCmd).exec();
    }

    @Test
    public void testGetContainerStatistics() {
        dockerContainerOperations.getContainerStatsImpl(mockSourceCallback, TestsConstants.GET_CONTAINER_STATS);
        Mockito.verify(mockDockerClient).statsCmd(TestsConstants.GET_CONTAINER_STATS);
    }

    @Test
    public void testGetContainerLogs() {
        dockerContainerOperations.getContainerLogsImpl(mockSourceCallback, TestsConstants.GET_CONTAINER_LOG, TestsConstants.GET_CONTAINER_LOG_SHOW_TIME_STAMP,
                TestsConstants.GET_CONTAINER_LOG_STANDARD_OUT, TestsConstants.GET_CONTAINER_LOG_STANDARD_ERROR, TestsConstants.GET_CONTAINER_LOG_SHOW_SINCE,
                TestsConstants.GET_CONTAINER_LOG_TAIL, TestsConstants.GET_CONTAINER_LOG_FOLLOW_LOGS);
        Mockito.verify(mockLogContainerCmd).withTail(TestsConstants.GET_CONTAINER_LOG_TAIL);
        Mockito.verify(mockLogContainerCmd).withStdOut(TestsConstants.GET_CONTAINER_LOG_STANDARD_OUT);
        Mockito.verify(mockLogContainerCmd).withStdErr(TestsConstants.GET_CONTAINER_LOG_STANDARD_ERROR);
    }

    @Test
    public void testGetContainerLogsWithoutTail() {
        dockerContainerOperations.getContainerLogsImpl(mockSourceCallback, TestsConstants.GET_CONTAINER_LOG, TestsConstants.GET_CONTAINER_LOG_SHOW_TIME_STAMP,
                TestsConstants.GET_CONTAINER_LOG_STANDARD_OUT, TestsConstants.GET_CONTAINER_LOG_STANDARD_ERROR, TestsConstants.GET_CONTAINER_LOG_SHOW_SINCE, 0,
                TestsConstants.GET_CONTAINER_LOG_FOLLOW_LOGS);
        Mockito.verify(mockLogContainerCmd, Mockito.times(0)).withTail(TestsConstants.GET_CONTAINER_LOG_TAIL);
    }

    @Test
    public void verifyDeleteContainer() {
        dockerContainerOperations.deleteContainerImpl(TestsConstants.DELETE_CONTAINER, TestsConstants.DELETE_CONTAINER_FORCE_REMOVE, TestsConstants.DELETE_CONTAINER_REMOVE_VOLUME);
        Mockito.verify(mockDockerClient).removeContainerCmd(TestsConstants.DELETE_CONTAINER);
        Mockito.verify(mockRemvoeContainerCmd).withForce(TestsConstants.DELETE_CONTAINER_FORCE_REMOVE);
        Mockito.verify(mockRemvoeContainerCmd).withRemoveVolumes(TestsConstants.DELETE_CONTAINER_FORCE_REMOVE);
        Mockito.verify(mockRemvoeContainerCmd).exec();
    }

    @Test
    public void verifyCreateContainerWithoutJsonFile() {
        CreateContainerResponse createContainerResponse = dockerContainerOperations.createContainerImpl(TestsConstants.CREATE_CONTAINERS_IMAGE,
                TestsConstants.CREATE_CONTAINERS_IMAGE_TAG, TestsConstants.CREATE_CONTAINER, null);
        Mockito.verify(mockCreateContainerCmd).withName(TestsConstants.CREATE_CONTAINER);
        Mockito.verify(mockCreateContainerCmd).withImage(TestsConstants.CREATE_CONTAINERS_IMAGE + ":" + TestsConstants.CREATE_CONTAINERS_IMAGE_TAG);
        assertNotNull(createContainerResponse.getId());
    }

    @Test
    public void verifyWithJsonFile() {
        CreateContainerResponse createContainerResponse = dockerContainerOperations.createContainerImpl(TestsConstants.CREATE_CONTAINERS_IMAGE,
                TestsConstants.CREATE_CONTAINERS_IMAGE_TAG, TestsConstants.CREATE_CONTAINER, TestsConstants.CREATE_CONTAINERS_JSON_FILE_PATH);
        Mockito.verify(mockCreateContainerCmd).withName(TestsConstants.CREATE_CONTAINER);
        Mockito.verify(mockCreateContainerCmd).withAttachStdin(Mockito.anyBoolean());
        Mockito.verify(mockCreateContainerCmd).withAttachStdout(Mockito.anyBoolean());
        Mockito.verify(mockCreateContainerCmd).withAttachStderr(Mockito.anyBoolean());
        assertNotNull(createContainerResponse.getId());
    }

    @Test
    public void verifyWithoutTimeOut() {
        dockerContainerOperations.killContainerImpl(TestsConstants.KILL_CONTAINER, TestsConstants.KILL_CONTAINER_SIGNAL);
        Mockito.verify(mockDockerClient).killContainerCmd(TestsConstants.KILL_CONTAINER);
        Mockito.verify(mockKillContainerCmd).withSignal(TestsConstants.KILL_CONTAINER_SIGNAL);
        Mockito.verify(mockKillContainerCmd).exec();
    }
}
