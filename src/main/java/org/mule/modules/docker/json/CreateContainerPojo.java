/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.HostConfig;

/**
 * 
 * Create Container POJO
 *
 */
public class CreateContainerPojo {

    public CreateContainerPojo() {
        // Default constructor used by jackson
    }

    @JsonProperty("Hostname")
    private String hostName;
    @JsonProperty("Domainname")
    private String domainName;
    private String name;
    private String user;
    private boolean attachStdin;
    private boolean attachStdout;
    private boolean attachStderr;
    private boolean tty;
    @JsonProperty("OpenStdin")
    private boolean stdinOpen;
    @JsonProperty("StdinOnce")
    private boolean stdInOnce;
    private List<String> env = null;
    private List<String> cmd = null;
    private List<String> entrypoint;
    private String image;
    private Map<String, String> labels = null;
    private String workingDir;
    private boolean networkDisabled;
    private String macAddress;
    private String stopSignal;
    @JsonProperty("HostConfig")
    private HostConfig hostConfig;
    private String ipv4Address;
    private String ipv6Address;
    private List<String> aliases;

    public com.github.dockerjava.api.model.HostConfig getHostConfig() {
        return hostConfig;
    }

    public void setHostConfig(com.github.dockerjava.api.model.HostConfig hostConfig) {
        this.hostConfig = hostConfig;
    }

    public String getHostName() {
        return this.hostName;
    }

    @JsonProperty("NetworkingConfig")
    public void extractNetworkingConfig(NetworkingConfig networkingConfig) {
        this.ipv4Address = networkingConfig.endpointsConfig.isolatedNw.ipAMConfig.ipv4Address;
        this.ipv6Address = networkingConfig.endpointsConfig.isolatedNw.ipAMConfig.ipv6Address;
        this.aliases = networkingConfig.endpointsConfig.isolatedNw.aliases;
    }

    public String getIpv4Address() {
        return ipv4Address;
    }

    public String getIpv6Address() {
        return ipv6Address;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setHostname(String hostname) {
        this.hostName = hostname;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainname) {
        this.domainName = domainname;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isAttachStdin() {
        return attachStdin;
    }

    public void setAttachStdin(boolean attachStdin) {
        this.attachStdin = attachStdin;
    }

    public boolean isAttachStdout() {
        return attachStdout;
    }

    public void setAttachStdout(boolean attachStdout) {
        this.attachStdout = attachStdout;
    }

    public boolean isAttachStderr() {
        return attachStderr;
    }

    public void setAttachStderr(boolean attachStderr) {
        this.attachStderr = attachStderr;
    }

    public boolean isTty() {
        return tty;
    }

    public void setTty(boolean tty) {
        this.tty = tty;
    }

    public boolean isStdinOpen() {
        return stdinOpen;
    }

    public void setStdinOpen(boolean stdinOpen) {
        this.stdinOpen = stdinOpen;
    }

    public boolean isStdInOnce() {
        return stdInOnce;
    }

    public void setStdInOnce(boolean stdinOnce) {
        this.stdInOnce = stdinOnce;
    }

    public List<String> getEnv() {
        return env;
    }

    public void setEnv(List<String> env) {
        this.env = env;
    }

    public List<String> getCmd() {
        return cmd;
    }

    public void setCmd(List<String> cmd) {
        this.cmd = cmd;
    }

    public List<String> getEntrypoint() {
        return entrypoint;
    }

    public void setEntrypoint(String entrypoint) {
        this.entrypoint = new ArrayList<String>(Arrays.asList(entrypoint.split("\\s+")));
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    public boolean isNetworkDisabled() {
        return networkDisabled;
    }

    public void setNetworkDisabled(boolean networkDisabled) {
        this.networkDisabled = networkDisabled;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getStopSignal() {
        return stopSignal;
    }

    public void setStopSignal(String stopSignal) {
        this.stopSignal = stopSignal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    static class NetworkingConfig {

        public NetworkingConfig() {
            // Default constructor used by jackson
        }

        private EndpointsConfig endpointsConfig;

        public void setEndpointsConfig(EndpointsConfig endpointsConfig) {
            this.endpointsConfig = endpointsConfig;
        }
    }

    static class EndpointsConfig {

        @JsonProperty("isolated_nw")
        private IsolatedNW isolatedNw;

        public void setIsolatedNw(IsolatedNW isolatedNw) {
            this.isolatedNw = isolatedNw;
        }
    }

    static class IsolatedNW {

        private List<String> aliases;
        @JsonProperty("IPAMConfig")
        private IPAMConfig ipAMConfig;

        public void setAliases(List<String> aliases) {
            this.aliases = aliases;
        }

    }

    static class IPAMConfig {

        @JsonProperty("IPv4Address")
        private String ipv4Address;
        @JsonProperty("IPv6Address")
        private String ipv6Address;

        public void setIPv4Address(String iPv4Address) {
            this.ipv4Address = iPv4Address;
        }

        public void setIPv6Address(String iPv6Address) {
            this.ipv6Address = iPv6Address;
        }
    }

}