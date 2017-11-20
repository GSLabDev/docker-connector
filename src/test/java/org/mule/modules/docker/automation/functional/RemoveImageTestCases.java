/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.model.Info;

public class RemoveImageTestCases extends AbstractTestCase<DockerConnector> {

    java.lang.String imageName = "busybox";
    java.lang.String imageTag = "latest";
    java.lang.Boolean forceRemove = true;
    Info info = null;

    public RemoveImageTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().pullImage(imageName, imageTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().pullImage(imageName, imageTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void verify() {
        info = getConnector().dockerInfo();
        int imageCount = info.getImages();
        getConnector().removeImage(imageName, imageTag, forceRemove);
        info = getConnector().dockerInfo();
        assertEquals(imageCount, info.getImages() + 1);
    }

}