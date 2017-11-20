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
import static org.hamcrest.CoreMatchers.equalTo;

import com.github.dockerjava.api.model.Info;

public class ListImageTestCases extends AbstractTestCase<DockerConnector> {
    public ListImageTestCases() {
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
        java.util.List<com.github.dockerjava.api.model.Image> images = getConnector().listImage();
        assertNotNull(images);
        Info info = getConnector().dockerInfo();
        assertThat(images.size(), equalTo(info.getImages()));
    }

}