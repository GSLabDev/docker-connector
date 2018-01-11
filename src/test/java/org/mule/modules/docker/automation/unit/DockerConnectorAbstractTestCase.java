/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.unit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mule.api.callback.SourceCallback;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateVolumeCmd;
import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectVolumeCmd;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.command.KillContainerCmd;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.ListVolumesCmd;
import com.github.dockerjava.api.command.ListVolumesResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.api.command.RemoveVolumeCmd;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.github.dockerjava.core.command.WaitContainerResultCallback;

public class DockerConnectorAbstractTestCase {

    protected DockerClient mockDockerClient;
    protected InfoCmd mockInfoCmd;
    protected UnpauseContainerCmd mockUnpauseContainerCmd;
    protected StartContainerCmd mockStartContainerCmd;
    protected InspectContainerCmd mockInspectContainerCmd;
    protected StopContainerCmd mockStopContainerCmd;
    protected RestartContainerCmd mockRestartContainerCmd;
    protected KillContainerCmd mockKillContainerCmd;
    protected PauseContainerCmd mockPauseContainerCmd;
    protected WaitContainerCmd mockWaitContainerCmd;
    protected WaitContainerResultCallback mockWaitCallback;
    protected RemoveContainerCmd mockRemvoeContainerCmd;
    protected CreateContainerCmd mockCreateContainerCmd;
    protected ListContainersCmd mockListContainersCmd;
    protected SourceCallback mockSourceCallback;
    protected SourceCallBack<Frame> mockSourceCallbackFrame;
    protected SourceCallBack<Statistics> mockSourceCallBackStats;
    protected LogContainerCmd mockLogContainerCmd;
    protected StatsCmd mockStatsCmd;
    protected BuildImageCmd mockBuildImageCmd;
    protected InspectImageCmd mockInspectImageCmd;
    protected ListImagesCmd mockListImgCmd;
    protected PullImageCmd mockPullImgCmd;
    protected PushImageCmd mockPushImgCmd;
    protected RemoveImageCmd mockRemoveImgCmd;
    protected TagImageCmd mockTagImageCmd;
    protected CreateVolumeCmd mockCreateVolCmd;
    protected InspectVolumeCmd mockInspectVolCmd;
    protected ListVolumesCmd mockListVolCmd;
    protected RemoveVolumeCmd mockRemoveVolCmd;

    @Before
    public void setUp() throws Exception, Throwable, JsonParseException, JsonMappingException, FileNotFoundException, IOException {
        // Required in all test cases
        mockDockerClient = Mockito.mock(DockerClient.class);
        initializeCommands();
    }

    private void initializeCommands() throws Exception {
        // InfoCmd
        mockInfoCmd = Mockito.mock(InfoCmd.class);
        Mockito.when(mockDockerClient.infoCmd()).thenReturn(mockInfoCmd);
        Mockito.when(mockInfoCmd.exec()).thenReturn(TestDataBuilder.getDockerInfo());

        initializeContainerCommands();

        initializeImageCommands();

        initializeVolumeCommands();

    }

    private void initializeVolumeCommands() {
        // Inspect volume
        mockInspectVolCmd = Mockito.mock(InspectVolumeCmd.class);
        Mockito.when(mockDockerClient.inspectVolumeCmd(Mockito.anyString())).thenReturn(mockInspectVolCmd);
        Mockito.when(mockInspectVolCmd.withName(Mockito.anyString())).thenReturn(mockInspectVolCmd);
        InspectVolumeResponse inspectVolumeResponse = new InspectVolumeResponse();
        Mockito.when(mockInspectVolCmd.exec()).thenReturn(inspectVolumeResponse);

        // Create volume

        mockCreateVolCmd = Mockito.mock(CreateVolumeCmd.class);
        Mockito.when(mockDockerClient.createVolumeCmd()).thenReturn(mockCreateVolCmd);

        CreateVolumeResponse createVolumeResp = new CreateVolumeResponse();

        Mockito.when(mockCreateVolCmd.withDriver(Mockito.anyString())).thenReturn(mockCreateVolCmd);
        Mockito.when(mockCreateVolCmd.withDriverOpts(ArgumentMatchers.<Map<String, String>>any())).thenReturn(mockCreateVolCmd);
        Mockito.when(mockCreateVolCmd.withName(Mockito.anyString())).thenReturn(mockCreateVolCmd);
        Mockito.when(mockCreateVolCmd.exec()).thenReturn(createVolumeResp);

        // List Volume

        mockListVolCmd = Mockito.mock(ListVolumesCmd.class);
        Mockito.when(mockDockerClient.listVolumesCmd()).thenReturn(mockListVolCmd);
        Mockito.when(mockListVolCmd.withDanglingFilter(Mockito.anyBoolean())).thenReturn(mockListVolCmd);
        ListVolumesResponse listVolumeResp = new ListVolumesResponse();
        Mockito.when(mockListVolCmd.exec()).thenReturn(listVolumeResp);

        // Remove Volume

        mockRemoveVolCmd = Mockito.mock(RemoveVolumeCmd.class);
        Mockito.when(mockDockerClient.removeVolumeCmd(Mockito.anyString())).thenReturn(mockRemoveVolCmd);
        Mockito.when(mockRemoveVolCmd.withName(Mockito.anyString())).thenReturn(mockRemoveVolCmd);
        Mockito.doNothing().doThrow(new NotFoundException("Volume not found !")).when(mockRemoveVolCmd).exec();
    }

