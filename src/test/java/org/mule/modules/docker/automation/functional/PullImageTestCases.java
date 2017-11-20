/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Info;

public class PullImageTestCases extends AbstractTestCase<DockerConnector> {

    String testImage = "hackmann/empty";
    Info info = null;

    public PullImageTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().removeImage(testImage, "latest", true);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        info = getConnector().dockerInfo();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void verify() throws InterruptedException {

        int imgCount = info.getImages();
        getConnector().pullImage(testImage, "latest");
        info = getConnector().dockerInfo();
        assertEquals(imgCount, (info.getImages() - 1));

        InspectImageResponse inspectImageResponse = getConnector().inspectImage(testImage, "latest");
        assertNotNull(inspectImageResponse);

    }

}