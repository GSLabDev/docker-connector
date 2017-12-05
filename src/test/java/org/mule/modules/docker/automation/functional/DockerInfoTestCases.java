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

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Info;

public class DockerInfoTestCases extends AbstractTestCase<DockerConnector> {

    public DockerInfoTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void verify() {
        List<String> command = new ArrayList<String>();
        command.add("touch");
        command.add("/test");
        if (getConnector().dockerInfo().getContainers() == 0) {
            CreateContainerResponse container = getConnector().runContainer("busybox", "latest", "created-itest-info", command);
            assertNotNull(container.getId());
        }

        Info dockerInfo = getConnector().dockerInfo();
        assertTrue(dockerInfo.toString().contains("containers"));
        assertTrue(dockerInfo.toString().contains("images"));
        assertTrue(dockerInfo.toString().contains("debug"));

        assertTrue(dockerInfo.getContainers() > 0);
        assertTrue(dockerInfo.getImages() > 0);
        assertTrue(dockerInfo.getNFd() > 0);
        assertTrue(dockerInfo.getNGoroutines() > 0);
        assertTrue(dockerInfo.getNCPU() > 0);
    }

}