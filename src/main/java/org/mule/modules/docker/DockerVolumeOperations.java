/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.command.ListVolumesResponse;

public class DockerVolumeOperations {

    private static final Logger logger = LogManager.getLogger(DockerVolumeOperations.class.getName());

    private DockerClient dockerClient;

    public DockerVolumeOperations(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public ListVolumesResponse listVolumeImpl() {
        logger.info("Listing Volumes");
        ListVolumesResponse listVolumesResponse = dockerClient.listVolumesCmd().exec();
        logger.info("Volumes listed successfully");
        logger.debug("List volume respone : " + listVolumesResponse);
        return listVolumesResponse;
    }

    public CreateVolumeResponse createVolumeImpl(final String volumeName, final String volumeDriver) {
        logger.info("Creating volume : " + volumeName);
        CreateVolumeResponse createVolumeResponse = dockerClient.createVolumeCmd().withName(volumeName)
                .withDriver(volumeDriver).exec();
        logger.info("Create volume response: " + createVolumeResponse);
        return createVolumeResponse;
    }

    public InspectVolumeResponse inspectVolumeImpl(final String volumeName) {
        logger.info("Inspecting Volume : " + volumeName);
        final InspectVolumeResponse inspectVolumeResponse = dockerClient.inspectVolumeCmd(volumeName).exec();
        logger.info("Done inspecting volume");
        logger.debug("Inspect Volume response : " + inspectVolumeResponse);
        return inspectVolumeResponse;
    }

    public void removeVolumeImpl(final String volumeName) {
        logger.info("Removing Volume : " + volumeName);
        dockerClient.removeVolumeCmd(volumeName).exec();
        logger.info("Volume " + volumeName + " is removed");
    }

}
