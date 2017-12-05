/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.ListVolumesResponse;
import com.github.dockerjava.api.exception.NotFoundException;

public class CreateVolumeTestCases extends AbstractTestCase<DockerConnector> {

    com.github.dockerjava.api.command.CreateVolumeResponse createVolumeResponse = null;
    private static final Logger logger = LogManager.getLogger(CreateVolumeTestCases.class.getName());
    java.lang.String volumeName = "volume1", volumeDriver = "local";
    Map<String, String> driverOpts = new HashMap<String, String>();

    public CreateVolumeTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            boolean danglingFilter = false;
            ListVolumesResponse volumes = getConnector().listVolume(danglingFilter);
            if (volumes.toString().contains(volumeName)) {
                getConnector().removeVolume(volumeName);
            }
        } catch (NotFoundException e) {
            logger.error("Ignoring exception", e);
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().removeVolume(volumeName);
        } catch (NotFoundException e) {
            logger.error("Ignoring exception", e);
        }
    }

    @Test
    public void verifyDefault() {
        createVolumeResponse = getConnector().createVolume(volumeName, volumeDriver, null);
        assertEquals(createVolumeResponse.getName(), volumeName);
        assertEquals(createVolumeResponse.getDriver(), volumeDriver);
    }

    @Test
    public void verifyWithAll() {
        driverOpts.put("type", "tmpfs");
        createVolumeResponse = getConnector().createVolume(volumeName, volumeDriver, driverOpts);
        assertEquals(createVolumeResponse.getName(), volumeName);
        assertEquals(createVolumeResponse.getDriver(), volumeDriver);
    }

}