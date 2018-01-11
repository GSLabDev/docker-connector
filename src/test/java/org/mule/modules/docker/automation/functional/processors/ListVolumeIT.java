/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.command.ListVolumesResponse;

public class ListVolumeTestCasesIT extends AbstractTestCase<DockerConnector> {

    CreateVolumeResponse createVolumeResponse = null;

    public ListVolumeTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        createVolumeResponse = getConnector().createVolume(TestsConstants.LIST_VOLUME_VOLUME_NAME, TestsConstants.LIST_VOLUME_VOLUMEDRIVER, TestsConstants.LIST_VOLUME_DRIVEROPTS);

    }

    @After
    public void tearDown() {
        getConnector().removeVolume(createVolumeResponse.getName());
    }

    @Test
    public void verifyListVolume() {
        assertTrue(createVolumeResponse.getName().equals(TestsConstants.LIST_VOLUME_VOLUME_NAME));
        assertTrue(createVolumeResponse.getDriver().equals(TestsConstants.LIST_VOLUME_VOLUMEDRIVER));
        ListVolumesResponse listVolumesResponse = getConnector().listVolume(TestsConstants.LIST_VOLUME_DANGLING_FILTER);
        assertTrue(listVolumesResponse.getVolumes().size() >= 1);
    }

}