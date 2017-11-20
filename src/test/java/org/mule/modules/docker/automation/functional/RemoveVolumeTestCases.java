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

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.exception.NotFoundException;

public class RemoveVolumeTestCases extends AbstractTestCase<DockerConnector> {

    String volumeName = "created-test-remove";
    CreateVolumeResponse createVolumeResponse = null;

    public RemoveVolumeTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        createVolumeResponse = getConnector().createVolume(volumeName, "local");

    }

    @After
    public void tearDown() {
    }

    @Test(expected = NotFoundException.class)
    public void verify() {
        assertEquals(createVolumeResponse.getName(), volumeName);
        getConnector().removeVolume(volumeName);
        getConnector().inspectVolume(volumeName);

    }

}