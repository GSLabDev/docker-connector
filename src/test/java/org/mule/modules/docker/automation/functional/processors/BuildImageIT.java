/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.InspectImageResponse;

public class BuildImageTestCasesIT extends AbstractTestCase<DockerConnector> {

    List<String> imageTags = new ArrayList<String>();

    public BuildImageTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        imageTags.add("create-build-image-test1:test");
        imageTags.add("create-build-image-test2:test");
    }

    @After
    public void tearDown() {
        try {
            for (String tags : imageTags) {
                String[] tag = tags.split(":");
                getConnector().removeImage(tag[0], tag[1], true, false, null);
            }
        } catch (Exception e) {
        }
    }

    @Test
    public void verifyDefault() throws InterruptedException, URISyntaxException {

        List<String> cacheFromImage = new ArrayList<String>();
        InspectImageResponse buildResponse = getConnector().buildImage(TestsConstants.BUILD_IMAGE_DOCKERFILE_PATH, imageTags, null, null, null, 0, 0, null, null, cacheFromImage,
                true, true, false, true, null);
        assertNotNull(buildResponse);
        buildResponse.getId();
        assertNotNull(buildResponse.getId());
    }

    @Test
    public void verifyWithAll() throws InterruptedException, URISyntaxException {

        InspectImageResponse buildImageResponse = getConnector().buildImage(TestsConstants.BUILD_IMAGE_DOCKERFILE_PATH, imageTags, TestsConstants.BUILD_IMAGE_CPUSET,
                TestsConstants.BUILD_IMAGE_CPUSHARES, TestsConstants.BUILD_IMAGE_LABELS, TestsConstants.BUILD_IMAGE_MEMORY, TestsConstants.BUILD_IMAGE_MEMSWAP,
                TestsConstants.BUILD_IMAGE_BUILDARGUMET_NAME, TestsConstants.BUILD_IMAGE_BUILDARGUMET_VALUE, null, TestsConstants.BUILD_IMAGE_NOCACHE,
                TestsConstants.BUILD_IMAGE_FORCERM, TestsConstants.BUILD_IMAGE_PULLIMAGE, TestsConstants.BUILD_IMAGE_REMOVE_CONTAINERS, TestsConstants.BUILD_IMAGE_REMOTEURI);
        assertTrue(buildImageResponse.getRepoTags().size() == 2);
        assertTrue(buildImageResponse.toString().contains(TestsConstants.BUILD_IMAGE_LABELS.get("TestKey1").toString()));
        assertNotNull(buildImageResponse);
        assertNotNull(buildImageResponse.getId());
    }

}