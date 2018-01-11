/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.command.ListVolumesResponse;

/**
 * @author Great Software Laboratory Pvt. Ltd.
 */
public class DockerVolumeOperations {

    private static final Logger logger = LogManager.getLogger(DockerVolumeOperations.class.getName());

    private DockerClient dockerClient;

    public DockerVolumeOperations(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    /**
     * List the volumes.
     * 
     * @param danglingFilter
     *            Returns all volumes that are dangling (not in use by a
     *            container).
     * 
     * @return Listing volume response.
     */
    public ListVolumesResponse listVolumeImpl(final boolean danglingFilter) {
        logger.info("Listing Volumes");
        ListVolumesResponse listVolumesResponse = dockerClient.listVolumesCmd().withDanglingFilter(danglingFilter)
                .exec();
        logger.info("Volumes listed successfully");
        logger.debug("List volume respone : " + listVolumesResponse);
        return listVolumesResponse;
    }

    /**
     * Create new volume on docker.
     * 
     * @param volumeName
     *            The new volume's name
     * @param volumeDriver
     *            Name of the volume driver to use
     * @param driverOpts
     *            A mapping of driver options and values. These options are
     *            passed directly to the driver and are driver specific.
     * @return The low level info of the created volume.
     */
    public CreateVolumeResponse createVolumeImpl(final String volumeName, final String volumeDriver,
            final Map<String, String> driverOpts) {
        logger.info("Creating volume : " + volumeName);
        CreateVolumeResponse createVolumeResponse;
        if (driverOpts != null) {
            createVolumeResponse = dockerClient.createVolumeCmd().withName(volumeName).withDriver(volumeDriver)
                    .withDriverOpts(driverOpts).exec();
        } else {
            createVolumeResponse = dockerClient.createVolumeCmd().withName(volumeName).withDriver(volumeDriver).exec();
        }

        logger.info("Create volume response: " + createVolumeResponse);
        return createVolumeResponse;
    }

    /**
     * Inspect volume for low-level information on the given volume name.
     * 
     * @param volumeName
     *            Volume name or ID
     * @return Low-level information on the given volume name.
     */
    public InspectVolumeResponse inspectVolumeImpl(final String volumeName) {
        logger.info("Inspecting Volume : " + volumeName);
        final InspectVolumeResponse inspectVolumeResponse = dockerClient.inspectVolumeCmd(volumeName).exec();
        logger.info("Done inspecting volume");
        logger.debug("Inspect Volume response : " + inspectVolumeResponse);
        return inspectVolumeResponse;
    }

    /**
     * Operation to remove a volume.
     * 
     * @param volumeName
     *            Volume name or ID
     */
    public void removeVolumeImpl(final String volumeName) {
        logger.info("Removing Volume : " + volumeName);
        dockerClient.removeVolumeCmd(volumeName).exec();
        logger.info("Volume " + volumeName + " is removed");
    }

}
