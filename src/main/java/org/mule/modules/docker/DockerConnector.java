/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.SourceStrategy;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Path;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.display.Summary;
import org.mule.api.annotations.licensing.RequiresEnterpriseLicense;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.docker.config.AbstractDockerConfig;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.command.ListVolumesResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;

/**
 * @author Great Software Laboratory Pvt. Ltd. 
 */

/**
 * Docker Connector, built using Docker Java API client, is a communication tool
 * that provides seamless integration with the Docker engine from a mule flow.
 * It exposes Docker operations by executing their API calls as per
 * configuration. It supports HTTP and HTTPS connections and can be used as
 * inbound as well as outbound connector from the mule flow.
 */

@RequiresEnterpriseLicense ( allowEval = true )
@Connector(name = "docker", friendlyName = "Docker", minMuleVersion = "3.8.5")
public class DockerConnector {
    /**
     * Configuration
     */
    @Config
    private AbstractDockerConfig config;

    /**
     * All operations objects
     */

    private DockerContainerOperations dockerContOperation;
    private DockerImageOperations dockerImgOperation;
    private DockerVolumeOperations dockerVolOperation;
    private static final Logger logger = LogManager.getLogger(DockerConnector.class.getName());

    /**
     * Returns Docker Connector configuration
     */
    public AbstractDockerConfig getConfig() {
        return config;
    }

    /**
     * Set Docker Connector configuration
     */
    public void setConfig(AbstractDockerConfig config) {
        this.config = config;
        init(this.config.getDockerClient());
    }

    private void init(DockerClient dockerClient) {
        dockerContOperation = new DockerContainerOperations(dockerClient);
        dockerImgOperation = new DockerImageOperations(dockerClient);
        dockerVolOperation = new DockerVolumeOperations(dockerClient);
    }

    /**
     * Get the docker information.
     * 
     * @return Docker info like number of containers, number of images, server
     *         version etc.
     */
    @Processor(friendlyName = "Docker Info")
    public Info dockerInfo() {
        logger.info("Getting docker info...");
        final Info info = config.getDockerClient().infoCmd().exec();
        logger.info("Docker info received");
        logger.debug("Docker info response : " + info);
        return info;
    }

    /**
     * Get the list of containers.
     * 
     * @param showAll
     *            Show all container: Running and Exited
     * @param before
     *            Show only containers created before Id, include non-running
     *            ones.
     * @param limit
     *            Show limit last created containers, include non-running ones
     * @param showSize
     *            Show the containers sizes
     * @param status
     *            Container status like
     *            created,restarting,running,paused,exited,dead.
     * @param labels
     *            labels associated with the container
     * 
     * @return Listing of all running containers.
     */
    @Processor(friendlyName = "List Containers")
    public List<Container> listContainers(@Default("false") final boolean showAll, @Optional String before,
            @Default("0") int limit, @Default("false") boolean showSize, @Default("running") String status,
            @Optional String labels) {
        return dockerContOperation.listContainerImpl(showAll, before, limit, showSize, status, labels);
    }

    /**
     * Create a container on docker host using a docker image.
     * 
     * @param imageName
     *            Docker image name to be used to create the container
     * @param imageTag
     *            Docker image tag
     * @param containerName
     *            Name of the container that will be created
     * @param jsonFilePath
     *            Path of JSON file that will be used to set request parameters.
     *            All options except Healthcheck are supported in the JSON.
     * @return Low-level information of the created container.
     */
    @Processor(friendlyName = "Create Container")
    public CreateContainerResponse createContainer(@Placement(order = 1) @Optional final String imageName,
            @Placement(order = 2) @Default("latest") final String imageTag,
            @Optional @Placement(order = 3) final String containerName,
            @Optional @Path @FriendlyName("JSON File Path") String jsonFilePath) {

        return dockerContOperation.createContainerImpl(imageName, imageTag, containerName, jsonFilePath);
    }

    /**
     * Inspect the container.
     * 
     * @param containerName
     *            Name of the existing container
     * @param showSize
     *            Return container size information
     * @return Low-level information on the container with containerName.
     */
    @Processor(friendlyName = "Inspect Container")
    public InspectContainerResponse inspectContainer(final String containerName, @Default("false") boolean showSize) {
        return dockerContOperation.inspectContainerImpl(containerName, showSize);
    }

    /**
     * Get logs from the container with given container name or container ID.
     * 
     * @param containerName
     *            Name or ID of the running container
     * @param tail
     *            Output specified number of lines at the end of logs
     * @param followStream
     *            Follow the logs
     * @param showTimeStamp
     *            Select to show time stamp with log statement
     * @param standardOut
     *            Print timestamps for every log line. Show standard out logs
     * @param sourceCallback
     *            parameter to process the callback which represents the next
     *            message processor in the chain
     * @param standardError
     *            Show standard error logs
     * @param showSince
     *            Show logs since timestamp or relative Minutes
     * 
     */

