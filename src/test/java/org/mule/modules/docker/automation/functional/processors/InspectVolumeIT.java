/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class InspectVolumeTestCasesIT extends AbstractTestCase<DockerConnector> {

    public InspectVolumeTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        getConnector().createVolume(TestsConstants.INSPECT_VOLUME_VOLUME_NAME, TestsConstants.INSPECT_VOLUME_VOLUMEDRIVER, TestsConstants.INSPECT_VOLUME_DRIVEROPTS);
    }

    @After
    public void tearDown() {
        getConnector().removeVolume(TestsConstants.INSPECT_VOLUME_VOLUME_NAME);
    }

    @Test
    public void verify() {
        com.github.dockerjava.api.command.InspectVolumeResponse inspectVolumeResponse = getConnector().inspectVolume(TestsConstants.INSPECT_VOLUME_VOLUME_NAME);
        assertNotNull(inspectVolumeResponse.getName());
        assertEquals(inspectVolumeResponse.getName(), TestsConstants.INSPECT_VOLUME_VOLUME_NAME);
        assertEquals(inspectVolumeResponse.getDriver(), TestsConstants.INSPECT_VOLUME_VOLUMEDRIVER);
    }

}