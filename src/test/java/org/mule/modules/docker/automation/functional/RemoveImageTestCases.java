/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.model.Info;

public class RemoveImageTestCases extends AbstractTestCase<DockerConnector> {

    java.lang.String imageName = "busybox";
    java.lang.String imageTag = "latest";
    java.lang.Boolean forceRemove = true;
    java.lang.Boolean prune = false;
    Info info = null;

    public RemoveImageTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().pullImage(imageName, imageTag, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void verifyWithImageName() {
        info = getConnector().dockerInfo();
        int imageCount = info.getImages();
        getConnector().removeImage(imageName, imageTag, forceRemove, prune, null);
        info = getConnector().dockerInfo();
        assertEquals(imageCount, info.getImages() + 1);
    }

    @Test
    public void verifyWithImageId() {
        info = getConnector().dockerInfo();
        String imageid = getConnector().inspectImage(imageName, imageTag).getId();
        int imageCount = info.getImages();
        getConnector().removeImage(imageName, imageTag, forceRemove, prune, imageid);
        info = getConnector().dockerInfo();
        assertEquals(imageCount, info.getImages() + 1);
    }
}