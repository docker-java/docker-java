package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerConfig {

    @JsonProperty("AttachStderr")
    private boolean attachStderr = false;

    @JsonProperty("AttachStdin")
    private boolean attachStdin = false;

    @JsonProperty("AttachStdout")
    private boolean attachStdout = false;

    @JsonProperty("Cmd")
    private String[] cmd;

    @JsonProperty("Domainname")
    private String domainName = "";

    @JsonProperty("Entrypoint")
    private String[] entrypoint = new String[] {};

    @JsonProperty("Env")
    private String[] env;

    @JsonProperty("ExposedPorts")
    private ExposedPorts exposedPorts;

    @JsonProperty("Hostname")
    private String hostName = "";

    @JsonProperty("Image")
    private String image;

    @JsonProperty("Labels")
    private Map<String, String> labels;

    @JsonProperty("MacAddress")
    private String macAddress;

    @JsonProperty("NetworkDisabled")
    private boolean networkDisabled = false;

    @JsonProperty("OnBuild")
    private String[] onBuild;

    @JsonProperty("OpenStdin")
    private boolean stdinOpen = false;

    @JsonProperty("PortSpecs")
    private String[] portSpecs;

    @JsonProperty("StdinOnce")
    private boolean stdInOnce = false;

    @JsonProperty("Tty")
    private boolean tty = false;

    @JsonProperty("User")
    private String user = "";

    @JsonProperty("Volumes")
    private Map<String, ?> volumes;

    @JsonProperty("WorkingDir")
    private String workingDir = "";

    @JsonIgnore
    public ExposedPort[] getExposedPorts() {
        return exposedPorts.getExposedPorts();
    }

    public boolean isNetworkDisabled() {
        return networkDisabled;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public String getHostName() {
        return hostName;
    }

    public String[] getPortSpecs() {
        return portSpecs;
    }

    public String getUser() {
        return user;
    }

    public boolean isTty() {
        return tty;
    }

    public boolean isStdinOpen() {
        return stdinOpen;
    }

    public boolean isStdInOnce() {
        return stdInOnce;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public boolean isAttachStdin() {
        return attachStdin;
    }

    public boolean isAttachStdout() {
        return attachStdout;
    }

    public boolean isAttachStderr() {
        return attachStderr;
    }

    public String[] getEnv() {
        return env;
    }

    public String[] getCmd() {
        return cmd;
    }

    public String getImage() {
        return image;
    }

    public Map<String, ?> getVolumes() {
        return volumes;
    }

    public String[] getEntrypoint() {
        return entrypoint;
    }

    public String[] getOnBuild() {
        return onBuild;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
