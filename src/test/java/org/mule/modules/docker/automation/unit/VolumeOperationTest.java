package org.mule.modules.docker.automation.unit;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mule.modules.docker.DockerVolumeOperations;
import org.mule.modules.docker.automation.util.TestsConstants;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.command.ListVolumesResponse;

public class VolumeOperationTests extends DockerConnectorAbstractTestCase {

    DockerVolumeOperations dockerVolumeOperations;

    @Before
    public void setup() {
        dockerVolumeOperations = new DockerVolumeOperations(mockDockerClient);
    }

    @Test
    public void verifyCreateVolumeWithNameDriver() {
        CreateVolumeResponse createVolumeResp = dockerVolumeOperations.createVolumeImpl(TestsConstants.CREATE_VOLUME_VOLUMENAME, TestsConstants.CREATE_VOLUME_VOLUMEDRIVER,
                TestsConstants.CREATE_VOLUME_DRIVEROPTS);
        assertNotNull(createVolumeResp);
        Mockito.verify(mockCreateVolCmd).withDriver(TestsConstants.CREATE_VOLUME_VOLUMEDRIVER);
        Mockito.verify(mockCreateVolCmd).withName(TestsConstants.CREATE_VOLUME_VOLUMENAME);
        Mockito.verify(mockCreateVolCmd).withDriverOpts(TestsConstants.CREATE_VOLUME_DRIVEROPTS);

    }

    @Test
    public void verifyCreateVolumeWithoutNameDriver() {
        CreateVolumeResponse createVolumeResp = dockerVolumeOperations.createVolumeImpl(TestsConstants.CREATE_VOLUME_VOLUMENAME, TestsConstants.CREATE_VOLUME_VOLUMEDRIVER, null);
        assertNotNull(createVolumeResp);
        Mockito.verify(mockCreateVolCmd).withDriver(TestsConstants.CREATE_VOLUME_VOLUMEDRIVER);
        Mockito.verify(mockCreateVolCmd).withName(TestsConstants.CREATE_VOLUME_VOLUMENAME);

    }

    @Test
    public void verifyInspectVolume() {
        InspectVolumeResponse inspectVolumeResponse = dockerVolumeOperations.inspectVolumeImpl(TestsConstants.INSPECT_VOLUME_VOLUME_NAME);
        assertNotNull(inspectVolumeResponse);
        Mockito.verify(mockInspectVolCmd, Mockito.times(0)).withName(TestsConstants.INSPECT_VOLUME_VOLUME_NAME);
    }

    @Test
    public void verifyListVolume() {
        ListVolumesResponse listVolumeResp = dockerVolumeOperations.listVolumeImpl(TestsConstants.LIST_VOLUME_DANGLING_FILTER);
        assertNotNull(listVolumeResp);
        Mockito.verify(mockListVolCmd, Mockito.times(1)).withDanglingFilter(false);
    }

    @Test
    public void verifyRemoveVolume() {
        dockerVolumeOperations.removeVolumeImpl(TestsConstants.REMOVE_VOLUME_VOLUMENAME);
        Mockito.verify(mockDockerClient).removeVolumeCmd(TestsConstants.REMOVE_VOLUME_VOLUMENAME);
    }

}