    @Source(sourceStrategy = SourceStrategy.POLLING, pollingPeriod = 5000)
    public void getContainerLogs(final SourceCallback sourceCallback, final String containerName,
            @Default("false") boolean showTimeStamp,
            @Placement(group = "Stream Type") @Default("false") boolean standardOut,
            @Default("false") @Placement(group = "Stream Type") boolean standardError, @Default("10") int showSince,
            @Default("0") int tail, @Default("false") boolean followStream) {

        dockerContOperation.getContainerLogsImpl(sourceCallback, containerName, showTimeStamp, standardOut,
                standardError, showSince, tail, followStream);
    }

    /**
     * Get information of a container's resource usage statistics.
     * 
     * @param containerName
     *            Name or Id of the container
     * 
     * @param sourceCallback
     *            Parameter to process the callback which represents the next
     *            message processor in the chain.
     * 
     */
    @Source(sourceStrategy = SourceStrategy.POLLING, pollingPeriod = 5000)
    public void getContainerStatistics(final SourceCallback sourceCallback, final String containerName) {
        dockerContOperation.getContainerStatsImpl(sourceCallback, containerName);
    }

    /**
     * Start a container on docker host.
     * 
     * @param containerName
     *            Name of the container to be started (container is initially in
     *            stopped state).
     */
    @Processor(friendlyName = "Start Container")
    public void startContainer(final String containerName) {
        dockerContOperation.startContainerImpl(containerName);
    }

    /**
     * Stop the running container.
     * 
     * @param containerName
     *            Name or ID of the container to be stopped (Container is
     *            initially in start state).
     * @param timeout
     *            Number of seconds to wait before stopping the container
     */
    @Processor(friendlyName = "Stop Container")
    public void stopContainer(final String containerName, @Default("0") final int timeout) {
        dockerContOperation.stopContainerImpl(containerName, timeout);
    }

    /**
     * Restart the container.
     * 
     * @param containerName
     *            Name or ID of the running container
     * @param timeout
     *            Number of seconds to wait before restarting the container
     */
    @Processor(friendlyName = "Restart Container")
    public void restartContainer(final String containerName, @Default("0") int timeout) {
        dockerContOperation.restartContainerImpl(containerName, timeout);
    }

    /**
     * Kill the container.
     * 
     * @param containerName
     *            Name or ID of the running container
     * @param signal
     *            Signal to send to the container: integer or string like
     *            SIGINT. When not set, SIGKILL is assumed and the call waits
     *            for the container to exit.
     */
    @Processor(friendlyName = "Kill Container")
    public void killContainer(final String containerName, @Default("SIGKILL") String signal) {
        dockerContOperation.killContainerImpl(containerName, signal);
    }

    /**
     * Pause the container.
     * 
     * @param containerName
     *            Name or ID of the running container
     */

    @Processor(friendlyName = "Pause Container")
    public void pauseContainer(final String containerName) {
        dockerContOperation.pauseContainerImpl(containerName);
    }

    /**
     * Unpause the container.
     * 
     * @param containerName
     *            Name or ID of the paused container
     */

    @Processor(friendlyName = "Unpause Container")
    public void unpauseContainer(final String containerName) {
        dockerContOperation.unpauseContainerImpl(containerName);
    }

    /**
     * Wait for a container with given name or ID to execute.
     * 
     * @param containerName
     *            Name or ID of the container that needs to wait
     */
    @Processor(friendlyName = "Wait for A Container")
    public void waitAContainer(final String containerName) {
        dockerContOperation.waitAContainerImpl(containerName);
    }

    /**
     * Delete the container with container name or ID.
     * 
     * @param containerName
     *            Name or ID of the container
     * @param forceDelete
     *            Force remove container
     * @param removeVolumes
     *            Remove the volumes associated with the container
     */
    @Processor(friendlyName = "Delete Container")
    public void deleteContainer(final String containerName, @Default("false") boolean forceDelete,
            @Default("false") boolean removeVolumes) {
        dockerContOperation.deleteContainerImpl(containerName, forceDelete, removeVolumes);
    }