    private void initializeImageCommands() throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
        // Build Image
        mockBuildImageCmd = Mockito.mock(BuildImageCmd.class);
        Mockito.when(mockDockerClient.buildImageCmd()).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withDockerfile(ArgumentMatchers.any(File.class))).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withTags(ArgumentMatchers.<HashSet<String>>any())).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withForcerm(Mockito.anyBoolean())).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withNoCache(Mockito.anyBoolean())).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withPull(Mockito.anyBoolean())).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withRemove(Mockito.anyBoolean())).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withBuildArg(Mockito.anyString(), Mockito.anyString())).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withCacheFrom(ArgumentMatchers.<HashSet<String>>any())).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withCpusetcpus(Mockito.anyString())).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withCpushares(Mockito.anyString())).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withLabels(ArgumentMatchers.<Map<String, String>>any())).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withRemote(Mockito.any(URI.class))).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withMemory(Mockito.anyLong())).thenReturn(mockBuildImageCmd);
        Mockito.when(mockBuildImageCmd.withMemswap(Mockito.anyLong())).thenReturn(mockBuildImageCmd);

        BuildImageResultCallback buildImageResp = Mockito.mock(BuildImageResultCallback.class);

        Mockito.when(mockBuildImageCmd.exec(Mockito.any(BuildImageResultCallback.class))).thenReturn(buildImageResp);
        Mockito.when(buildImageResp.awaitImageId()).thenReturn(Mockito.anyString());
        Mockito.when(mockBuildImageCmd.exec(buildImageResp)).thenReturn(buildImageResp);

        // Inspect Image

        mockInspectImageCmd = Mockito.mock(InspectImageCmd.class);
        Mockito.when(mockDockerClient.inspectImageCmd(Mockito.anyString())).thenReturn(mockInspectImageCmd);
        Mockito.when(mockInspectImageCmd.withImageId(Mockito.anyString())).thenReturn(mockInspectImageCmd);
        Mockito.when(mockInspectImageCmd.exec()).thenReturn(TestDataBuilder.getInspectImageResponse());

        // List Image

        mockListImgCmd = Mockito.mock(ListImagesCmd.class);
        Mockito.when(mockDockerClient.listImagesCmd()).thenReturn(mockListImgCmd);
        Mockito.when(mockListImgCmd.withDanglingFilter(Mockito.anyBoolean())).thenReturn(mockListImgCmd);
        Mockito.when(mockListImgCmd.withImageNameFilter(Mockito.anyString())).thenReturn(mockListImgCmd);
        Mockito.when(mockListImgCmd.withLabelFilter(Mockito.anyString())).thenReturn(mockListImgCmd);
        Mockito.when(mockListImgCmd.withShowAll(Mockito.anyBoolean())).thenReturn(mockListImgCmd);

        Mockito.when(mockListImgCmd.exec()).thenReturn(TestDataBuilder.getListImageResponse());

        // Pull Image

        mockPullImgCmd = Mockito.mock(PullImageCmd.class);
        Mockito.when(mockDockerClient.pullImageCmd(Mockito.anyString())).thenReturn(mockPullImgCmd);
        Mockito.when(mockPullImgCmd.withRegistry(Mockito.anyString())).thenReturn(mockPullImgCmd);
        Mockito.when(mockPullImgCmd.withRepository(Mockito.anyString())).thenReturn(mockPullImgCmd);
        Mockito.when(mockPullImgCmd.withTag(Mockito.anyString())).thenReturn(mockPullImgCmd);
        Mockito.when(mockPullImgCmd.withAuthConfig(ArgumentMatchers.<AuthConfig>any())).thenReturn(mockPullImgCmd);
        PullImageResultCallback pullImageCallback = Mockito.mock(PullImageResultCallback.class);
        Mockito.when(mockPullImgCmd.exec(ArgumentMatchers.<PullImageResultCallback>any())).thenReturn(pullImageCallback);
        Mockito.doNothing().when(pullImageCallback).awaitSuccess();

        // Push Image

        mockPushImgCmd = Mockito.mock(PushImageCmd.class);
        Mockito.when(mockDockerClient.pushImageCmd(Mockito.anyString())).thenReturn(mockPushImgCmd);
        Mockito.when(mockPushImgCmd.withName(Mockito.anyString())).thenReturn(mockPushImgCmd);
        Mockito.when(mockPushImgCmd.withAuthConfig(ArgumentMatchers.<AuthConfig>any())).thenReturn(mockPushImgCmd);
        Mockito.when(mockPushImgCmd.withTag(Mockito.anyString())).thenReturn(mockPushImgCmd);
        PushImageResultCallback pushImageCallback = Mockito.mock(PushImageResultCallback.class);
        Mockito.when(mockPushImgCmd.exec(Mockito.any(PushImageResultCallback.class))).thenReturn(pushImageCallback);
        Mockito.doNothing().doThrow(new NotFoundException("Image not found !")).when(pushImageCallback).awaitSuccess();
        Mockito.when(mockPushImgCmd.exec(pushImageCallback)).thenReturn(pushImageCallback);
        Mockito.when(mockPushImgCmd.exec(pushImageCallback)).thenReturn(pushImageCallback);

        // Remove Image

        mockRemoveImgCmd = Mockito.mock(RemoveImageCmd.class);
        Mockito.when(mockDockerClient.removeImageCmd(Mockito.anyString())).thenReturn(mockRemoveImgCmd);
        Mockito.when(mockRemoveImgCmd.withImageId(Mockito.anyString())).thenReturn(mockRemoveImgCmd);
        Mockito.when(mockRemoveImgCmd.withForce(Mockito.anyBoolean())).thenReturn(mockRemoveImgCmd);
        Mockito.when(mockRemoveImgCmd.withNoPrune(Mockito.anyBoolean())).thenReturn(mockRemoveImgCmd);
        Mockito.doNothing().doThrow(new NotFoundException("Image not found !")).when(mockRemoveImgCmd).exec();

        // Tag Image
        mockTagImageCmd = Mockito.mock(TagImageCmd.class);
        Mockito.when(mockDockerClient.tagImageCmd(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(mockTagImageCmd);
        Mockito.when(mockTagImageCmd.withTag(Mockito.anyString())).thenReturn(mockTagImageCmd);
        Mockito.when(mockTagImageCmd.withForce(Mockito.anyBoolean())).thenReturn(mockTagImageCmd);
        Mockito.when(mockTagImageCmd.withImageId(Mockito.anyString())).thenReturn(mockTagImageCmd);
        Mockito.when(mockTagImageCmd.withRepository(Mockito.anyString())).thenReturn(mockTagImageCmd);
        Mockito.when(mockTagImageCmd.withForce()).thenReturn(mockTagImageCmd);

        Void arg0 = null;
        Mockito.when(mockTagImageCmd.exec()).thenReturn(arg0);
    }

    private void initializeContainerCommands() throws Exception {
        // Unpause container
        mockUnpauseContainerCmd = Mockito.mock(UnpauseContainerCmd.class);

        Mockito.when(mockDockerClient.unpauseContainerCmd(Mockito.anyString())).thenReturn(mockUnpauseContainerCmd);
        Mockito.when(mockUnpauseContainerCmd.withContainerId(Mockito.anyString())).thenReturn(mockUnpauseContainerCmd);
        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(mockUnpauseContainerCmd).exec();

        // Start container
        mockStartContainerCmd = Mockito.mock(StartContainerCmd.class);
        Mockito.when(mockDockerClient.startContainerCmd(Mockito.anyString())).thenReturn(mockStartContainerCmd);
        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(mockStartContainerCmd).exec();

        // Inspect container
        mockInspectContainerCmd = Mockito.mock(InspectContainerCmd.class);
        Mockito.when(mockDockerClient.inspectContainerCmd(Mockito.anyString())).thenReturn(mockInspectContainerCmd);
        Mockito.when(mockInspectContainerCmd.withSize(Mockito.anyBoolean())).thenReturn(mockInspectContainerCmd);
        Mockito.when(mockInspectContainerCmd.exec()).thenReturn(TestDataBuilder.getInspectContainerResponse());

        // Stop container
        mockStopContainerCmd = Mockito.mock(StopContainerCmd.class);
        Mockito.when(mockDockerClient.stopContainerCmd(Mockito.anyString())).thenReturn(mockStopContainerCmd);
        Mockito.when(mockStopContainerCmd.withTimeout(Mockito.anyInt())).thenReturn(mockStopContainerCmd);
        Mockito.when(mockStopContainerCmd.withContainerId(Mockito.anyString())).thenReturn(mockStopContainerCmd);
        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(mockStopContainerCmd).exec();

        // Restart container
        mockRestartContainerCmd = Mockito.mock(RestartContainerCmd.class);
        Mockito.when(mockDockerClient.restartContainerCmd(Mockito.anyString())).thenReturn(mockRestartContainerCmd);
        Mockito.when(mockRestartContainerCmd.withContainerId(Mockito.anyString())).thenReturn(mockRestartContainerCmd);
        Mockito.when(mockRestartContainerCmd.withtTimeout(Mockito.anyInt())).thenReturn(mockRestartContainerCmd);
        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(mockRestartContainerCmd).exec();

        // Kill container
        mockKillContainerCmd = Mockito.mock(KillContainerCmd.class);
        Mockito.when(mockDockerClient.killContainerCmd(Mockito.anyString())).thenReturn(mockKillContainerCmd);
        Mockito.when(mockKillContainerCmd.withSignal(Mockito.anyString())).thenReturn(mockKillContainerCmd);
        Mockito.when(mockKillContainerCmd.withContainerId(Mockito.anyString())).thenReturn(mockKillContainerCmd);
        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(mockKillContainerCmd).exec();

        // Pause container
        mockPauseContainerCmd = Mockito.mock(PauseContainerCmd.class);
        Mockito.when(mockDockerClient.pauseContainerCmd(Mockito.anyString())).thenReturn(mockPauseContainerCmd);
        Mockito.when(mockPauseContainerCmd.withContainerId(Mockito.anyString())).thenReturn(mockPauseContainerCmd);
        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(mockPauseContainerCmd).exec();

        // Wait a container
        mockWaitContainerCmd = Mockito.mock(WaitContainerCmd.class);
        mockWaitCallback = Mockito.mock(WaitContainerResultCallback.class);
        Mockito.when(mockDockerClient.waitContainerCmd(Mockito.anyString())).thenReturn(mockWaitContainerCmd);
        Mockito.when(mockWaitContainerCmd.withContainerId(Mockito.anyString())).thenReturn(mockWaitContainerCmd);
        Mockito.when(mockWaitContainerCmd.exec(Mockito.any(WaitContainerResultCallback.class))).thenReturn(mockWaitCallback);
        Mockito.when(mockWaitCallback.awaitStatusCode()).thenReturn(0);

        // Delete container
        mockRemvoeContainerCmd = Mockito.mock(RemoveContainerCmd.class);
        Mockito.when(mockDockerClient.removeContainerCmd(Mockito.anyString())).thenReturn(mockRemvoeContainerCmd);
        Mockito.when(mockRemvoeContainerCmd.withContainerId(Mockito.anyString())).thenReturn(mockRemvoeContainerCmd);
        Mockito.when(mockRemvoeContainerCmd.withRemoveVolumes(Mockito.anyBoolean())).thenReturn(mockRemvoeContainerCmd);
        Mockito.when(mockRemvoeContainerCmd.withForce(Mockito.anyBoolean())).thenReturn(mockRemvoeContainerCmd);
        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(mockRemvoeContainerCmd).exec();

        // Create container
        mockCreateContainerCmd = Mockito.mock(CreateContainerCmd.class);
        Mockito.when(mockDockerClient.createContainerCmd(Mockito.anyString())).thenReturn(mockCreateContainerCmd);
        Mockito.when(mockCreateContainerCmd.withName(Mockito.anyString())).thenReturn(mockCreateContainerCmd);
        Mockito.when(mockCreateContainerCmd.withImage(Mockito.anyString())).thenReturn(mockCreateContainerCmd);
        Mockito.when(mockCreateContainerCmd.withCmd(ArgumentMatchers.<List<String>>any())).thenReturn(mockCreateContainerCmd);
        Mockito.when(mockCreateContainerCmd.exec()).thenReturn(TestDataBuilder.getCreateContainerResponse());

        // List container
        mockListContainersCmd = Mockito.mock(ListContainersCmd.class);
        Mockito.when(mockDockerClient.listContainersCmd()).thenReturn(mockListContainersCmd);
        Mockito.when(mockListContainersCmd.withShowAll(Mockito.anyBoolean())).thenReturn(mockListContainersCmd);
        Mockito.when(mockListContainersCmd.withBefore(Mockito.anyString())).thenReturn(mockListContainersCmd);
        Mockito.when(mockListContainersCmd.withLimit(Mockito.anyInt())).thenReturn(mockListContainersCmd);
        Mockito.when(mockListContainersCmd.withShowSize(Mockito.anyBoolean())).thenReturn(mockListContainersCmd);
        Mockito.when(mockListContainersCmd.withStatusFilter(Mockito.anyString())).thenReturn(mockListContainersCmd);
        Mockito.when(mockListContainersCmd.withLabelFilter(Mockito.anyString())).thenReturn(mockListContainersCmd);
        Mockito.when(mockListContainersCmd.exec()).thenReturn(TestDataBuilder.getListContainerResponse());

        // Log container
        Object response = new String("Source call back response");
        mockSourceCallback = Mockito.mock(SourceCallback.class);
        mockSourceCallbackFrame = new SourceCallBack<Frame>(mockSourceCallback);
        mockLogContainerCmd = Mockito.mock(LogContainerCmd.class);
        Mockito.when(mockDockerClient.logContainerCmd(Mockito.anyString())).thenReturn(mockLogContainerCmd);
        Mockito.when(mockLogContainerCmd.withContainerId(Mockito.anyString())).thenReturn(mockLogContainerCmd);
        Mockito.when(mockLogContainerCmd.withTimestamps(Mockito.anyBoolean())).thenReturn(mockLogContainerCmd);
        Mockito.when(mockLogContainerCmd.withStdOut(Mockito.anyBoolean())).thenReturn(mockLogContainerCmd);
        Mockito.when(mockLogContainerCmd.withStdErr(Mockito.anyBoolean())).thenReturn(mockLogContainerCmd);
        Mockito.when(mockLogContainerCmd.withSince(Mockito.anyInt())).thenReturn(mockLogContainerCmd);
        Mockito.when(mockLogContainerCmd.withTail(Mockito.anyInt())).thenReturn(mockLogContainerCmd);
        Mockito.when(mockLogContainerCmd.withFollowStream(Mockito.anyBoolean())).thenReturn(mockLogContainerCmd);
        Mockito.when(mockSourceCallback.process()).thenReturn(response);
        Mockito.when(mockLogContainerCmd.exec(ArgumentMatchers.<SourceCallBack<Frame>>any())).thenReturn(mockSourceCallbackFrame);

        // get statistics of container
        mockStatsCmd = Mockito.mock(StatsCmd.class);
        Mockito.when(mockDockerClient.statsCmd(Mockito.anyString())).thenReturn(mockStatsCmd);
        Mockito.when(mockStatsCmd.withContainerId(Mockito.anyString())).thenReturn(mockStatsCmd);
        mockSourceCallBackStats = new SourceCallBack<Statistics>(mockSourceCallback);
        Mockito.when(mockStatsCmd.exec(ArgumentMatchers.<SourceCallBack<Statistics>>any())).thenReturn(mockSourceCallBackStats);
    }

    private class SourceCallBack<T> extends ResultCallbackTemplate<SourceCallBack<T>, T> {

        private final SourceCallback callback;

        SourceCallBack(SourceCallback sourceCallback) {
            this.callback = sourceCallback;
        }

        @Override
        public void onNext(T t) {

            try {
                this.callback.process(t);
            } catch (Exception e) {
            }

        }
    }
}