/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class InspectImageTestCasesIT extends AbstractTestCase<DockerConnector> {

    public InspectImageTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().pullImage(TestsConstants.INSPECT_IMAGE_IMAGENAME, TestsConstants.INSPECT_IMAGE_IMAGETAG, null, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void verifyInspectImage() {
        com.github.dockerjava.api.command.InspectImageResponse inspectImageResponse = getConnector().inspectImage(TestsConstants.INSPECT_IMAGE_IMAGENAME,
                TestsConstants.INSPECT_IMAGE_IMAGETAG);
        assertNotNull(inspectImageResponse.getId());
        assertNotNull(inspectImageResponse.getRepoTags());
    }

}