package com.github.dockerjava.api.model;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ContainerConfig {

    @JsonProperty("AttachStderr")
    private Boolean attachStderr;

    @JsonProperty("AttachStdin")
    private Boolean attachStdin;

    @JsonProperty("AttachStdout")
    private Boolean attachStdout;

    @JsonProperty("Cmd")
    private String[] cmd;

    @JsonProperty("Domainname")
    private String domainName;

    @JsonProperty("Entrypoint")
    private String[] entrypoint;

    @JsonProperty("Env")
    private String[] env;

    @JsonProperty("ExposedPorts")
    private ExposedPorts exposedPorts;

    @JsonProperty("Hostname")
    private String hostName;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("Labels")
    private Map<String, String> labels;

    @JsonProperty("MacAddress")
    private String macAddress;

    @JsonProperty("NetworkDisabled")
    private Boolean networkDisabled;

    @JsonProperty("OnBuild")
    private String[] onBuild;

    @JsonProperty("OpenStdin")
    private Boolean stdinOpen;

    @JsonProperty("PortSpecs")
    private String[] portSpecs;

    @JsonProperty("StdinOnce")
    private Boolean stdInOnce;

    @JsonProperty("Tty")
    private Boolean tty;

    @JsonProperty("User")
    private String user;

    @JsonProperty("Volumes")
    private Map<String, ?> volumes;

    @JsonProperty("WorkingDir")
    private String workingDir;

    @JsonIgnore
    public ExposedPort[] getExposedPorts() {
        return exposedPorts.getExposedPorts();
    }

    public Boolean isNetworkDisabled() {
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

    public Boolean isTty() {
        return tty;
    }

    public Boolean isStdinOpen() {
        return stdinOpen;
    }

    public Boolean isStdInOnce() {
        return stdInOnce;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public Boolean isAttachStdin() {
        return attachStdin;
    }

    public Boolean isAttachStdout() {
        return attachStdout;
    }

    public Boolean isAttachStderr() {
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
