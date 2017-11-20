package org.mule.modules.docker.automation.functional;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;
import static org.hamcrest.CoreMatchers.equalTo;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Info;

public class ListContainersTestCases extends AbstractTestCase<DockerConnector> {

    java.lang.String imageName = "busybox";
    java.lang.String imageTag = "latest";
    java.lang.String containerName = "Created-test-list-container";
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
            getConnector().pullImage(imageName, imageTag);
            getConnector().runContainer(imageName, imageTag, containerName, command);
        } catch (Exception e) {
        }
    }

    @After
    public void tearDown() {
        try {
            if (getConnector().inspectContainer(containerName).getState().getRunning()) {
                getConnector().killContainer(containerName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getConnector().deleteContainer(containerName, true);
        }

    }

    @Test
    public void verify() {
        java.util.List<com.github.dockerjava.api.model.Container> containers = getConnector().listContainers(true);
        assertNotNull(containers);
        Info info = getConnector().dockerInfo();
        System.out.println("container listing" + containers );
        assertThat(containers.size(), equalTo(info.getContainers()));
    }

}