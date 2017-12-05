/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.docker.config;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.display.Summary;
import org.mule.api.annotations.param.Default;
import org.mule.extension.annotations.param.Optional;
import com.github.dockerjava.api.DockerClient;

/**
 * 
 * @author Great Software Laboratory Pvt. Ltd.
 *
 */
public abstract class AbstractDockerConfig {

    /**
     * Docker host IP or DNS name
     */
    @Configurable
    @FriendlyName(value = "Docker Host")
    @Summary(value = "Provide DNS Name or IP address of Docker Engine Host:: e.g. myDockerHost.com")
    @Placement(order = 1)
    private String dockerHostIP;

    /**
     * Docker Engine Port
     */
    @Configurable
    @FriendlyName(value = "Docker Host Port")
    @Summary(value = "Provide the port of Docker Engine:: e.g. 4242")
    @Placement(order = 2)
    private String dockerHostPort;

    /**
     * Docker Engine API version
     */
    @Configurable
    @FriendlyName(value = "Docker API version")
    @Summary(value = "Docker API version : e.g. 1.24")
    @Placement(order = 3)
    @Optional
    @Default("1.24")
    private String apiVersion;

    /**
     * Set docker host IP or DNS name
     * 
     * @param dockerHostIP
     *            or DNS name
     */
    public void setDockerHostIP(String dockerHostIP) {
        this.dockerHostIP = dockerHostIP;
    }

    /**
     * Get docker host IP or DNS name
     * 
     * @return Docker host IP or DNS name
     */
    public String getDockerHostIP() {
        return this.dockerHostIP;
    }

    /**
     * Get docker engine port
     * 
     * @return dockerHostPort Port on Which docker Engine is running
     */
    public String getDockerHostPort() {
        return dockerHostPort;
    }

    /**
     * Set the docker engine port
     * 
     * @param dockerEnginePort
     *            Port on Which docker Engine is running
     */
    public void setDockerHostPort(String dockerEnginePort) {
        this.dockerHostPort = dockerEnginePort;
    }

    /**
     * Get docker engine API version
     * 
     * @return API Version API version of docker Engine API
     */
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * Set Docker engine API version
     * 
     * @param apiVersion
     *            API version of docker Engine API
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * Utility method to prepare docker host URL
     * 
     * @return Docker host URL based on protocol, IP and port
     */
    public String getDockerHostURL() {
        return "tcp://" + getDockerHostIP() + ":" + getDockerHostPort();
    }

    /**
     * Abstract method to return Docker Client Object
     * 
     * @return Docker client object
     */
    public abstract DockerClient getDockerClient();

}