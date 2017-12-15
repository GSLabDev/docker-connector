/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.docker.automation.functional.processors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class PushImageTestCasesIT extends AbstractTestCase<DockerConnector> {

    public PushImageTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        try {
            getConnector().pullImage(TestsConstants.PULL_IMAGE_TESTIMAGE, TestsConstants.PULL_IMAGE_IMAGETAG, TestsConstants.PULL_IMAGE_USERNAME,
                    TestsConstants.PULL_IMAGE_PASSWORD);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() {
        getConnector().removeImage(TestsConstants.PUSH_IMAGE_REPOSITORY + "/" + TestsConstants.PUSH_IMAGE_DEST_IMAGE_NAME, TestsConstants.PUSH_IMAGE_DEST_IMAGE_TAG, true,
                TestsConstants.PUSH_IMAGE_PRUNE, null);
    }

    @Test
    public void verifyWithoutUsernamePassword() throws InterruptedException {
        getConnector().tagImage(TestsConstants.PULL_IMAGE_TESTIMAGE, TestsConstants.PUSH_IMAGE_IMAGETAG, TestsConstants.PUSH_IMAGE_DEST_IMAGE_NAME,
                TestsConstants.PUSH_IMAGE_REPOSITORY, TestsConstants.PUSH_IMAGE_DEST_IMAGE_TAG);
        getConnector().pushImage(TestsConstants.PUSH_IMAGE_REPOSITORY + "/" + TestsConstants.PUSH_IMAGE_DEST_IMAGE_NAME, TestsConstants.PUSH_IMAGE_DEST_IMAGE_TAG, null, null);
        getConnector().removeImage(TestsConstants.PULL_IMAGE_TESTIMAGE, TestsConstants.PUSH_IMAGE_IMAGETAG, true, TestsConstants.PUSH_IMAGE_PRUNE, null);
        getConnector().pullImage(TestsConstants.PUSH_IMAGE_REPOSITORY + "/" + TestsConstants.PUSH_IMAGE_DEST_IMAGE_NAME, TestsConstants.PUSH_IMAGE_DEST_IMAGE_TAG, null, null);
    }

    @Test
    public void verifyWithUsernamePassword() throws InterruptedException {
        getConnector().tagImage(TestsConstants.PULL_IMAGE_TESTIMAGE, TestsConstants.PUSH_IMAGE_IMAGETAG, TestsConstants.PUSH_IMAGE_DEST_IMAGE_NAME,
                TestsConstants.PUSH_IMAGE_REPOSITORY, TestsConstants.PUSH_IMAGE_DEST_IMAGE_TAG);
        getConnector().pushImage(TestsConstants.PUSH_IMAGE_REPOSITORY + "/" + TestsConstants.PUSH_IMAGE_DEST_IMAGE_NAME, TestsConstants.PUSH_IMAGE_DEST_IMAGE_TAG,
                TestsConstants.PUSH_IMAGE_USERNAME, TestsConstants.PUSH_IMAGE_PASSWORD);
        getConnector().removeImage(TestsConstants.PULL_IMAGE_TESTIMAGE, TestsConstants.PUSH_IMAGE_IMAGETAG, true, TestsConstants.PUSH_IMAGE_PRUNE, null);
        getConnector().pullImage(TestsConstants.PUSH_IMAGE_REPOSITORY + "/" + TestsConstants.PUSH_IMAGE_DEST_IMAGE_NAME, TestsConstants.PUSH_IMAGE_DEST_IMAGE_TAG,
                TestsConstants.PUSH_IMAGE_USERNAME, TestsConstants.PUSH_IMAGE_PASSWORD);
    }

}