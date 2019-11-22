package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import com.github.dockerjava.api.annotation.IgnoredField;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;

/**
 * Used as part of 'images/IMAGE/someimage' response.
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
@EqualsAndHashCode
@ToString
public class ContainerConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("AttachStderr")
    private Boolean attachStderr;

    @FieldName("AttachStdin")
    private Boolean attachStdin;

    @FieldName("AttachStdout")
    private Boolean attachStdout;

    @FieldName("Cmd")
    private String[] cmd;

    @FieldName("Domainname")
    private String domainName;

    @FieldName("Entrypoint")
    private String[] entrypoint;

    @FieldName("Env")
    private String[] env;

    @FieldName("ExposedPorts")
    private ExposedPorts exposedPorts;

    @FieldName("Hostname")
    private String hostName;

    @FieldName("Image")
    private String image;

    @FieldName("Labels")
    private Map<String, String> labels;

    @FieldName("MacAddress")
    private String macAddress;

    @FieldName("NetworkDisabled")
    private Boolean networkDisabled;

    @FieldName("OnBuild")
    private String[] onBuild;

    @FieldName("OpenStdin")
    private Boolean stdinOpen;

    @FieldName("PortSpecs")
    private String[] portSpecs;

    @FieldName("StdinOnce")
    private Boolean stdInOnce;

    @FieldName("Tty")
    private Boolean tty;

    @FieldName("User")
    private String user;

    @FieldName("Volumes")
    private Map<String, ?> volumes;

    @FieldName("WorkingDir")
    private String workingDir;

    @FieldName("Healthcheck")
    private HealthCheck healthCheck;

    @IgnoredField
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
    public String[] getPortSpecs() {
        return portSpecs;
    }

    /**
     * @see #portSpecs
     */
    public ContainerConfig withPortSpecs(String[] portSpecs) {
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
     * @see #healthCheck
     */
    @CheckForNull
    public HealthCheck getHealthcheck() {
        return healthCheck;
    }

    /**
     * @see #workingDir
     */
    public ContainerConfig withWorkingDir(String workingDir) {
        this.workingDir = workingDir;
        return this;
    }
}
