package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;

/**
 * Used as part of 'images/IMAGE/someimage' response.
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ContainerConfig implements Serializable {
    private static final long serialVersionUID = 1L;

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

    //YD - switched from String[] to String, BMX returns empty string "" which causes runtime error
    @JsonProperty("PortSpecs")
    private String portSpecs;

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
        return exposedPorts != null ? exposedPorts.getExposedPorts() : null;
    }

    /**
     * @see #attachStderr
     */
    @CheckForNull
    public Boolean getAttachStderr() {
        return attachStderr;
    }

    /**
     * @see #attachStderr
     */
    public ContainerConfig withAttachStderr(Boolean attachStderr) {
        this.attachStderr = attachStderr;
        return this;
    }

    /**
     * @see #attachStdin
     */
    @CheckForNull
    public Boolean getAttachStdin() {
        return attachStdin;
    }

    /**
     * @see #attachStdin
     */
    public ContainerConfig withAttachStdin(Boolean attachStdin) {
        this.attachStdin = attachStdin;
        return this;
    }

    /**
     * @see #attachStdout
     */
    @CheckForNull
    public Boolean getAttachStdout() {
        return attachStdout;
    }

    /**
     * @see #attachStdout
     */
    public ContainerConfig withAttachStdout(Boolean attachStdout) {
        this.attachStdout = attachStdout;
        return this;
    }

    /**
     * @see #cmd
     */
    @CheckForNull
    public String[] getCmd() {
        return cmd;
    }

    /**
     * @see #cmd
     */
    public ContainerConfig withCmd(String[] cmd) {
        this.cmd = cmd;
        return this;
    }

    /**
     * @see #domainName
     */
    @CheckForNull
    public String getDomainName() {
        return domainName;
    }

    /**
     * @see #domainName
     */
    public ContainerConfig withDomainName(String domainName) {
        this.domainName = domainName;
        return this;
    }

    /**
     * @see #entrypoint
     */
    @CheckForNull
    public String[] getEntrypoint() {
        return entrypoint;
    }

    /**
     * @see #entrypoint
     */
    public ContainerConfig withEntrypoint(String[] entrypoint) {
        this.entrypoint = entrypoint;
        return this;
    }

    /**
     * @see #env
     */
    @CheckForNull
    public String[] getEnv() {
        return env;
    }

    /**
     * @see #env
     */
    public ContainerConfig withEnv(String[] env) {
        this.env = env;
        return this;
    }

    /**
     * @see #exposedPorts
     */
    public ContainerConfig withExposedPorts(ExposedPorts exposedPorts) {
        this.exposedPorts = exposedPorts;
        return this;
    }

    /**
     * @see #hostName
     */
    @CheckForNull
    public String getHostName() {
        return hostName;
    }

    /**
     * @see #hostName
     */
    public ContainerConfig withHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    /**
     * @see #image
     */
    @CheckForNull
    public String getImage() {
        return image;
    }

    /**
     * @see #image
     */
    public ContainerConfig withImage(String image) {
        this.image = image;
        return this;
    }

    /**
     * @see #labels
     */
    @CheckForNull
    public Map<String, String> getLabels() {
        return labels;
    }

    /**
     * @see #labels
     */
    public ContainerConfig withLabels(Map<String, String> labels) {
        this.labels = labels;
        return this;
    }

    /**
     * @see #macAddress
     */
    @CheckForNull
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * @see #macAddress
     */
    public ContainerConfig withMacAddress(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }

    /**
     * @see #networkDisabled
     */
    @CheckForNull
    public Boolean getNetworkDisabled() {
        return networkDisabled;
    }

    /**
     * @see #networkDisabled
     */
    public ContainerConfig withNetworkDisabled(Boolean networkDisabled) {
        this.networkDisabled = networkDisabled;
        return this;
    }

    /**
     * @see #onBuild
     */
    @CheckForNull
    public String[] getOnBuild() {
        return onBuild;
    }

    /**
     * @see #onBuild
     */
    public ContainerConfig withOnBuild(String[] onBuild) {
        this.onBuild = onBuild;
        return this;
    }

    /**
     * @see #portSpecs
     */
    @CheckForNull
    public String getPortSpecs() {
        return portSpecs;
    }

    /**
     * @see #portSpecs
     */
    public ContainerConfig withPortSpecs(String portSpecs) {
        this.portSpecs = portSpecs;
        return this;
    }

    /**
     * @see #stdInOnce
     */
    @CheckForNull
    public Boolean getStdInOnce() {
        return stdInOnce;
    }

    /**
     * @see #stdInOnce
     */
    public ContainerConfig withStdInOnce(Boolean stdInOnce) {
        this.stdInOnce = stdInOnce;
        return this;
    }

    /**
     * @see #stdinOpen
     */
    @CheckForNull
    public Boolean getStdinOpen() {
        return stdinOpen;
    }

    /**
     * @see #stdinOpen
     */
    public ContainerConfig withStdinOpen(Boolean stdinOpen) {
        this.stdinOpen = stdinOpen;
        return this;
    }

    /**
     * @see #tty
     */
    @CheckForNull
    public Boolean getTty() {
        return tty;
    }

    /**
     * @see #tty
     */
    public ContainerConfig withTty(Boolean tty) {
        this.tty = tty;
        return this;
    }

    /**
     * @see #user
     */
    @CheckForNull
    public String getUser() {
        return user;
    }

    /**
     * @see #user
     */
    public ContainerConfig withUser(String user) {
        this.user = user;
        return this;
    }

    /**
     * @see #volumes
     */
    @CheckForNull
    public Map<String, ?> getVolumes() {
        return volumes;
    }

    /**
     * @see #volumes
     */
    public ContainerConfig withVolumes(Map<String, ?> volumes) {
        this.volumes = volumes;
        return this;
    }

    /**
     * @see #workingDir
     */
    @CheckForNull
    public String getWorkingDir() {
        return workingDir;
    }

    /**
     * @see #workingDir
     */
    public ContainerConfig withWorkingDir(String workingDir) {
        this.workingDir = workingDir;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
