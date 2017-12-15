/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.docker.config;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.components.Configuration;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.display.Summary;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

/**
 * 
 * @author Great Software Laboratory Pvt. Ltd.
 *
 */
@Configuration(configElementName = "HTTPS-Docker-Config", friendlyName = "HTTPS Docker Config")
public class HttpsDockerConfig extends AbstractDockerConfig {

    /**
     * Path of directory containing certificates required for docker TLS
     * connection (ca.pem, cert.pem, key.pem)
     */
    @Configurable
    @Placement(order = 5)
    @Summary(value = "Provide the path of client certificates")
    private String clientCertificateDirectoryPath;

    /**
     * @return client certificates directory path.
     */
    public String getClientCertificateDirectoryPath() {
        return clientCertificateDirectoryPath;
    }

    /**
     * Set client certificates directory path
     * 
     * @param clientCertificateDirectoryPath
     *            Path of directory containing certificates required for docker
     *            TLS connection (ca.pem, cert.pem, key.pem)
     */

    public void setClientCertificateDirectoryPath(String clientCertificateDirectoryPath) {
        this.clientCertificateDirectoryPath = clientCertificateDirectoryPath;
    }

    @Override
    public DockerClient getDockerClient() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(getDockerHostURL()).withApiVersion(getApiVersion()).withDockerTlsVerify(true)
                .withDockerCertPath(getClientCertificateDirectoryPath()).build();

        return DockerClientBuilder.getInstance(config).build();
    }

}
