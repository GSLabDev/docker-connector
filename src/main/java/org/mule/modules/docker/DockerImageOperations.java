/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;

public class DockerImageOperations {

    /**
     * Logging object
     */
    private static final Logger logger = LogManager.getLogger(DockerImageOperations.class.getName());

    private DockerClient dockerClient;

    public DockerImageOperations(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public InspectImageResponse pullImageImpl(final String imageName, final String imageTag)
            throws InterruptedException {
        logger.info("Please wait......... Pulling docker image " + imageName);
        final PullImageResultCallback pullImageCallback = new PullImageResultCallback();
        dockerClient.pullImageCmd(imageName + ":" + imageTag).exec(pullImageCallback).awaitSuccess();
        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageName + ":" + imageTag).exec();
        logger.info("Inspect image response for pull image:" + inspectImageResponse);
        return inspectImageResponse;
    }

    public void tagImageImpl(final String imageName, final String imageTag, final String destImageName,
            final String repository, final String destImagetag) {
        logger.info("Tagging image " + imageName);
        dockerClient.tagImageCmd(imageName + ':' + imageTag, repository + '/' + destImageName, destImagetag).exec();
        logger.info("Tagged image to repository" + repository);
    }

    public void pushImageImpl(final String imageName, final String imageTag) {
        logger.info("Push an image" + imageName + "to registry");
        dockerClient.pushImageCmd(imageName + ":" + imageTag).exec(new PushImageResultCallback()).awaitSuccess();
        logger.info("Image pushed to registry");
    }

    public List<Image> listImageImpl() {
        logger.info("Getting image list from docker");
        final List<Image> listImagesResponse = dockerClient.listImagesCmd().exec();
        logger.info("Listing image responses : " + listImagesResponse);
        return listImagesResponse;
    }

    public InspectImageResponse buildImageImpl(final String dockerFilePath, final String imageName,
            final String imageTag) throws InterruptedException {
        logger.info("Docker File Path " + dockerFilePath);
        File dockerfile = new File(dockerFilePath);
        BuildImageCmd command = dockerClient.buildImageCmd().withDockerfile(dockerfile)
                .withTags(new HashSet<>(Arrays.asList(imageName + ":" + imageTag)));
        String response = command.withNoCache(true).exec(new BuildImageResultCallback()).awaitImageId();
        logger.info("Build image ID : " + response);
        InspectImageResponse imageResponse = dockerClient.inspectImageCmd(imageName + ":" + imageTag).exec();
        logger.info("Created image details : " + imageResponse);
        return imageResponse;
    }

    public InspectImageResponse inspectImageImpl(final String imageName, final String imageTag) {
        logger.info("Inspecting image " + imageName);
        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageName + ":" + imageTag).exec();
        logger.info("Inspect image response received.");
        logger.debug("Inspect image response : " + inspectImageResponse);
        return inspectImageResponse;
    }

    public void removeImageImpl(final String imageName, final String imageTag, final Boolean forceRemove) {
        logger.info("Removing Image " + imageName);
        dockerClient.removeImageCmd(imageName + ":" + imageTag).withForce(forceRemove).exec();
        logger.info(imageName + "with tag " + imageTag + " is removed");
    }

}
