/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.ListVolumesResponse;
import com.github.dockerjava.api.exception.NotFoundException;

public class CreateVolumeTestCasesIT extends AbstractTestCase<DockerConnector> {

    com.github.dockerjava.api.command.CreateVolumeResponse createVolumeResponse = null;
    private static final Logger logger = LogManager.getLogger(CreateVolumeTestCasesIT.class.getName());

    public CreateVolumeTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            boolean danglingFilter = false;
            ListVolumesResponse volumes = getConnector().listVolume(danglingFilter);
            if (volumes.toString().contains(TestsConstants.CREATE_VOLUME_VOLUMENAME)) {
                getConnector().removeVolume(TestsConstants.CREATE_VOLUME_VOLUMENAME);
            }
        } catch (NotFoundException e) {
            logger.error("Ignoring exception", e);
        }
    }

    @After
    public void tearDown() {
        try {
            getConnector().removeVolume(TestsConstants.CREATE_VOLUME_VOLUMENAME);
        } catch (NotFoundException e) {
            logger.error("Ignoring exception", e);
        }
    }

    @Test
    public void verifyWithoutDriverOpts() {
        createVolumeResponse = getConnector().createVolume(TestsConstants.CREATE_VOLUME_VOLUMENAME, TestsConstants.CREATE_VOLUME_VOLUMEDRIVER, null);
        assertEquals(createVolumeResponse.getName(), TestsConstants.CREATE_VOLUME_VOLUMENAME);
        assertEquals(createVolumeResponse.getDriver(), TestsConstants.CREATE_VOLUME_VOLUMEDRIVER);
    }

    @Test
    public void verifyWithDriverOpts() {
        createVolumeResponse = getConnector().createVolume(TestsConstants.CREATE_VOLUME_VOLUMENAME, TestsConstants.CREATE_VOLUME_VOLUMEDRIVER,
                TestsConstants.CREATE_VOLUME_DRIVEROPTS);
        assertEquals(createVolumeResponse.getName(), TestsConstants.CREATE_VOLUME_VOLUMENAME);
        assertEquals(createVolumeResponse.getDriver(), TestsConstants.CREATE_VOLUME_VOLUMEDRIVER);
    }

}