/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;
import static org.hamcrest.core.Is.*;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class WaitAContainerTestCases extends AbstractTestCase<DockerConnector> {
    boolean removeVolumes = false;
    boolean showSize = false;

    CreateContainerResponse container = null;

    public WaitAContainerTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        List<String> command = new ArrayList<String>();
        command.add("true");
        container = getConnector().runContainer("busybox", "latest", "created-test-wait", command);
    }

    @After
    public void tearDown() {
        getConnector().deleteContainer(container.getId(), true, removeVolumes);
    }

    @Test
    public void verify() {
        assertNotNull(container.getId());
        getConnector().waitAContainer(container.getId());

        InspectContainerResponse inspectContainerResponse = getConnector().inspectContainer(container.getId(), showSize);

        assertFalse(inspectContainerResponse.getState().getRunning());
        assertThat(inspectContainerResponse.getState().getExitCode(), is(0));
    }

}