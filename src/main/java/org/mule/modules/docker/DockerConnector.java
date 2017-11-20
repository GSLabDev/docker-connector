/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.SourceStrategy;
import org.mule.api.annotations.display.Path;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.param.Default;
import org.mule.api.callback.SourceCallback;
import org.mule.extension.annotations.param.Optional;
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
 * The Docker Connector will allow the end user to perform operations on the
 * docker engine. Almost every operation of images, containers and volumes that
 * can be done via the docker engine APIs can be done using this connector. This
 * connector can be operated in HTTP and HTTPS modes. The technical details of
 * this connector such as request headers, error handling, HTTPS connection, etc
 * are all abstracted from the user to make it easy to use.
 */

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
     * Start a container on docker host.
     * 
     * @param containerName
     *            Name of the container to be started (It is assumed that the
     *            container is initially created or in stopped state).
     */
    @Processor(friendlyName = "Start Container")
    public void startContainer(final String containerName) {
        dockerContOperation.startContainerImpl(containerName);
    }

    /**
     * Get the list of containers.
     * @param showAll
     *          Show all container: Running and Exited 
     * @return Listing of all running containers.
     */
    @Processor(friendlyName = "List Containers")
    public List<Container> listContainers(@Default("false") final boolean showAll) {
        return dockerContOperation.listContainerImpl(showAll);
    }

    /**
     * Create a container on docker host using a docker image.
     * 
     * @param imageName
     *            Docker image name to be used to create the container.
     * @param imageTag
     *            Docker image tag.
     * @param containerName
     *            Name of the container that will be created.
     * @return Low-level information of the created container.
     */
    @Processor(friendlyName = "Create Container")
    public CreateContainerResponse createContainer(@Placement(order = 1) final String imageName,
            @Placement(order = 2) @Default("latest") final String imageTag,
            @Optional @Placement(order = 3) final String containerName) {

        return dockerContOperation.createContainerImpl(imageName, imageTag, containerName);
    }

    /**
     * Tag a docker image.
     * 
     * @param imageName
     *            Name of the source image.
     * @param imageTag
     *            Tag of the source image.
     * @param destImageName
     *            Name of the image to be reflected in registry.
     * @param repository
     *            Repository URL
     * @param destImagetag
     *            Tag of the image in registry.
     */
    @Processor(friendlyName = "Tag Image")
    public void tagImage(@Placement(order = 1) final String imageName, @Placement(order = 2) final String imageTag,
            @Placement(order = 3) final String destImageName, @Placement(order = 4) final String repository,
            @Placement(order = 5) final String destImagetag) {
        dockerImgOperation.tagImageImpl(imageName, imageTag, destImageName, repository, destImagetag);
    }

    /**
     * Push an image to registry.
     * 
     * @param imageName
     *            Name of the image to be pushed in the registry.
     * @param imageTag
     *            Tag of the image that is to be pushed to registry.
     */
    @Processor(friendlyName = "Push Image")
    public void pushImage(@Placement(order = 1) final String imageName, @Placement(order = 2) final String imageTag) {
        dockerImgOperation.pushImageImpl(imageName, imageTag);
    }

    /**
     * Stop the running container.
     * 
     * @param containerName
     *            Name of the container to be stopped (It is assumed that the
     *            container is initially in start state).
     */
    @Processor(friendlyName = "Stop Container")
    public void stopContainer(final String containerName) {
        dockerContOperation.stopContainerImpl(containerName);
    }

    /**
     * Inspect the container.
     * 
     * @param containerName
     *            Name of the existing container.
     * @return Low-level information on the container with containerName.
     */
    @Processor(friendlyName = "Inspect Container")
    public InspectContainerResponse inspectContainer(final String containerName) {
        return dockerContOperation.inspectContainerImpl(containerName);
    }

    /**
     * A custom operation where Create-Container and Start-Container operations
     * are performed.
     * 
     * @param imageName
     *            Docker image name used to create the container.
     * @param imageTag
     *            Docker image tag.
     * @param containerName
     *            Container name which will be created.
     * @param command
     *            Command executed while running container.
     * @return Low level details of the container gets generated from docker.
     */
    @Processor(friendlyName = "Run Container")
    public CreateContainerResponse runContainer(@Placement(order = 1) final String imageName,
            @Placement(order = 2) @Default("latest") final String imageTag, @Placement(order = 3) String containerName,
            @Optional final List<String> command) {
        return dockerContOperation.runContainerImpl(imageName, imageTag, containerName, command);
    }

    /**
     * Get logs from the container with given container name or container id.
     * 
     * @param containerName
     *            Name or Id of the running container.
     * @param showTimeStamp
     *            Select to show time stamp with log statement
     * @param standardOut
     *            Show standard out logs
     * @param standardError
     *            Show standard error logs
     * @param showSince
     *            Show logs since timestamp (Date) or relative (Minutes)
     * @return Logs for the container
     * @throws Exception
     */

    @Source(sourceStrategy = SourceStrategy.POLLING, pollingPeriod = 5000)
    public void getContainerLogs(final SourceCallback sourceCallback, final String containerName,
            @Default("false") boolean showTimeStamp,
            @Placement(group = "Stream Type") @Default("false") boolean standardOut,
            @Default("false") @Placement(group = "Stream Type") boolean standardError, @Default("10") int showSince)
            throws Exception {

        dockerContOperation.getContainerLogsImpl(sourceCallback, containerName, showTimeStamp, standardOut,
                standardError, showSince);
    }

    /**
     * Get a live stream of a container's resource usage statistics.
     * 
     * @param containerName
     *            Name or Id of the container.
     * @return Statistics of Container.
     * @throws Exception
     */
    @Source(sourceStrategy = SourceStrategy.POLLING, pollingPeriod = 5000)

    public void getContainerStatistics(final SourceCallback sourceCallback, final String containerName) {
        dockerContOperation.getContainerStatsImpl(sourceCallback, containerName);
    }

    /**
     * Pull an image from docker registry to the docker host.
     * 
     * @param imageName
     *            Docker image name.
     * @param imageTag
     *            Docker image tag.
     * @return Low-level information on the image imageName.
     * @throws InterruptedException
     *             Throws InterruptedException
     */
    @Processor(friendlyName = "Pull Image")
    public InspectImageResponse pullImage(@Placement(order = 1) final String imageName,
            @Placement(order = 2) @Default("latest") final String imageTag) throws InterruptedException {
        return dockerImgOperation.pullImageImpl(imageName, imageTag);
    }

    /**
     * Restart the container.
     * 
     * @param containerName
     *            Name of the running container.
     */
    @Processor(friendlyName = "Restart Container")
    public void restartContainer(final String containerName) {
        dockerContOperation.restartContainerImpl(containerName);
    }

    /**
     * Kill the container.
     * 
     * @param containerName
     *            Name of the running container.
     */
    @Processor(friendlyName = "Kill Container")
    public void killContainer(final String containerName) {
        dockerContOperation.killContainerImpl(containerName);
    }

    /**
     * Pause the container.
     * 
     * @param containerName
     *            Name of the running container.
     */

    @Processor(friendlyName = "Pause Container")
    public void pauseContainer(final String containerName) {
        dockerContOperation.pauseContainerImpl(containerName);
    }

    /**
     * Unpause the container.
     * 
     * @param containerName
     *            Name of the paused container.
     */

    @Processor(friendlyName = "Unpause Container")
    public void unpauseContainer(final String containerName) {
        dockerContOperation.unpauseContainerImpl(containerName);
    }

    /**
     * Delete the container with container name or id.
     * 
     * @param containerName
     *            Name of the container.
     * @param forceDelete
     *            Force remove container (optional parameter).
     */
    @Processor(friendlyName = "Delete Container")
    public void deleteContainer(final String containerName, @Default("false") boolean forceDelete) {
        dockerContOperation.deleteContainerImpl(containerName, forceDelete);
    }

    /**
     * Get the list of images.
     * 
     * @return Listing of all images.
     */
    @Processor(friendlyName = "List Images")
    public List<Image> listImage() {
        return dockerImgOperation.listImageImpl();
    }

    /**
     * Create an image using docker file.
     * 
     * @param dockerFilePath
     *            Path of the Docker file.
     * @param imageName
     *            Name of the new target image
     * @param imageTag
     *            Target image name tag
     * @return Low-level information on the image name.
     * @throws InterruptedException
     *             throws InterruptedException
     */
    @Processor(friendlyName = "Build an image from Docker file")
    public InspectImageResponse buildImage(@Placement(order = 1) @Path final String dockerFilePath,
            @Placement(order = 2) final String imageName, @Placement(order = 3) final String imageTag)
            throws InterruptedException {

        return dockerImgOperation.buildImageImpl(dockerFilePath, imageName, imageTag);
    }

    /**
     * Inspect an image to get the low level information of the image.
     * 
     * @param imageName
     *            Docker image name to inspect.
     * @param imageTag
     *            Docker image tag.
     * @return Low-level information on the image name.
     */
    @Processor(friendlyName = "Inspect Image")
    public InspectImageResponse inspectImage(@Placement(order = 1) final String imageName,
            @Default("latest") final String imageTag) {

        return dockerImgOperation.inspectImageImpl(imageName, imageTag);
    }

    /**
     * Delete the image with given image name or id.
     * 
     * @param imageName
     *            Docker image name that needs to be removed.
     * @param imageTag
     *            Docker image tag.
     * @param forceRemove
     *            Force remove image (optional parameter).
     * @param imageTag
     *            Docker image tag.
     */
    @Processor(friendlyName = "Remove Image")
    public void removeImage(@Placement(order = 1) final String imageName, @Default("latest") final String imageTag,
            final Boolean forceRemove) {
        dockerImgOperation.removeImageImpl(imageName, imageTag, forceRemove);
    }

    /**
     * List the volumes.
     * 
     * @return Listing volume response.
     */
    @Processor(friendlyName = "List Volumes")
    public ListVolumesResponse listVolume() {

        return dockerVolOperation.listVolumeImpl();
    }

    /**
     * Wait for a container with given name or id to execute.
     * 
     * @param containerName
     *            Name of the container that needs to wait.
     */
    @Processor(friendlyName = "Wait for A Container")
    public void waitAContainer(final String containerName) {
        dockerContOperation.waitAContainerImpl(containerName);
    }

    /**
     * Create new volume on docker.
     * 
     * @param volumeName
     *            Name of the volume.
     * @param volumeDriver
     *            Name of the driver.
     * @return The low level info of the created volume.
     */
    @Processor(friendlyName = "Create Volume")
    public CreateVolumeResponse createVolume(final String volumeName, final String volumeDriver) {

        return dockerVolOperation.createVolumeImpl(volumeName, volumeDriver);
    }

    /**
     * Inspect volume for low-level information on the given volume name.
     * 
     * @param volumeName
     *            Name of the volume.
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
     *            Name of the volume.
     */
    @Processor(friendlyName = "Remove Volume")
    public void removeVolume(final String volumeName) {
        dockerVolOperation.removeVolumeImpl(volumeName);
    }
}