    /**
     * A custom operation where Create-Container and Start-Container operations
     * are performed.
     * 
     * @param imageName
     *            Docker image name used to create the container
     * @param imageTag
     *            Docker image tag
     * @param containerName
     *            Container name or ID which will be created
     * @param command
     *            Command executed while running container
     * @return Low level details of the container gets generated from docker.
     */
    @Processor(friendlyName = "Run Container")
    public CreateContainerResponse runContainer(@Placement(order = 1) final String imageName,
            @Placement(order = 2) @Default("latest") final String imageTag, @Placement(order = 3) String containerName,
            @Optional final List<String> command) {
        return dockerContOperation.runContainerImpl(imageName, imageTag, containerName, command);
    }

    /**
     * Get the list of images.
     * 
     * @param showAll
     *            Display all the images
     * @param dangling
     *            Images with dangling status
     * @param imageNameFilter
     *            Return images with the specified name
     * @param imageLabelFilter
     *            Return images with the specified label
     * 
     * @return Listing of all images.
     */
    @Processor(friendlyName = "List Images")
    public List<Image> listImage(@Default("false") final boolean showAll, @Default("false") final boolean dangling,
            @Optional final String imageNameFilter, @Optional final String imageLabelFilter) {
        return dockerImgOperation.listImageImpl(showAll, dangling, imageNameFilter, imageLabelFilter);
    }

    /**
     * Create an image using docker file.
     * 
     * @param dockerFilePath
     *            Path of the Docker file
     * @param imageTags
     *            Tag to apply to the image in the "name:tag" format
     * @param cacheFromImage
     *            Use the cache when building the image
     * @param buildArgumetName
     *            Name of build-time variables
     * @param buildArgumetValue
     *            Value of build-time variables
     * @param cpuSet
     *            CPUs in which to allow execution (e.g., 0-3, 0,1)
     * @param cpuShares
     *            CPU shares (relative weight)
     * @param labels
     *            Key-value labels to set on the image
     * @param memory
     *            Memory limit for build (in Bytes)
     * @param memswap
     *            Total memory (memory + swap). Set as -1 to disable swap
     * @param forcerm
     *            Always remove intermediate containers, even upon failure
     * @param noCache
     *            Do not use the cache when building the image
     * @param pullImage
     *            Attempt to pull the image even if an older image exists
     *            locally.
     * @param removeContainers
     *            Remove intermediate containers after a successful build
     * @param remoteURI
     *            A Git repository URI or HTTP/HTTPS context URI pointing to
     *            Dockerfile or tarball.
     * @return Low-level information of the image.
     * @throws InterruptedException
     *             throws InterruptedException
     * @throws URISyntaxException
     *             throws URISyntaxException
     */
    @Processor(friendlyName = "Build an image from Docker file")
    public InspectImageResponse buildImage(@Placement(order = 1) @Path final String dockerFilePath,
            @Placement(order = 2) @Summary("imageName:ImageTag") final List<String> imageTags,
            @Placement(group = "CPU") @Summary("CPUs in which to allow execution (e.g., 0-3, 0,1).") @Optional final String cpuSet,
            @Placement(group = "CPU") @Summary("CPU shares (relative weight).") @Optional final String cpuShares,
            @Placement(group = "labels") @Optional final Map<String, String> labels,
            @Placement(group = "Memory") @Summary("Set memory limit for build. Set as 0 to use default memory.") @Default("0") final long memory,
            @Placement(group = "Memory") @Summary("Total memory (memory + swap). Set as -1 to disable swap.") @Default("-1") final long memswap,
            @Placement(group = "Build Args") @Summary("Build-time variables") @Default("null") final String buildArgumetName,
            @Placement(group = "Build Args") @Optional final String buildArgumetValue,
            @Placement(group = "Cache") @Summary("Images used for build cache resolution.") @Optional final List<String> cacheFromImage,
            @Placement(group = "Cache") @Summary("Do not use the cache when building the image.") @Default("false") final Boolean noCache,
            @Placement(group = "Build Args") @Summary("Force remove intermediate container") @Default("true") final Boolean forcerm,
            @Placement(group = "Build Args") @Default("true") @Summary("Attempt to pull the image even if an older image exists locally.") final Boolean pullImage,
            @Placement(group = "Build Args") @Summary("Remove intermediate containers after a successful build") final Boolean removeContainers,
            @Placement(group = "Remote URI") @Optional @Summary("A Git repository URI or HTTP/HTTPS context URI points to Dockerfile or tarball") @FriendlyName("Remote URI") final String remoteURI)
            throws InterruptedException, URISyntaxException {
        return dockerImgOperation.buildImageImpl(dockerFilePath, imageTags, buildArgumetName, buildArgumetValue,
                cacheFromImage, cpuSet, cpuShares, labels, memory, memswap, forcerm, noCache, pullImage,
                removeContainers, remoteURI);
    }

