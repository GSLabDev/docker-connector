/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Info;

public class ListContainersTestCases extends AbstractTestCase<DockerConnector> {

    java.lang.String imageName = "busybox", imageTag = "latest", containerName = "Created-test-list-container",
            signal = "SIGKILL";
    boolean showSize = false, removeVolumes = false, showAll = true;
    CreateContainerResponse container = null;

    public ListContainersTestCases() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        List<String> command = new ArrayList<String>();
        command.add("sleep");
        command.add("999999");
        try {
            getConnector().pullImage(imageName, imageTag, null, null);
            getConnector().runContainer(imageName, imageTag, containerName, command);
        } catch (Exception e) {
        }
    }

    @After
    public void tearDown() {
        try {
            if (getConnector().inspectContainer(containerName, showSize).getState().getRunning()) {
                getConnector().killContainer(containerName, signal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getConnector().deleteContainer(containerName, true, removeVolumes);
        }

    }

    @Test
    public void verifyDefault() {
        java.lang.String status = null, labels = null, before = null;
        int limit = 0;
        java.util.List<com.github.dockerjava.api.model.Container> containers = getConnector().listContainers(showAll,
                before, limit, showSize, status, labels);
        assertNotNull(containers);
        Info info = getConnector().dockerInfo();
        assertThat(containers.size(), equalTo(info.getContainers()));
    }

    @Test
    public void verifyWithAll() {
        java.lang.String status = "running", labels = "NonExisting", before = "nonCreated";
        boolean showSize = true, showAll = true;
        int limit = 10;
        java.util.List<com.github.dockerjava.api.model.Container> containers = getConnector().listContainers(showAll,
                before, limit, showSize, status, labels);
        assertTrue(containers.size() <= 0);
        Info info = getConnector().dockerInfo();
        assertTrue(containers.size() <= info.getContainers());
    }

}