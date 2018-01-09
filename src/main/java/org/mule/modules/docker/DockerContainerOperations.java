/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.docker;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.docker.json.CreateContainerPojo;
import org.mule.modules.docker.json.JsonParametersProcessor;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import com.github.dockerjava.core.command.WaitContainerResultCallback;

/**
 * @author Great Software Laboratory Pvt. Ltd.
 */
public class DockerContainerOperations {

    /**
     * Logging object
     */
    private static final Logger logger = LogManager.getLogger(DockerContainerOperations.class.getName());

    private DockerClient dockerClient;

    public DockerContainerOperations(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    /**
     * Start a container on docker host.
     * 
     * @param containerName
     *            Name of the container to be started (container is initially in
     *            stopped state).
     */
    public void startContainerImpl(final String containerName) {
        logger.info("starting Container " + containerName);
        dockerClient.startContainerCmd(containerName).exec();
        logger.info(containerName + " Container started");
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
    public List<Container> listContainerImpl(final boolean showAll, String before, int limit, boolean showSize,
            String status, String labels) {

        List<Container> containerList;
        logger.info("Getting container list from docker");
        final ListContainersCmd listContainerResponse = dockerClient.listContainersCmd().withShowAll(showAll)
                .withShowSize(showSize);
        if (before != null) {
            logger.info("Listing all containers before ", before);
            listContainerResponse.withBefore(before);
        }
        if (limit != 0) {
            logger.info("Using limit value : ", limit);
            listContainerResponse.withLimit(limit);
            logger.info("limit executed");
        }
        if (status != null) {
            logger.info("Using status value : " + status);
            listContainerResponse.withStatusFilter(status);
            logger.info("status executed : " + status.getClass().getName());
        }
        if (labels != null) {
            logger.info("Listing containers using label filter : " + labels);
            listContainerResponse.withLabelFilter(labels);
        }
        containerList = listContainerResponse.exec();
        return containerList;

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
    public CreateContainerResponse createContainerImpl(final String imageName, final String imageTag,
            final String containerName, String jsonFilePath) {
        logger.info("Creating container");
        CreateContainerResponse createContainerResponse = null;

        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd("");
        if (jsonFilePath != null) {
            logger.info("Using file " + jsonFilePath);
            JsonParametersProcessor.parseJsonParameters(jsonFilePath, createContainerCmd, CreateContainerPojo.class);
        }

        if (imageName != null && imageTag != null) {
            createContainerCmd.withImage(imageName + ":" + imageTag);
        }
        if (containerName != null) {
            createContainerCmd.withName(containerName);
        }

        createContainerResponse = createContainerCmd.exec();
        logger.info("Create container response " + createContainerResponse);

        return createContainerResponse;
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
    public void stopContainerImpl(final String containerName, final int timeout) {
        logger.info("Stopping Container " + containerName);
        if (timeout != 0) {
            dockerClient.stopContainerCmd(containerName).withTimeout(timeout).exec();
            logger.info(containerName + " Container stopped after " + timeout);
        } else {
            dockerClient.stopContainerCmd(containerName).exec();
            logger.info(containerName + " Container stopped");
        }

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
    public InspectContainerResponse inspectContainerImpl(final String containerName, final boolean showSize) {
        logger.info("Inspecting Container " + containerName);
        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(containerName)
                .withSize(showSize).exec();
        logger.info("Done inspecting a container");
        logger.debug("Inspect Container response: " + inspectContainerResponse);
        return inspectContainerResponse;
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
    public CreateContainerResponse runContainerImpl(final String imageName, final String imageTag,
            final String containerName, final List<String> command) {
        logger.info("Creating and Starting container " + containerName);
        CreateContainerCmd createContainerCommand = dockerClient.createContainerCmd(imageName + ":" + imageTag)
                .withName(containerName);
        if (command != null) {
            createContainerCommand.withCmd(command);
        }
        CreateContainerResponse createContainerResponse = createContainerCommand.exec();

        logger.info("Create container response : " + createContainerResponse);
        dockerClient.startContainerCmd(containerName).exec();
        logger.info(containerName + " container is stared");
        return createContainerResponse;
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
    public void getContainerLogsImpl(final SourceCallback sourceCallback, final String containerName,
            final boolean showTimeStamp, final boolean standardOut, final boolean standardError, final int showSince,
            final int tail, final boolean followStream) {
        logger.info("Getting container logs");
        if (tail != 0) {
            logger.debug("Container logs with tail : " + tail);
            dockerClient.logContainerCmd(containerName).withTimestamps(showTimeStamp).withStdOut(standardOut)
                    .withStdErr(standardError).withSince(showSince).withTail(tail).withFollowStream(followStream)
                    .exec(new SourceCallBack<Frame>(sourceCallback));
        } else {
            dockerClient.logContainerCmd(containerName).withTimestamps(showTimeStamp).withStdOut(standardOut)
                    .withStdErr(standardError).withSince(showSince).withFollowStream(followStream)
                    .exec(new SourceCallBack<Frame>(sourceCallback));
        }

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
    public void getContainerStatsImpl(final SourceCallback sourceCallback, final String containerName) {
        logger.info("Getting Statistics of Container : " + containerName);
        dockerClient.statsCmd(containerName).exec(new SourceCallBack<Statistics>(sourceCallback));
    }

    /**
     * Restart the container.
     * 
     * @param containerName
     *            Name or ID of the running container
     * @param timeout
     *            Number of seconds to wait before restarting the container
     */
    public void restartContainerImpl(final String containerName, final int timeout) {
        logger.info("Restarting Container " + containerName);
        if (timeout != 0) {
            dockerClient.restartContainerCmd(containerName).withtTimeout(timeout).exec();
            logger.info(containerName + " Container restared");
        } else {

            dockerClient.restartContainerCmd(containerName).exec();
            logger.info(containerName + " Container restared");
        }
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
    public void killContainerImpl(final String containerName, String signal) {
        logger.info("Killing Container " + containerName);
        dockerClient.killContainerCmd(containerName).withSignal(signal).exec();
        logger.info(containerName + " Container Killed with signal " + signal);
    }

    /**
     * Pause the container.
     * 
     * @param containerName
     *            Name or ID of the running container
     */
    public void pauseContainerImpl(final String containerName) {
        logger.info("Pausing Container " + containerName);
        dockerClient.pauseContainerCmd(containerName).exec();
        logger.info(containerName + " Container Paused");
    }

    /**
     * Unpause the container.
     * 
     * @param containerName
     *            Name or ID of the paused container
     */
    public void unpauseContainerImpl(final String containerName) {
        logger.info("Unpausing Container " + containerName);
        dockerClient.unpauseContainerCmd(containerName).exec();
        logger.info(containerName + " Container Unpaused");
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
    public void deleteContainerImpl(final String containerName, final boolean forceDelete,
            final boolean removeVolumes) {
        logger.info("Deleting Container " + containerName);
        dockerClient.removeContainerCmd(containerName).withForce(forceDelete).withRemoveVolumes(removeVolumes).exec();
        logger.info(containerName + " Container Deleted");
    }

    /**
     * Wait for a container with given name or ID to execute.
     * 
     * @param containerName
     *            Name or ID of the container that needs to wait
     */
    public void waitAContainerImpl(final String containerName) {
        WaitContainerResultCallback waitCallback = new WaitContainerResultCallback();
        logger.info("Waiting for container " + containerName);
        final int exitCode = dockerClient.waitContainerCmd(containerName).exec(waitCallback).awaitStatusCode();
        logger.info("Cotainer exit code is " + exitCode);
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
                logger.error(e);
            }

        }
    }

}