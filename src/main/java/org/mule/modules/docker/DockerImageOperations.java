/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;

/**
 * @author Great Software Laboratory Pvt. Ltd.
 */

public class DockerImageOperations {

    /**
     * Logging object
     */
    private static final Logger logger = LogManager.getLogger(DockerImageOperations.class.getName());

    private DockerClient dockerClient;

    public DockerImageOperations(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
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
    public InspectImageResponse pullImageImpl(final String imageName, final String imageTag, final String username,
            final String password) throws InterruptedException {
        logger.info("Please wait......... Pulling docker image " + imageName);
        final PullImageResultCallback pullImageCallback = new PullImageResultCallback();
        if (username != null && password != null) {
            AuthConfig authConfig = new AuthConfig();
            authConfig.withUsername(username);
            authConfig.withPassword(password);
            dockerClient.pullImageCmd(imageName).withTag(imageTag).withAuthConfig(authConfig).exec(pullImageCallback)
                    .awaitSuccess();
        } else {
            dockerClient.pullImageCmd(imageName + ":" + imageTag).exec(pullImageCallback).awaitSuccess();
        }
        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageName + ":" + imageTag).exec();
        logger.info("Inspect image response for pull image:" + inspectImageResponse);
        return inspectImageResponse;
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
    public void tagImageImpl(final String imageName, final String imageTag, final String destImageName,
            final String repository, final String destImagetag) {
        logger.info("Tagging image " + imageName);
        dockerClient.tagImageCmd(imageName + ':' + imageTag, repository + '/' + destImageName, destImagetag).exec();
        logger.info("Tagged image to repository" + repository);
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
    public void pushImageImpl(final String imageName, final String imageTag, final String username,
            final String password) {
        logger.info("Push an image" + imageName + "to registry");
        if (username != null && password != null) {
            AuthConfig authConfig = new AuthConfig();
            authConfig.withUsername(username);
            authConfig.withPassword(password);
            dockerClient.pushImageCmd(imageName).withTag(imageTag).withAuthConfig(authConfig)
                    .exec(new PushImageResultCallback()).awaitSuccess();
        } else {
            dockerClient.pushImageCmd(imageName + ":" + imageTag).exec(new PushImageResultCallback()).awaitSuccess();
        }
        logger.info("Image pushed to registry");
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
    public List<Image> listImageImpl(boolean showAll, boolean dangling, String imageNameFilter,
            String imageLabelFilter) {

        final List<Image> listImagesResponse;
        logger.info("Getting image list from docker");
        ListImagesCmd listImage = dockerClient.listImagesCmd();

        if (imageNameFilter != null) {
            logger.debug("List image with image name filter: " + imageNameFilter);

            listImage.withImageNameFilter(imageNameFilter);
        }
        if (imageLabelFilter != null) {
            logger.debug("List image with image label filter: " + imageLabelFilter);
            listImage.withLabelFilter(imageLabelFilter);
        }
        if (showAll) {
            logger.debug("List image with showAll flag set to : " + showAll);
            listImage.withShowAll(showAll);

        }
        if (dangling) {
            logger.debug("List image with dangling flag set to : " + dangling);
            listImage.withDanglingFilter(dangling);
        }
        listImagesResponse = listImage.exec();
        logger.info("List image response received !");
        return listImagesResponse;
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
    public InspectImageResponse buildImageImpl(final String dockerFilePath, final List<String> imageTags,
            final String buildArgumetName, final String buildArgumetValue, final List<String> cacheFromImage,
            final String cpuSet, final String cpuShares, final Map<String, String> labels, final long memory,
            final long memswap, final Boolean forcerm, final Boolean noCache, final Boolean pullImage,
            final Boolean removeContainers, final String remoteURI) throws InterruptedException, URISyntaxException {

        logger.info("Docker File Path " + dockerFilePath);
        File dockerfile = new File(dockerFilePath);
        BuildImageCmd buildImageCommand = dockerClient.buildImageCmd().withDockerfile(dockerfile)
                .withTags(new HashSet<String>(imageTags)).withForcerm(forcerm).withNoCache(noCache).withPull(pullImage)
                .withRemove(removeContainers);

        if (buildArgumetName != null) {
            buildImageCommand.withBuildArg(buildArgumetName, buildArgumetValue);
        }

        if (cacheFromImage != null) {
            buildImageCommand.withCacheFrom(new HashSet<String>(cacheFromImage));
        }

        if (cpuSet != null) {
            buildImageCommand.withCpusetcpus(cpuSet);
        }
        if (cpuShares != null) {
            buildImageCommand.withCpushares(cpuShares);
        }
        if (labels != null) {
            buildImageCommand.withLabels(labels);
        }
        if (remoteURI != null) {
            buildImageCommand.withRemote(new URI(remoteURI));
        }
        if (memory != 0) {
            buildImageCommand.withMemory(memory);
        }
        if (memswap != 0) {
            buildImageCommand.withMemswap(memswap);
        }
        logger.info("Creating image");
        String buildImageResponse = buildImageCommand.exec(new BuildImageResultCallback()).awaitImageId();
        logger.info("Build image ID : " + buildImageResponse);
        String tag = imageTags.iterator().next();
        InspectImageResponse imageResponse = dockerClient.inspectImageCmd(tag).exec();
        logger.info("Created image details : " + imageResponse);
        return imageResponse;
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
    public InspectImageResponse inspectImageImpl(final String imageName, final String imageTag) {
        logger.info("Inspecting image " + imageName);
        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageName + ":" + imageTag).exec();
        logger.info("Inspect image response received.");
        logger.debug("Inspect image response : " + inspectImageResponse);
        return inspectImageResponse;
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
     * @param imageid
     *            Image ID of the docker image that needs to be removed
     */
    public void removeImageImpl(final String imageName, final String imageTag, final Boolean forceRemove,
            final Boolean prune, final String imageid) {
        logger.info("Removing Image " + imageName);
        if (imageName != null && imageTag != null) {
            dockerClient.removeImageCmd(imageName + ":" + imageTag).withForce(forceRemove).withNoPrune(prune).exec();
            logger.info(imageName + " with tag " + imageTag + " is removed");
        } else if (imageid != null) {
            dockerClient.removeImageCmd(imageid).withForce(forceRemove).withNoPrune(prune).exec();
            logger.info("Image with id " + imageid + " is removed");
        }

    }

}
