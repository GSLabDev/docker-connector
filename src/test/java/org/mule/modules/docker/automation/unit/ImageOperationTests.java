package org.mule.modules.docker.automation.unit;

import static org.junit.Assert.assertNotNull;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mule.modules.docker.DockerImageOperations;
import org.mule.modules.docker.automation.util.TestsConstants;

import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Image;

public class ImageOperationTests extends DockerConnectorAbstractTestCase {

    DockerImageOperations dockerImageOperations;
    java.util.List<java.lang.String> imageTags = new ArrayList<java.lang.String>();

    @Before
    public void setup() {
        dockerImageOperations = new DockerImageOperations(mockDockerClient);

        imageTags.add("create-build-image-test1:test");
        imageTags.add("create-build-image-test2:test");
    }

    @Test
    public void verifyBuildImageDefault() throws InterruptedException, URISyntaxException {

        java.util.List<java.lang.String> cacheFromImage = new ArrayList<java.lang.String>();

        InspectImageResponse buildResponse = dockerImageOperations.buildImageImpl(
                TestsConstants.BUILD_IMAGE_DOCKERFILE_PATH, imageTags, null, null, cacheFromImage, null, null, null, 0,
                0, true, true, false, true, "");
        Mockito.verify(mockBuildImageCmd, Mockito.times(0)).withCpushares(null);
        Mockito.verify(mockBuildImageCmd, Mockito.times(0)).withLabels(null);
        Mockito.verify(mockBuildImageCmd, Mockito.times(0)).withMemory((long) 0);
        Mockito.verify(mockBuildImageCmd, Mockito.times(0)).withMemswap((long) 0);
        assertNotNull(buildResponse);
        buildResponse.getId();
        assertNotNull(buildResponse.getId());
    }

    @Test
    public void verifyBuildImageWithParams() throws InterruptedException, URISyntaxException {
        dockerImageOperations.buildImageImpl(TestsConstants.BUILD_IMAGE_DOCKERFILE_PATH, imageTags,
                TestsConstants.BUILD_IMAGE_BUILDARGUMET_NAME, TestsConstants.BUILD_IMAGE_BUILDARGUMET_VALUE,
                TestsConstants.BUILD_IMAGE_CACHE_FROM_IMAGE, TestsConstants.BUILD_IMAGE_CPUSET,
                TestsConstants.BUILD_IMAGE_CPUSHARES, TestsConstants.BUILD_IMAGE_LABELS,
                TestsConstants.BUILD_IMAGE_MEMORY, TestsConstants.BUILD_IMAGE_MEMSWAP,
                TestsConstants.BUILD_IMAGE_FORCERM, TestsConstants.BUILD_IMAGE_NOCACHE,
                TestsConstants.BUILD_IMAGE_PULLIMAGE, TestsConstants.BUILD_IMAGE_REMOVE_CONTAINERS,
                TestsConstants.BUILD_IMAGE_REMOTEURI);
        Mockito.verify(mockBuildImageCmd).withCpushares(TestsConstants.BUILD_IMAGE_CPUSHARES);
        Mockito.verify(mockBuildImageCmd).withLabels(TestsConstants.BUILD_IMAGE_LABELS);
        Mockito.verify(mockBuildImageCmd).withMemory(TestsConstants.BUILD_IMAGE_MEMORY);
        Mockito.verify(mockBuildImageCmd).withMemswap(TestsConstants.BUILD_IMAGE_MEMSWAP);
    }

    @Test
    public void verifyInspectImage() {
        dockerImageOperations.inspectImageImpl(TestsConstants.INSPECT_IMAGE_IMAGENAME,
                TestsConstants.INSPECT_IMAGE_IMAGETAG);
        Mockito.verify(mockDockerClient)
                .inspectImageCmd(TestsConstants.INSPECT_IMAGE_IMAGENAME + ":" + TestsConstants.INSPECT_IMAGE_IMAGETAG);
        assertNotNull(dockerImageOperations
                .inspectImageImpl(TestsConstants.INSPECT_IMAGE_IMAGENAME, TestsConstants.INSPECT_IMAGE_IMAGETAG)
                .getId());
        assertNotNull(dockerImageOperations
                .inspectImageImpl(TestsConstants.INSPECT_IMAGE_IMAGENAME, TestsConstants.INSPECT_IMAGE_IMAGETAG)
                .getRepoTags());
    }

    @Test
    public void verifyListImage() {
        List<Image> listImageResponse = dockerImageOperations.listImageImpl(true, false,
                TestsConstants.LIST_IMAGE_IMAGENAME_FILTER, TestsConstants.LIST_IMAGE_IMAGELABEL_FILTER);
        assertNotNull(listImageResponse);
        Mockito.verify(mockListImgCmd, Mockito.times(0)).withDanglingFilter(false);
        Mockito.verify(mockListImgCmd, Mockito.times(1))
                .withImageNameFilter(TestsConstants.LIST_IMAGE_IMAGENAME_FILTER);
        Mockito.verify(mockListImgCmd, Mockito.times(1)).withLabelFilter(TestsConstants.LIST_IMAGE_IMAGELABEL_FILTER);
        Mockito.verify(mockListImgCmd, Mockito.times(1)).withShowAll(true);
    }

