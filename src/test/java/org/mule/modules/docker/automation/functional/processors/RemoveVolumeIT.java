/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.exception.NotFoundException;

public class RemoveVolumeIT extends AbstractTestCase<DockerConnector> {

    CreateVolumeResponse createVolumeResponse = null;

    public RemoveVolumeIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {

        createVolumeResponse = getConnector().createVolume(TestsConstants.CREATE_VOLUME_VOLUMENAME, TestsConstants.CREATE_VOLUME_VOLUMEDRIVER,
                TestsConstants.CREATE_VOLUME_DRIVEROPTS);

    }

    @Test(expected = NotFoundException.class)
    public void verifyRemoveVolume() {
        assertEquals(createVolumeResponse.getName(), TestsConstants.CREATE_VOLUME_VOLUMENAME);
        getConnector().removeVolume(TestsConstants.CREATE_VOLUME_VOLUMENAME);
        getConnector().inspectVolume(TestsConstants.CREATE_VOLUME_VOLUMENAME);

    }

}