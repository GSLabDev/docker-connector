/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.model.Info;

public class ListImageTestCases extends AbstractTestCase<DockerConnector> {    
    public ListImageTestCases() {
        super(DockerConnector.class);
    }
    java.util.List<java.lang.String> imageTags = new ArrayList<java.lang.String>();
    java.util.Map<java.lang.String, java.lang.String> labels = new HashMap<java.lang.String, java.lang.String>();

    @Before
    public void setup() {
        imageTags.add("created-list-image-test:latest");
        java.lang.String dockerFilePath = "src/test/resources/Dockerfile", cpuSet = null, cpuShares = null,
                buildArgumetName = null, buildArgumetValue = null;

        labels.put("TestLabel", "label1");
        labels.put("TestLabel2", "label2");
        long memory = 0, memswap = 0;
        java.util.List<java.lang.String> cacheFromImage = new ArrayList<java.lang.String>();
        java.lang.Boolean noCache = true, forcerm = true, pullImage = false, removeContainers = true;
        
        try {
            getConnector().buildImage(dockerFilePath, imageTags, cpuSet, cpuShares,
                    labels, memory, memswap, buildArgumetName, buildArgumetValue, cacheFromImage, noCache, forcerm,
                    pullImage, removeContainers, null);
        } catch (InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        String[] tag = imageTags.iterator().next().split(":");
        getConnector().removeImage(tag[0], tag[1] , true, false, null);
    }

    @Test
    public void verifyDefault() {
        
        boolean showAll = true, dangling = false;
        String imageNameFilter =null, imageLabelFilter = null;
        java.util.List<com.github.dockerjava.api.model.Image> images = getConnector().listImage(showAll,dangling,imageNameFilter,imageLabelFilter);
        assertNotNull(images);
        Info info = getConnector().dockerInfo();
        assertThat(images.size(), equalTo(info.getImages()));
    }
    
    @Test
    public void verifyWithAll() {
        boolean showAll = false, dangling = false;
        String imageNameFilter = imageTags.iterator().next(), imageLabelFilter = "TestLabel";
        java.util.List<com.github.dockerjava.api.model.Image> images = getConnector().listImage(showAll, dangling, imageNameFilter, imageLabelFilter);
        assertNotNull(images);
        assertTrue(images.size() == 1);
    }

}