    /**
     * Pull an image from docker registry to the docker host.
     * 
     * @param imageName
     *            Docker image name or the registry url for the image like
     *            localhost:5000/ubuntu.
     * @param imageTag
     *            Docker image tag
     * @param username
     *            Username for the private registry
     * @param password
     *            Password for the private registry
     * @return Low-level information on the image imageName.
     * @throws InterruptedException
     *             Throws InterruptedException
     */
    @Processor(friendlyName = "Pull Image")
    public InspectImageResponse pullImage(@Placement(order = 1) final String imageName,
            @Placement(order = 2) @Default("latest") final String imageTag,
            @Optional @Placement(group = "Authentication Details") final String username,
            @Optional @Placement(group = "Authentication Details") final String password) throws InterruptedException {
        return dockerImgOperation.pullImageImpl(imageName, imageTag, username, password);
    }

    /**
     * Inspect an image to get the low level information of the image.
     * 
     * @param imageName
     *            Docker image name to inspect
     * @param imageTag
     *            Docker image tag
     * @return Low-level information on the image name.
     */
    @Processor(friendlyName = "Inspect Image")
    public InspectImageResponse inspectImage(@Placement(order = 1) final String imageName,
            @Default("latest") final String imageTag) {

        return dockerImgOperation.inspectImageImpl(imageName, imageTag);
    }

    /**
     * Push an image to registry.
     * 
     * @param imageName
     *            Name of the image to be pushed in the registry with registry
     *            URL like localhost:5000/ubuntu.
     * @param imageTag
     *            Tag of the image that is to be pushed to registry
     * @param username
     *            Username of the private registry
     * @param password
     *            Password of the private registry
     */
    @Processor(friendlyName = "Push Image")
    public void pushImage(@Placement(order = 1) final String imageName, @Placement(order = 2) final String imageTag,
            @Optional @Placement(group = "Authentication Details") final String username,
            @Optional @Placement(group = "Authentication Details") final String password) {
        dockerImgOperation.pushImageImpl(imageName, imageTag, username, password);
    }

    /**
     * Tag a docker image.
     * 
     * @param imageName
     *            Name of the source image
     * @param imageTag
     *            Tag of the source image
     * @param destImageName
     *            Name of the image to be reflected in registry
     * @param repository
     *            Repository URL
     * @param destImagetag
     *            Tag of the image in registry
     */
    @Processor(friendlyName = "Tag Image")
    public void tagImage(@Placement(order = 1) final String imageName, @Placement(order = 2) final String imageTag,
            @Placement(order = 3) final String destImageName, @Placement(order = 4) final String repository,
            @Placement(order = 5) final String destImagetag) {
        dockerImgOperation.tagImageImpl(imageName, imageTag, destImageName, repository, destImagetag);
    }

    /**
     * Delete the image with given image name or ID.
     * 
     * @param imageName
     *            Docker image name that needs to be removed
     * @param imageTag
     *            Docker image tag
     * @param forceRemove
     *            Force remove image
     * @param prune
     *            Do not delete untagged parent images
     * @param imageId
     *            Image ID of the docker image that needs to be removed
     */
    @Processor(friendlyName = "Remove Image")
    public void removeImage(@Placement(order = 1) @Optional final String imageName,
            @Default("latest") final String imageTag, @Default("false") final Boolean forceRemove,
            @Default("false") final boolean prune, @Optional final String imageId) {
        dockerImgOperation.removeImageImpl(imageName, imageTag, forceRemove, prune, imageId);
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
    @Processor(friendlyName = "List Volumes")
    public ListVolumesResponse listVolume(@Default("false") boolean danglingFilter) {
        return dockerVolOperation.listVolumeImpl(danglingFilter);
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
    @Processor(friendlyName = "Create Volume")
    public CreateVolumeResponse createVolume(final String volumeName, @Default("local") final String volumeDriver,
            @Optional Map<String, String> driverOpts) {
        return dockerVolOperation.createVolumeImpl(volumeName, volumeDriver, driverOpts);
    }

    /**
     * Inspect volume for low-level information on the given volume name.
     * 
     * @param volumeName
     *            Volume name or ID
     * @return Low-level information on the given volume name.
     */
    @Processor(friendlyName = "Inspect Volume")
    public InspectVolumeResponse inspectVolume(final String volumeName) {
        return dockerVolOperation.inspectVolumeImpl(volumeName);
    }

    /**
     * Operation to remove a volume.
     * 
     * @param volumeName
     *            Volume name or ID
     */
    @Processor(friendlyName = "Remove Volume")
    public void removeVolume(final String volumeName) {
        dockerVolOperation.removeVolumeImpl(volumeName);
    }
}
