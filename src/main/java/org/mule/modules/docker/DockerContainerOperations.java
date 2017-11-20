/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.api.callback.SourceCallback;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import com.github.dockerjava.core.command.WaitContainerResultCallback;

public class DockerContainerOperations {

    /**
     * Logging object
     */
    private static final Logger logger = LogManager.getLogger(DockerContainerOperations.class.getName());

    private DockerClient dockerClient;

    public DockerContainerOperations(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public void startContainerImpl(final String containerName) {
        logger.info("starting Container " + containerName);
        dockerClient.startContainerCmd(containerName).exec();
        logger.info(containerName + " Container started");
    }

    public List<Container> listContainerImpl(boolean showAll) {
        logger.info("Getting container list from docker");
        List<Container> listContainerResponse = dockerClient.listContainersCmd().withShowAll(showAll).exec();
        logger.info("Listing container responses : " + listContainerResponse);
        return listContainerResponse;
    }

    public CreateContainerResponse createContainerImpl(final String imageName, final String imageTag,
            final String containerName) {
        logger.info("Create container operation initiated");
        final CreateContainerResponse createContainerResponse = dockerClient
                .createContainerCmd(imageName + ":" + imageTag).withName(containerName).exec();
        logger.info("Create container response " + createContainerResponse);
        return createContainerResponse;
    }

    public void stopContainerImpl(final String containerName) {
        logger.info("Stopping Container " + containerName);
        dockerClient.stopContainerCmd(containerName).exec();
        logger.info(containerName + " Container stopped");
    }

    public InspectContainerResponse inspectContainerImpl(final String containerName) {
        logger.info("Inspecting Container " + containerName);
        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(containerName).exec();
        logger.info("Done inspecting a container");
        logger.debug("Inspect Container response: " + inspectContainerResponse);
        return inspectContainerResponse;
    }

    public CreateContainerResponse runContainerImpl(final String imageName, final String imageTag,
            final String containerName, final List<String> command) {
        logger.info("Creating and Starting container " + containerName);
        CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd(imageName + ":" + imageTag)
                .withName(containerName).withCmd(command).exec();
        logger.info("Create container response : " + createContainerResponse);
        dockerClient.startContainerCmd(containerName).exec();
        logger.info(containerName + "is stared");
        return createContainerResponse;
    }

    public void getContainerLogsImpl(final SourceCallback sourceCallback, final String containerName,
            final boolean showTimeStamp, final boolean standardOut, final boolean standardError, final int showSince) {
        logger.info("Getting container logs");

        dockerClient.logContainerCmd(containerName).withTimestamps(showTimeStamp).withStdOut(standardOut)
                .withStdErr(standardError).withSince(showSince).withTailAll()
                .exec(new SourceCallBack<Frame>(sourceCallback));
    }

    public void getContainerStatsImpl(final SourceCallback sourceCallback, final String containerName) {
        logger.info("Getting Statistics of Container : " + containerName);
        dockerClient.statsCmd(containerName).exec(new SourceCallBack<Statistics>(sourceCallback));
    }

    public void restartContainerImpl(final String containerName) {
        logger.info("Restarting Container " + containerName);
        dockerClient.restartContainerCmd(containerName).exec();
        logger.info(containerName + " Container restared");
    }

    public void killContainerImpl(final String containerName) {
        logger.info("Killing Container " + containerName);
        dockerClient.killContainerCmd(containerName).exec();
        logger.info(containerName + " Container Killed");
    }

    public void pauseContainerImpl(final String containerName) {
        logger.info("Pausing Container " + containerName);
        dockerClient.pauseContainerCmd(containerName).exec();
        logger.info(containerName + " Container Paused");
    }

    public void unpauseContainerImpl(final String containerName) {
        logger.info("Unpausing Container " + containerName);
        dockerClient.unpauseContainerCmd(containerName).exec();
        logger.info(containerName + " Container Unpaused");
    }

    public void deleteContainerImpl(final String containerName, final boolean forceDelete) {
        logger.info("Deleting Container " + containerName);
        dockerClient.removeContainerCmd(containerName).withForce(forceDelete).exec();
        logger.info(containerName + " Container Deleted");
    }

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