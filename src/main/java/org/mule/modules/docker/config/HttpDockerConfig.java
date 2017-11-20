/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.config;

import org.mule.api.annotations.components.Configuration;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

/**
 * 
 * @author Great Software Laboratory Pvt. Ltd.
 *
 */
@Configuration(configElementName = "HTTP-Docker-Config", friendlyName = "HTTP Docker Config")
public class HttpDockerConfig extends AbstractDockerConfig {

    @Override
    public DockerClient getDockerClient() {

        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(getDockerHostURL()).withApiVersion(getApiVersion()).build();

        return DockerClientBuilder.getInstance(config).build();
    }

}
