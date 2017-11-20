/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.InspectImageResponse;

public class BuildImageTestCases extends AbstractTestCase<DockerConnector> {

    String dockerFilePath = "src/test/resources/Dockerfile", imageName = "created-test-build", imageTag = "test";

    public BuildImageTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
        try {
            getConnector().removeImage(imageName, imageTag, true);
        } catch (Exception e) {
        }
    }

    @Test
    public void verify() throws InterruptedException {

        InspectImageResponse response = getConnector().buildImage(dockerFilePath, imageName, imageTag);
        assertNotNull(response);
        assertNotNull(response.getId());
    }

}