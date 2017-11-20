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

public class InspectImageTestCases extends AbstractTestCase<DockerConnector> {

    java.lang.String imageName = "busybox";
    java.lang.String imageTag = "latest";

    public InspectImageTestCases() {
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
        com.github.dockerjava.api.command.InspectImageResponse inspectImageResponse = getConnector()
                .inspectImage(imageName, imageTag);
        assertNotNull(inspectImageResponse.getId());
        assertNotNull(inspectImageResponse.getRepoTags());
    }

}