/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.docker.automation.unit.ContainerOperationTests;
import org.mule.modules.docker.automation.unit.ImageOperationTests;
import org.mule.modules.docker.automation.unit.VolumeOperationTests;

@RunWith(Suite.class)
@SuiteClasses({
    ContainerOperationTests.class,
    VolumeOperationTests.class,
    ImageOperationTests.class
})
public class UnitTestSuite {

}
