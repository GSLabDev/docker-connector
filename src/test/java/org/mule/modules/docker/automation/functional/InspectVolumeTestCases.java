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

public class InspectVolumeTestCases extends AbstractTestCase<DockerConnector> {
    java.lang.String volumeName = "created-test";

    public InspectVolumeTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        getConnector().createVolume(volumeName, "local");
    }

    @After
    public void tearDown() {
        getConnector().removeVolume(volumeName);
    }

    @Test
    public void verify() {
        com.github.dockerjava.api.command.InspectVolumeResponse inspectVolumeResponse = getConnector()
                .inspectVolume(volumeName);
        assertNotNull(inspectVolumeResponse.getName());
        assertEquals(inspectVolumeResponse.getName(), volumeName);
        assertEquals(inspectVolumeResponse.getDriver(), "local");
    }

}