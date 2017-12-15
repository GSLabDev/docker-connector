/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.model.Info;

public class RemoveImageTestCasesIT extends AbstractTestCase<DockerConnector> {

    Info info = null;

    public RemoveImageTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().pullImage(TestsConstants.REMOVE_IMAGE_IMAGENAME, TestsConstants.REMOVE_IMAGE_IMAGETAG, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void verifyWithImageName() {
        info = getConnector().dockerInfo();
        int imageCount = info.getImages();
        getConnector().removeImage(TestsConstants.REMOVE_IMAGE_IMAGENAME, TestsConstants.REMOVE_IMAGE_IMAGETAG, TestsConstants.REMOVE_IMAGE_FORCE_REMOVE, false, null);
        info = getConnector().dockerInfo();
        assertEquals(imageCount, info.getImages() + 1);
    }

    @Test
    public void verifyWithImageId() {
        info = getConnector().dockerInfo();
        String imageid = getConnector().inspectImage(TestsConstants.REMOVE_IMAGE_IMAGENAME, TestsConstants.REMOVE_IMAGE_IMAGETAG).getId();
        int imageCount = info.getImages();
        getConnector().removeImage(TestsConstants.REMOVE_IMAGE_IMAGENAME, TestsConstants.REMOVE_IMAGE_IMAGETAG, TestsConstants.REMOVE_IMAGE_FORCE_REMOVE, false, imageid);
        info = getConnector().dockerInfo();
        assertEquals(imageCount, info.getImages() + 1);
    }
}