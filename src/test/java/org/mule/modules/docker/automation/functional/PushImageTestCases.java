/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class PushImageTestCases extends AbstractTestCase<DockerConnector> {
    String imageName = "busybox";
    String imageTag = "latest";
    String destImageName = "test-tag-image";
    String repository = "localhost:5000";
    String destImagetag = "test";

    public PushImageTestCases() {
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
        getConnector().removeImage(repository + "/" + destImageName, destImagetag, true);
    }

    @Test
    public void verify() throws InterruptedException {
        getConnector().tagImage(imageName, imageTag, destImageName, repository, destImagetag);
        getConnector().pushImage(repository + "/" + destImageName, destImagetag);
        getConnector().removeImage(imageName, imageTag, true);
        getConnector().pullImage(repository + "/" + destImageName, destImagetag);
    }

}