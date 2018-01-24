/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;

public class ListImageIT extends AbstractTestCase<DockerConnector> {

    public ListImageIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {

        try {
			getConnector().pullImage(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, null, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void verifyListImagesWithParameter() {
        List<Image> images = getConnector().listImage(true, false, null, null);
        assertNotNull(images);
        Info info = getConnector().dockerInfo();
        assertTrue(images.size() == info.getImages());
    }

    @Test
    public void verifyListImagesWithConfigParameter() {
        List<Image> images = getConnector().listImage(TestsConstants.LIST_IMAGE_SHOWALL, TestsConstants.LIST_IMAGE_DANGLING, TestsConstants.LIST_IMAGE_IMAGENAME_FILTER,
                TestsConstants.LIST_IMAGE_IMAGELABEL_FILTER);
        assertNotNull(images);
    }

}