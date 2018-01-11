/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.exception.NotFoundException;

public class TagImageIT extends AbstractTestCase<DockerConnector> {

    java.util.List<com.github.dockerjava.api.model.Image> images = null;

    public TagImageIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().pullImage(TestsConstants.TAG_IMAGE_NAME, TestsConstants.TAG_IMAGE_TAG, null, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void verifyImageTag() {
//        getConnector().tagImage(imageName, imageTag, destImageName, repository, destImagetag);
        getConnector().tagImage(TestsConstants.TAG_IMAGE_NAME, TestsConstants.TAG_IMAGE_TAG, TestsConstants.TAG_IMAGE_DEST_IMAGE_NAME, TestsConstants.TAG_IMAGE_REPOSITORY,
                TestsConstants.TAG_IMAGE_DEST_IMAGE_TAG);
        System.out.println(getConnector().listImage(true, false, null, null));
        assertTrue(getConnector().listImage(true, false, null, null).toString().contains(TestsConstants.TAG_IMAGE_DEST_IMAGE_NAME));
        getConnector().removeImage(TestsConstants.TAG_IMAGE_REPOSITORY + "/" + TestsConstants.TAG_IMAGE_DEST_IMAGE_NAME, TestsConstants.TAG_IMAGE_DEST_IMAGE_TAG, true, false,
                null);
    }

    @Test(expected = NotFoundException.class)
    public void tagNonExistingImage() throws Exception {

        String tag = "" + RandomUtils.nextInt(Integer.MAX_VALUE);
        getConnector().tagImage("non-existing", tag, "test", "localhost:5000", tag);
    }

}