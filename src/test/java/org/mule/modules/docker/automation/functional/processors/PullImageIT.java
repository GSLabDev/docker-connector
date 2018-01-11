/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Info;

public class PullImageIT extends AbstractTestCase<DockerConnector> {

    Info info = null;

    public PullImageIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().removeImage(TestsConstants.PULL_IMAGE_TESTIMAGE, TestsConstants.PULL_IMAGE_IMAGETAG, true, false, null);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        info = getConnector().dockerInfo();
    }

    @Test
    public void verifyPullImage() throws InterruptedException {

        int imgCount = info.getImages();
        getConnector().pullImage(TestsConstants.PULL_IMAGE_TESTIMAGE, TestsConstants.PULL_IMAGE_IMAGETAG, TestsConstants.PULL_IMAGE_USERNAME, TestsConstants.PULL_IMAGE_PASSWORD);
        info = getConnector().dockerInfo();
        assertEquals(imgCount, (info.getImages() - 1));

        InspectImageResponse inspectImageResponse = getConnector().inspectImage(TestsConstants.PULL_IMAGE_TESTIMAGE, TestsConstants.PULL_IMAGE_IMAGETAG);
        assertNotNull(inspectImageResponse);

    }

}