/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.InspectImageResponse;

public class BuildImageTestCases extends AbstractTestCase<DockerConnector> {
    java.util.List<java.lang.String> imageTags = new ArrayList<java.lang.String>();

    public BuildImageTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        imageTags.add("create-build-image-test1:test");
        imageTags.add("create-build-image-test2:test");
    }

    @After
    public void tearDown() {
        try {
            Iterator<String> imageTagsList = imageTags.iterator();
            for (int i = 0; i < imageTags.size(); i++) {
                String[] tag = imageTagsList.next().split(":");
                getConnector().removeImage(tag[0], tag[1], true, false, null);
            }
        } catch (Exception e) {
        }
    }

    @Test
    public void verifyDefault() throws InterruptedException, URISyntaxException {
        java.lang.String dockerFilePath = "src/test/resources/Dockerfile", cpuSet = null, cpuShares = null,
                buildArgumetName = null, buildArgumetValue = null, remoteURI = null;

        java.util.Map<java.lang.String, java.lang.String> labels = null;
        long memory = 0, memswap = 0;
        java.util.List<java.lang.String> cacheFromImage = new ArrayList<java.lang.String>();
        java.lang.Boolean noCache = true, forcerm = true, pullImage = false, removeContainers = true;

        InspectImageResponse buildResponse = getConnector().buildImage(dockerFilePath, imageTags, cpuSet, cpuShares,
                labels, memory, memswap, buildArgumetName, buildArgumetValue, cacheFromImage, noCache, forcerm,
                pullImage, removeContainers, remoteURI);
        assertNotNull(buildResponse);
        buildResponse.getId();
        assertNotNull(buildResponse.getId());
    }

    @Test
    public void verifyWithAll() throws InterruptedException, URISyntaxException {
        java.lang.String dockerFilePath = "src/test/resources/Dockerfile", cpuSet = "0", cpuShares = "40",
                buildArgumetName = "HTTP_PROXY", buildArgumetValue = "http://localhost:5000", remoteURI = "";

        java.util.Map<java.lang.String, java.lang.String> labels = new HashMap<String, String>();
        labels.put("TestKay1", "TestValue1");
        labels.put("TestKay2", "TestValue2");
        long memory = 5000000, memswap = 6000000;
        java.util.List<java.lang.String> cacheFromImage = new ArrayList<java.lang.String>();
        cacheFromImage.add("ubuntu:latest");
        java.lang.Boolean noCache = false, forcerm = true, pullImage = true, removeContainers = true;

        InspectImageResponse buildImageResponse = getConnector().buildImage(dockerFilePath, imageTags, cpuSet,
                cpuShares, labels, memory, memswap, buildArgumetName, buildArgumetValue, null, noCache, forcerm,
                pullImage, removeContainers, remoteURI);
        assertTrue(buildImageResponse.getRepoTags().size() == 2);
        assertTrue(buildImageResponse.toString().contains(labels.get("TestKay1").toString()));
        assertNotNull(buildImageResponse);
        assertNotNull(buildImageResponse.getId());
    }

}