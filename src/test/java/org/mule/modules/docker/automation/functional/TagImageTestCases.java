/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.exception.NotFoundException;

public class TagImageTestCases extends AbstractTestCase<DockerConnector> {

    String imageName = "busybox";
    String imageTag = "latest";
    String destImageName = "test-tag-image";
    String repository = "localhost:5000";
    String destImagetag = "test";
    java.util.List<com.github.dockerjava.api.model.Image> images = null;

    public TagImageTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().pullImage(imageName, imageTag);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void verify() {
        getConnector().tagImage(imageName, imageTag, destImageName, repository, destImagetag);
        getConnector().removeImage(repository + "/" + destImageName, destImagetag, true);

    }

    @Test(expected = NotFoundException.class)
    public void tagNonExistingImage() throws Exception {

        String tag = "" + RandomUtils.nextInt(Integer.MAX_VALUE);
        getConnector().tagImage("non-existing", tag, "test", "localhost:5000", tag);
    }

}