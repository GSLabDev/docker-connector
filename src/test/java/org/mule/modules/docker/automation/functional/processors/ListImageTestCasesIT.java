/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.model.Info;

public class ListImageTestCasesIT extends AbstractTestCase<DockerConnector> {

    public ListImageTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {

        java.util.List<java.lang.String> cacheFromImage = new ArrayList<java.lang.String>();
        try {
            getConnector().buildImage(TestsConstants.LIST_IMAGE_DOCKERFILE_PATH, TestsConstants.LIST_IMAGE_IMAGE_TAGS, TestsConstants.LIST_IMAGE_CPUSET,
                    TestsConstants.LIST_IMAGE_CPUSHARES, TestsConstants.LIST_IMAGE_LABELS, TestsConstants.LIST_IMAGE_MEMORY, TestsConstants.LIST_IMAGE_MEMSWAP,
                    TestsConstants.LIST_IMAGE_BUILDARGUMENT_NAME, TestsConstants.LIST_IMAGE_BUILDARGUMENT_VALUE, cacheFromImage, TestsConstants.LIST_IMAGE_NOCACHE,
                    TestsConstants.LIST_IMAGE_FORCERM, TestsConstants.LIST_IMAGE_PULLIMAGE, TestsConstants.LIST_IMAGE_REMOVE_CONTAINERS, null);
        } catch (InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        String[] tag = TestsConstants.LIST_IMAGE_IMAGE_TAGS.iterator().next().split(":");
        getConnector().removeImage(tag[0], tag[1], true, false, null);
    }

    @Test
    public void verifyListImagesWithParameter() {
        java.util.List<com.github.dockerjava.api.model.Image> images = getConnector().listImage(true, false, null, null);
        assertNotNull(images);
        Info info = getConnector().dockerInfo();
        assertTrue(images.size() == info.getImages());
    }

    @Test
    public void verifyListImagesWithConfigParameter() {
        java.util.List<com.github.dockerjava.api.model.Image> images = getConnector().listImage(TestsConstants.LIST_IMAGE_SHOWALL, TestsConstants.LIST_IMAGE_DANGLING,
                TestsConstants.LIST_IMAGE_IMAGENAME_FILTER, TestsConstants.LIST_IMAGE_IMAGELABEL_FILTER);
        assertNotNull(images);
        assertTrue(images.size() > 0);
    }

}