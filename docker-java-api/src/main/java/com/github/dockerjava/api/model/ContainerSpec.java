package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The specification for containers as used in {@link TaskSpec}
 *
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class ContainerSpec implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @FieldName("Image")
    private String image;

    /**
     * @since 1.24
     */
    @FieldName("Labels")
    private Map<String, String> labels;

    /**
     * @since 1.24
     */
    @FieldName("Command")
    private List<String> command;

    /**
     * @since 1.24
     */
    @FieldName("Args")
    private List<String> args;

    /**
     * @since 1.24
     */
    @FieldName("Env")
    private List<String> env;

    /**
     * @since 1.24
     */
    @FieldName("Dir")
    private String dir;

    /**
     * @since 1.24
     */
    @FieldName("User")
    private String user;

    /**
     * @since 1.24
     */
    @FieldName("Groups")
    private String groups;

    /**
     * @since 1.24
     */
    @FieldName("TTY")
    private Boolean tty;

    /**
     * @since 1.24
     */
    @FieldName("Mounts")
    private List<Mount> mounts;

    /**
     * @since 1.24
     */
    @FieldName("Duration")
    private Long duration;

    /**
     * @since 1.24
     */
    @FieldName("StopGracePeriod")
    private Long stopGracePeriod;

    /**
     * @since 1.25
     * Specification for DNS related configurations in resolver configuration file
     */
    @FieldName("DNSConfig")
    private ContainerDNSConfig dnsConfig;

    /**
     * @since 1.26
     * Open stdin
     */
    @FieldName("OpenStdin")
    private Boolean openStdin;

    /**
     * @since 1.26
     * Mount the container's root filesystem as read only.
     */
    @FieldName("ReadOnly")
    private Boolean readOnly;

    /**
     * @since 1.26
     * A list of hostnames/IP mappings to add to the container's /etc/hosts file.
     */
    @FieldName("Hosts")
    private List<String> hosts;

    /**
     * @since 1.26
     * The hostname to use for the container, as a valid RFC 1123 hostname
     */
    @FieldName("Hostname")
    private String hostname;

    /**
     * @since 1.26
     * Secrets contains references to zero or more secrets that will be exposed to the service.
     */
    @FieldName("Secrets")
    private List<ContainerSpecSecret> secrets;

    /**
     * @since 1.26
     * A test to perform to check that the container is healthy.
     */
    @FieldName("HealthCheck")
    private HealthCheck healthCheck;

    /**
     * @since 1.28
     * Signal to stop the container.
     */
    @FieldName("StopSignal")
    private String stopSignal;

    /**
     * @since 1.29
     * Security options for the container
     */
    @FieldName("Privileges")
    private ContainerSpecPrivileges privileges;

    /**
     * @since 1.29
     * Configs contains references to zero or more configs that will be exposed to the service.
     */
    @FieldName("Configs")
    private List<ContainerSpecConfig> configs;

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
    public ContainerSpec withImage(String image) {
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
    public ContainerSpec withLabels(Map<String, String> labels) {
        this.labels = labels;
        return this;
    }

    /**
     * @see #command
     */
    @CheckForNull
    public List<String> getCommand() {
        return command;
    }

    /**
     * @see #command
     */
    public ContainerSpec withCommand(List<String> command) {
        this.command = command;
        return this;
    }

    /**
     * @see #args
     */
    @CheckForNull
    public List<String> getArgs() {
        return args;
    }

    /**
     * @see #args
     */
    public ContainerSpec withArgs(List<String> args) {
        this.args = args;
        return this;
    }

    /**
     * @see #env
     */
    @CheckForNull
    public List<String> getEnv() {
        return env;
    }

    /**
     * @see #env
     */
    public ContainerSpec withEnv(List<String> env) {
        this.env = env;
        return this;
    }

    /**
     * @see #dir
     */
    @CheckForNull
    public String getDir() {
        return dir;
    }

    /**
     * @see #dir
     */
    public ContainerSpec withDir(String dir) {
        this.dir = dir;
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
    public ContainerSpec withUser(String user) {
        this.user = user;
        return this;
    }

    /**
     * @see #groups
     */
    @CheckForNull
    public String getGroups() {
        return groups;
    }

    /**
     * @see #groups
     */
    public ContainerSpec withGroups(String groups) {
        this.groups = groups;
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
    public ContainerSpec withTty(Boolean tty) {
        this.tty = tty;
        return this;
    }

    /**
     * @see #mounts
     */
    @CheckForNull
    public List<Mount> getMounts() {
        return mounts;
    }

    /**
     * @see #mounts
     */
    public ContainerSpec withMounts(List<Mount> mounts) {
        this.mounts = mounts;
        return this;
    }

    /**
     * @see #duration
     */
    @CheckForNull
    public Long getDuration() {
        return duration;
    }

    /**
     * @see #duration
     */
    public ContainerSpec withDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public ContainerDNSConfig getDnsConfig() {
        return dnsConfig;
    }

    public ContainerSpec withDnsConfig(ContainerDNSConfig dnsConfig) {
        this.dnsConfig = dnsConfig;
        return this;
    }

    public Boolean getOpenStdin() {
        return openStdin;
    }

    public ContainerSpec withOpenStdin(Boolean openStdin) {
        this.openStdin = openStdin;
        return this;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public ContainerSpec withReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public ContainerSpec withHosts(List<String> hosts) {
        this.hosts = hosts;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public ContainerSpec withHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public List<ContainerSpecSecret> getSecrets() {
        return secrets;
    }

    public ContainerSpec withSecrets(List<ContainerSpecSecret> secrets) {
        this.secrets = secrets;
        return this;
    }

    public HealthCheck getHealthCheck() {
        return healthCheck;
    }

    public ContainerSpec withHealthCheck(HealthCheck healthCheck) {
        this.healthCheck = healthCheck;
        return this;
    }

    public String getStopSignal() {
        return stopSignal;
    }

    public ContainerSpec withStopSignal(String stopSignal) {
        this.stopSignal = stopSignal;
        return this;
    }

    public Long getStopGracePeriod() {
        return stopGracePeriod;
    }

    public ContainerSpec withStopGracePeriod(Long stopGracePeriod) {
        this.stopGracePeriod = stopGracePeriod;
        return this;
    }

    public ContainerSpecPrivileges getPrivileges() {
        return privileges;
    }

    public ContainerSpec withPrivileges(ContainerSpecPrivileges privileges) {
        this.privileges = privileges;
        return this;
    }

    public List<ContainerSpecConfig> getConfigs() {
        return configs;
    }

    public ContainerSpec withConfigs(List<ContainerSpecConfig> configs) {
        this.configs = configs;
        return this;
    }
}
