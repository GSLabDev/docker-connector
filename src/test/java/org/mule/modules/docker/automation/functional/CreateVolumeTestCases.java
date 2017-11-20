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

public class CreateVolumeTestCases extends AbstractTestCase<DockerConnector> {

    com.github.dockerjava.api.command.CreateVolumeResponse createVolumeResponse = null;
    java.lang.String volumeName = "volume1";
    java.lang.String volumeDriver = "local";

    public CreateVolumeTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().removeVolume(volumeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        getConnector().removeVolume(volumeName);
    }

    @Test
    public void verify() {
        createVolumeResponse = getConnector().createVolume(volumeName, volumeDriver);
        assertEquals(createVolumeResponse.getName(), volumeName);
        assertEquals(createVolumeResponse.getDriver(), volumeDriver);
    }

}