    @Test
    public void verifyPullImageWithAuthConfig() throws InterruptedException {
        AuthConfig authConfig = new AuthConfig();
        authConfig.withUsername(TestsConstants.PUSH_IMAGE_USERNAME);
        authConfig.withPassword(TestsConstants.PUSH_IMAGE_PASSWORD);
        InspectImageResponse inspectImageResp = dockerImageOperations.pullImageImpl(TestsConstants.PULL_IMAGE_TESTIMAGE,
                TestsConstants.PULL_IMAGE_IMAGETAG, TestsConstants.PUSH_IMAGE_USERNAME,
                TestsConstants.PUSH_IMAGE_PASSWORD);
        assertNotNull(inspectImageResp);
        Mockito.verify(mockPullImgCmd).withAuthConfig(authConfig);
    }

    @Test
    public void verifyPullImageWithoutAuthConfig() throws InterruptedException {
        InspectImageResponse inspectImageResp = dockerImageOperations.pullImageImpl(TestsConstants.PULL_IMAGE_TESTIMAGE,
                TestsConstants.PULL_IMAGE_IMAGETAG, null, null);
        assertNotNull(inspectImageResp);
        Mockito.verify(mockPullImgCmd, Mockito.times(0)).withAuthConfig(Mockito.any(AuthConfig.class));
    }

    @Test
    public void verifyPushImage() throws InterruptedException {
        AuthConfig authConfig = new AuthConfig();
        dockerImageOperations.pushImageImpl(
                TestsConstants.PUSH_IMAGE_REPOSITORY + "/" + TestsConstants.PUSH_IMAGE_DEST_IMAGE_NAME,
                TestsConstants.PUSH_IMAGE_DEST_IMAGE_TAG, null, null);
        Mockito.verify(mockPushImgCmd, Mockito.times(0)).withAuthConfig(authConfig);
        Mockito.verify(mockPushImgCmd, Mockito.times(0))
                .withName(TestsConstants.PUSH_IMAGE_REPOSITORY + "/" + TestsConstants.PUSH_IMAGE_DEST_IMAGE_NAME);
        Mockito.verify(mockPushImgCmd, Mockito.times(0)).withTag("latest");
    }

    @Test
    public void verifyPushImageWithWrongUsernamePassword() throws InterruptedException {
        AuthConfig authConfig = new AuthConfig();
        authConfig.withUsername(TestsConstants.PUSH_IMAGE_USERNAME);
        authConfig.withPassword(TestsConstants.PUSH_IMAGE_PASSWORD);
        dockerImageOperations.pushImageImpl(
                TestsConstants.PUSH_IMAGE_REPOSITORY + "/" + TestsConstants.PUSH_IMAGE_DEST_IMAGE_NAME,
                TestsConstants.PUSH_IMAGE_DEST_IMAGE_TAG, TestsConstants.PUSH_IMAGE_USERNAME,
                TestsConstants.PUSH_IMAGE_PASSWORD);
        Mockito.verify(mockPushImgCmd, Mockito.times(1)).withAuthConfig(authConfig);
        Mockito.verify(mockPushImgCmd, Mockito.times(0))
                .withName(TestsConstants.PUSH_IMAGE_REPOSITORY + "/" + TestsConstants.PUSH_IMAGE_DEST_IMAGE_NAME);
    }

    @Test
    public void verifyRemoveImageWithImageId() {
        dockerImageOperations.removeImageImpl(null, TestsConstants.REMOVE_IMAGE_IMAGETAG,
                TestsConstants.REMOVE_IMAGE_FORCE_REMOVE, true, TestsConstants.REMOVE_IMAGE_IMAGEID);
        Mockito.verify(mockDockerClient).removeImageCmd(TestsConstants.REMOVE_IMAGE_IMAGEID);
        Mockito.verify(mockRemoveImgCmd).withForce(true);
        Mockito.verify(mockRemoveImgCmd).withNoPrune(true);
    }

    @Test
    public void verifyRemoveImageWithImageName() {
        dockerImageOperations.removeImageImpl(TestsConstants.REMOVE_IMAGE_IMAGENAME,
                TestsConstants.REMOVE_IMAGE_IMAGETAG, TestsConstants.REMOVE_IMAGE_FORCE_REMOVE, true, null);
        Mockito.verify(mockDockerClient, Mockito.times(0)).removeImageCmd(TestsConstants.REMOVE_IMAGE_IMAGEID);
        Mockito.verify(mockRemoveImgCmd).withForce(true);
        Mockito.verify(mockRemoveImgCmd).withNoPrune(true);
    }

    @Test
    public void verifyTagImage() throws Exception {
        dockerImageOperations.tagImageImpl(TestsConstants.TAG_IMAGE_NAME, TestsConstants.TAG_IMAGE_TAG,
                TestsConstants.TAG_IMAGE_DEST_IMAGE_NAME, TestsConstants.TAG_IMAGE_REPOSITORY,
                TestsConstants.TAG_IMAGE_DEST_IMAGE_TAG);
        Mockito.verify(mockDockerClient).tagImageCmd(TestsConstants.TAG_IMAGE_NAME + ':' + TestsConstants.TAG_IMAGE_TAG,
                TestsConstants.TAG_IMAGE_REPOSITORY + '/' + TestsConstants.TAG_IMAGE_DEST_IMAGE_NAME,
                TestsConstants.TAG_IMAGE_DEST_IMAGE_TAG);
    }

    @Test(expected = NullPointerException.class)
    public void tagNonExistingImage() throws Exception {
        dockerImageOperations.tagImageImpl(null, null, TestsConstants.TAG_IMAGE_DEST_IMAGE_NAME,
                TestsConstants.TAG_IMAGE_REPOSITORY, null);

    }
}
