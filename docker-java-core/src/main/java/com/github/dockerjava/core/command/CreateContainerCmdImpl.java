package com.github.dockerjava.core.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Capability;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.ExposedPorts;
import com.github.dockerjava.api.model.HealthCheck;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.LogConfig;
import com.github.dockerjava.api.model.LxcConf;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.Ulimit;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.Volumes;
import com.github.dockerjava.api.model.VolumesFrom;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonMap;

/**
 * Creates a new container.
 * `/containers/create`
 */
public class CreateContainerCmdImpl extends AbstrDockerCmd<CreateContainerCmd, CreateContainerResponse> implements
        CreateContainerCmd {

    private String name;

    @JsonProperty("Hostname")
    private String hostName;

    @JsonProperty("Domainname")
    private String domainName;

    @JsonProperty("User")
    private String user;

    @JsonProperty("AttachStdin")
    private Boolean attachStdin;

    @JsonProperty("AttachStdout")
    private Boolean attachStdout;

    @JsonProperty("AttachStderr")
    private Boolean attachStderr;

    @JsonProperty("PortSpecs")
    private String[] portSpecs;

    @JsonProperty("Tty")
    private Boolean tty;

    @JsonProperty("OpenStdin")
    private Boolean stdinOpen;

    @JsonProperty("StdinOnce")
    private Boolean stdInOnce;

    @JsonProperty("Env")
    private String[] env;

    @JsonProperty("Cmd")
    private String[] cmd;

    @JsonProperty("Healthcheck")
    private HealthCheck healthcheck;

    @JsonProperty("ArgsEscaped")
    private Boolean argsEscaped;

    @JsonProperty("Entrypoint")
    private String[] entrypoint;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("Volumes")
    private Volumes volumes = new Volumes();

    @JsonProperty("WorkingDir")
    private String workingDir;

    @JsonProperty("MacAddress")
    private String macAddress;

    @JsonProperty("OnBuild")
    private List<String> onBuild;

    @JsonProperty("NetworkDisabled")
    private Boolean networkDisabled;

    @JsonProperty("ExposedPorts")
    private ExposedPorts exposedPorts = new ExposedPorts();

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_21}
     */
    @JsonProperty("StopSignal")
    private String stopSignal;

    @JsonProperty("StopTimeout")
    private Integer stopTimeout;

    @JsonProperty("HostConfig")
    private HostConfig hostConfig = new HostConfig();

    @JsonProperty("Labels")
    private Map<String, String> labels;

    @JsonProperty("Shell")
    private List<String> shell;

    @JsonProperty("NetworkingConfig")
    private NetworkingConfig networkingConfig;

    @JsonIgnore
    private String ipv4Address = null;

    @JsonIgnore
    private String ipv6Address = null;

    @JsonIgnore
    private List<String> aliases = null;

    private AuthConfig authConfig;

    public CreateContainerCmdImpl(CreateContainerCmd.Exec exec, AuthConfig authConfig, String image) {
        super(exec);
        withAuthConfig(authConfig);
        withImage(image);
    }

    public AuthConfig getAuthConfig() {
        return authConfig;
    }

    public CreateContainerCmd withAuthConfig(AuthConfig authConfig) {
        this.authConfig = authConfig;
        return this;
    }

    @Override
    @JsonIgnore
    public List<String> getAliases() {
        return aliases;
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Bind[] getBinds() {
        return hostConfig.getBinds();
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Integer getBlkioWeight() {
        return hostConfig.getBlkioWeight();
    }

    @Override
    @Deprecated
    public CreateContainerCmd withBinds(Bind... binds) {
        checkNotNull(binds, "binds was not specified");
        hostConfig.setBinds(binds);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withBinds(List<Bind> binds) {
        checkNotNull(binds, "binds was not specified");
        return withBinds(binds.toArray(new Bind[binds.size()]));
    }

    @Override
    @Deprecated
    public CreateContainerCmd withBlkioWeight(Integer blkioWeight) {
        hostConfig.withBlkioWeight(blkioWeight);
        return this;
    }

    @Override
    public CreateContainerCmd withAliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    @Override
    public CreateContainerCmd withAliases(List<String> aliases) {
        checkNotNull(aliases, "aliases was not specified");
        this.aliases = aliases;
        return this;
    }


    @Override
    public String[] getCmd() {
        return cmd;
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Integer getCpuPeriod() {
        Long result = hostConfig.getCpuPeriod();
        return result != null ? result.intValue() : null;
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Integer getCpuShares() {
        return hostConfig.getCpuShares();
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public String getCpusetCpus() {
        return hostConfig.getCpusetCpus();
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public String getCpusetMems() {
        return hostConfig.getCpusetMems();
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Device[] getDevices() {
        return hostConfig.getDevices();
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public String[] getDns() {
        return hostConfig.getDns();
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public String[] getDnsSearch() {
        return hostConfig.getDnsSearch();
    }

    @Override
    public CreateContainerCmd withCmd(String... cmd) {
        checkNotNull(cmd, "cmd was not specified");
        this.cmd = cmd;
        return this;
    }

    @Override
    public CreateContainerCmd withCmd(List<String> cmd) {
        checkNotNull(cmd, "cmd was not specified");
        return withCmd(cmd.toArray(new String[0]));
    }

    @Override
    @Deprecated
    public CreateContainerCmd withContainerIDFile(String containerIDFile) {
        hostConfig.withContainerIDFile(containerIDFile);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withCpuPeriod(Integer cpuPeriod) {
        hostConfig.withCpuPeriod(cpuPeriod != null ? cpuPeriod.longValue() : null);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withCpuShares(Integer cpuShares) {
        hostConfig.withCpuShares(cpuShares);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withCpusetCpus(String cpusetCpus) {
        hostConfig.withCpusetCpus(cpusetCpus);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withCpusetMems(String cpusetMems) {
        hostConfig.withCpusetMems(cpusetMems);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withDevices(Device... devices) {
        hostConfig.withDevices(devices);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withDevices(List<Device> devices) {
        hostConfig.withDevices(devices);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withDns(String... dns) {
        hostConfig.withDns(dns);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withDns(List<String> dns) {
        hostConfig.withDns(dns);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withDnsSearch(String... dnsSearch) {
        hostConfig.withDnsSearch(dnsSearch);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withDnsSearch(List<String> dnsSearch) {
        hostConfig.withDnsSearch(dnsSearch);
        return this;
    }

    @CheckForNull
    public HealthCheck getHealthcheck() {
        return healthcheck;
    }

    public CreateContainerCmdImpl withHealthcheck(HealthCheck healthcheck) {
        this.healthcheck = healthcheck;
        return this;
    }

    public Boolean getArgsEscaped() {
        return argsEscaped;
    }

    public CreateContainerCmdImpl withArgsEscaped(Boolean argsEscaped) {
        this.argsEscaped = argsEscaped;
        return this;
    }

    @Override
    public String getDomainName() {
        return domainName;
    }

    @Override
    public CreateContainerCmd withDomainName(String domainName) {
        checkNotNull(domainName, "no domainName was specified");
        this.domainName = domainName;
        return this;
    }

    @Override
    public String[] getEntrypoint() {
        return entrypoint;
    }

    @Override
    public CreateContainerCmd withEntrypoint(String... entrypoint) {
        checkNotNull(entrypoint, "entrypoint was not specified");
        this.entrypoint = entrypoint;
        return this;
    }

    @Override
    public CreateContainerCmd withEntrypoint(List<String> entrypoint) {
        checkNotNull(entrypoint, "entrypoint was not specified");
        return withEntrypoint(entrypoint.toArray(new String[0]));
    }

    @Override
    public String[] getEnv() {
        return env;
    }

    @Override
    public CreateContainerCmd withEnv(String... env) {
        checkNotNull(env, "env was not specified");
        this.env = env;
        return this;
    }

    @Override
    public CreateContainerCmd withEnv(List<String> env) {
        checkNotNull(env, "env was not specified");
        return withEnv(env.toArray(new String[0]));
    }

    @Override
    @JsonIgnore
    public ExposedPort[] getExposedPorts() {
        return exposedPorts.getExposedPorts();
    }

    @Override
    public CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts) {
        checkNotNull(exposedPorts, "exposedPorts was not specified");
        this.exposedPorts = new ExposedPorts(exposedPorts);
        return this;
    }

    @Override
    public CreateContainerCmd withExposedPorts(List<ExposedPort> exposedPorts) {
        checkNotNull(exposedPorts, "exposedPorts was not specified");
        return withExposedPorts(exposedPorts.toArray(new ExposedPort[0]));
    }

    /**
     * @see #stopSignal
     */
    @JsonIgnore
    @Override
    public String getStopSignal() {
        return stopSignal;
    }

    @Override
    public CreateContainerCmd withStopSignal(String stopSignal) {
        checkNotNull(stopSignal, "stopSignal wasn't specified.");
        this.stopSignal = stopSignal;
        return this;
    }

    @Override
    public Integer getStopTimeout() {
        return stopTimeout;
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Ulimit[] getUlimits() {
        return hostConfig.getUlimits();
    }

    @Override
    public CreateContainerCmd withStopTimeout(Integer stopTimeout) {
        this.stopTimeout = stopTimeout;
        return this;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public CreateContainerCmd withHostName(String hostName) {
        checkNotNull(hostName, "no hostName was specified");
        this.hostName = hostName;
        return this;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public CreateContainerCmd withImage(String image) {
        checkNotNull(image, "no image was specified");
        this.image = image;
        return this;
    }

    @Override
    @JsonIgnore
    public Map<String, String> getLabels() {
        return labels;
    }

    @Override
    public CreateContainerCmd withLabels(Map<String, String> labels) {
        checkNotNull(labels, "labels was not specified");
        this.labels = labels;
        return this;
    }

    @Override
    public String getMacAddress() {
        return macAddress;
    }

    @Override
    public CreateContainerCmd withMacAddress(String macAddress) {
        checkNotNull(macAddress, "macAddress was not specified");
        this.macAddress = macAddress;
        return this;
    }

    @Deprecated
    @Override
    @JsonIgnore
    public Long getMemory() {
        return hostConfig.getMemory();
    }

    @Deprecated
    @Override
    public CreateContainerCmd withMemory(Long memory) {
        checkNotNull(memory, "memory was not specified");
        hostConfig.withMemory(memory);
        return this;
    }

    @Deprecated
    @Override
    @JsonIgnore
    public Long getMemorySwap() {
        return hostConfig.getMemorySwap();
    }

    @Deprecated
    @Override
    public CreateContainerCmd withMemorySwap(Long memorySwap) {
        checkNotNull(memorySwap, "memorySwap was not specified");
        hostConfig.withMemorySwap(memorySwap);
        return this;
    }


    @Override
    public String getName() {
        return name;
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public String getNetworkMode() {
        return hostConfig.getNetworkMode();
    }

    @Override
    @Deprecated
    public CreateContainerCmd withNetworkMode(String networkMode) {
        checkNotNull(networkMode, "networkMode was not specified");
        this.hostConfig.withNetworkMode(networkMode);
        return this;
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Ports getPortBindings() {
        return hostConfig.getPortBindings();
    }

    @Override
    @Deprecated
    public CreateContainerCmd withPortBindings(PortBinding... portBindings) {
        checkNotNull(portBindings, "portBindings was not specified");
        this.hostConfig.withPortBindings(new Ports(portBindings));
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withPortBindings(List<PortBinding> portBindings) {
        checkNotNull(portBindings, "portBindings was not specified");
        return withPortBindings(portBindings.toArray(new PortBinding[0]));
    }

    @Override
    @Deprecated
    public CreateContainerCmd withPortBindings(Ports portBindings) {
        checkNotNull(portBindings, "portBindings was not specified");
        this.hostConfig.withPortBindings(portBindings);
        return this;
    }

    @Override
    public CreateContainerCmd withName(String name) {
        checkNotNull(name, "name was not specified");
        this.name = name;
        return this;
    }

    @Override
    public String[] getPortSpecs() {
        return portSpecs;
    }

    @Override
    public CreateContainerCmd withPortSpecs(String... portSpecs) {
        checkNotNull(portSpecs, "portSpecs was not specified");
        this.portSpecs = portSpecs;
        return this;
    }

    @Override
    public CreateContainerCmd withPortSpecs(List<String> portSpecs) {
        checkNotNull(portSpecs, "portSpecs was not specified");
        return withPortSpecs(portSpecs.toArray(new String[0]));
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Boolean getPrivileged() {
        return hostConfig.getPrivileged();
    }

    @Override
    @Deprecated
    public CreateContainerCmd withPrivileged(Boolean privileged) {
        checkNotNull(privileged, "no privileged was specified");
        this.hostConfig.withPrivileged(privileged);
        return this;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public CreateContainerCmd withUser(String user) {
        checkNotNull(user, "user was not specified");
        this.user = user;
        return this;
    }

    @Override
    public Boolean isAttachStderr() {
        return attachStderr;
    }

    @Override
    public CreateContainerCmd withAttachStderr(Boolean attachStderr) {
        checkNotNull(attachStderr, "attachStderr was not specified");
        this.attachStderr = attachStderr;
        return this;
    }

    @Override
    public Boolean isAttachStdin() {
        return attachStdin;
    }

    @Override
    public CreateContainerCmd withAttachStdin(Boolean attachStdin) {
        checkNotNull(attachStdin, "attachStdin was not specified");
        this.attachStdin = attachStdin;
        return this;
    }

    @Override
    public Boolean isAttachStdout() {
        return attachStdout;
    }

    @Override
    public CreateContainerCmd withAttachStdout(Boolean attachStdout) {
        checkNotNull(attachStdout, "attachStdout was not specified");
        this.attachStdout = attachStdout;
        return this;
    }

    @Override
    @JsonIgnore
    public Volume[] getVolumes() {
        return volumes.getVolumes();
    }

    @Override
    public CreateContainerCmd withVolumes(Volume... volumes) {
        checkNotNull(volumes, "volumes was not specified");
        this.volumes = new Volumes(volumes);
        return this;
    }

    @Override
    public CreateContainerCmd withVolumes(List<Volume> volumes) {
        checkNotNull(volumes, "volumes was not specified");
        return withVolumes(volumes.toArray(new Volume[0]));
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public VolumesFrom[] getVolumesFrom() {
        return hostConfig.getVolumesFrom();
    }

    @Override
    @Deprecated
    public CreateContainerCmd withVolumesFrom(VolumesFrom... volumesFrom) {
        checkNotNull(volumesFrom, "volumesFrom was not specified");
        this.hostConfig.withVolumesFrom(volumesFrom);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withVolumesFrom(List<VolumesFrom> volumesFrom) {
        checkNotNull(volumesFrom, "volumesFrom was not specified");
        return withVolumesFrom(volumesFrom.toArray(new VolumesFrom[volumesFrom.size()]));
    }

    @Override
    public String getWorkingDir() {
        return workingDir;
    }

    @Override
    public CreateContainerCmd withWorkingDir(String workingDir) {
        checkNotNull(workingDir, "workingDir was not specified");
        this.workingDir = workingDir;
        return this;
    }

    @Override
    public Boolean isNetworkDisabled() {
        return networkDisabled;
    }

    @Override
    public CreateContainerCmd withNetworkDisabled(Boolean disableNetwork) {
        checkNotNull(disableNetwork, "disableNetwork was not specified");
        this.networkDisabled = disableNetwork;
        return this;
    }


    @Override
    public Boolean isStdInOnce() {
        return stdInOnce;
    }

    @Override
    public CreateContainerCmd withStdInOnce(Boolean stdInOnce) {
        checkNotNull(stdInOnce, "no stdInOnce was specified");
        this.stdInOnce = stdInOnce;
        return this;
    }

    @Override
    public Boolean isStdinOpen() {
        return stdinOpen;
    }

    @Override
    public CreateContainerCmd withStdinOpen(Boolean stdinOpen) {
        checkNotNull(stdinOpen, "no stdinOpen was specified");
        this.stdinOpen = stdinOpen;
        return this;
    }


    @Override
    public Boolean isTty() {
        return tty;
    }

    @Override
    public CreateContainerCmd withTty(Boolean tty) {
        checkNotNull(tty, "no tty was specified");
        this.tty = tty;
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withUlimits(Ulimit... ulimits) {
        hostConfig.withUlimits(ulimits);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withUlimits(List<Ulimit> ulimits) {
        hostConfig.withUlimits(ulimits);
        return this;
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Boolean getPublishAllPorts() {
        return hostConfig.getPublishAllPorts();
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Boolean getReadonlyRootfs() {
        return hostConfig.getReadonlyRootfs();
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public RestartPolicy getRestartPolicy() {
        return hostConfig.getRestartPolicy();
    }

    @Override
    @Deprecated
    public CreateContainerCmd withPublishAllPorts(Boolean publishAllPorts) {
        checkNotNull(publishAllPorts, "no publishAllPorts was specified");
        this.hostConfig.withPublishAllPorts(publishAllPorts);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withReadonlyRootfs(Boolean readonlyRootfs) {
        hostConfig.withReadonlyRootfs(readonlyRootfs);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withRestartPolicy(RestartPolicy restartPolicy) {
        hostConfig.withRestartPolicy(restartPolicy);
        return this;
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public String[] getExtraHosts() {
        return hostConfig.getExtraHosts();
    }

    @Override
    @Deprecated
    public CreateContainerCmd withExtraHosts(String... extraHosts) {
        checkNotNull(extraHosts, "extraHosts was not specified");
        this.hostConfig.withExtraHosts(extraHosts);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withExtraHosts(List<String> extraHosts) {
        checkNotNull(extraHosts, "extraHosts was not specified");
        return withExtraHosts(extraHosts.toArray(new String[extraHosts.size()]));
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Capability[] getCapAdd() {
        return hostConfig.getCapAdd();
    }

    @Override
    @Deprecated
    public CreateContainerCmd withCapAdd(Capability... capAdd) {
        checkNotNull(capAdd, "capAdd was not specified");
        hostConfig.withCapAdd(capAdd);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withCapAdd(List<Capability> capAdd) {
        checkNotNull(capAdd, "capAdd was not specified");
        return withCapAdd(capAdd.toArray(new Capability[capAdd.size()]));
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Capability[] getCapDrop() {
        return hostConfig.getCapDrop();
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public String getCgroupParent() {
        return hostConfig.getCgroupParent();
    }

    @Override
    @Deprecated
    public CreateContainerCmd withCapDrop(Capability... capDrop) {
        checkNotNull(capDrop, "capDrop was not specified");
        hostConfig.withCapDrop(capDrop);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withCapDrop(List<Capability> capDrop) {
        checkNotNull(capDrop, "capDrop was not specified");
        return withCapDrop(capDrop.toArray(new Capability[capDrop.size()]));
    }

    @Override
    @Deprecated
    public CreateContainerCmd withCgroupParent(String cgroupParent) {
        hostConfig.withCgroupParent(cgroupParent);
        return this;
    }

    @Override
    public HostConfig getHostConfig() {
        return hostConfig;
    }

    @Override
    public CreateContainerCmd withHostConfig(HostConfig hostConfig) {
        this.hostConfig = hostConfig;
        return this;
    }

    @Override
    public String getIpv4Address() {
        return ipv4Address;
    }

    @Override
    public CreateContainerCmd withIpv4Address(String ipv4Address) {
        checkNotNull(ipv4Address, "no ipv4Address was specified");
        this.ipv4Address = ipv4Address;
        return this;
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Link[] getLinks() {
        return hostConfig.getLinks();
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public LogConfig getLogConfig() {
        return hostConfig.getLogConfig();
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public LxcConf[] getLxcConf() {
        return hostConfig.getLxcConf();
    }

    @Override
    @Deprecated
    public CreateContainerCmd withLinks(Link... links) {
        checkNotNull(links, "links was not specified");
        this.hostConfig.setLinks(links);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withLinks(List<Link> links) {
        checkNotNull(links, "links was not specified");
        return withLinks(links.toArray(new Link[links.size()]));
    }

    @Override
    @Deprecated
    public CreateContainerCmd withLogConfig(LogConfig logConfig) {
        hostConfig.withLogConfig(logConfig);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withLxcConf(LxcConf... lxcConf) {
        hostConfig.withLxcConf(lxcConf);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withLxcConf(List<LxcConf> lxcConf) {
        hostConfig.withLxcConf(lxcConf.toArray(new LxcConf[0]));
        return this;
    }

    @Override
    public String getIpv6Address() {
        return ipv6Address;
    }

    @Override
    public CreateContainerCmd withIpv6Address(String ipv6Address) {
        checkNotNull(ipv6Address, "no ipv6Address was specified");
        this.ipv6Address = ipv6Address;
        return this;
    }

    @CheckForNull
    public List<String> getOnBuild() {
        return onBuild;
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public Boolean getOomKillDisable() {
        return hostConfig.getOomKillDisable();
    }

    @CheckForNull
    @Override
    @Deprecated
    @JsonIgnore
    public String getPidMode() {
        return hostConfig.getPidMode();
    }

    public CreateContainerCmdImpl withOnBuild(List<String> onBuild) {
        this.onBuild = onBuild;
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withOomKillDisable(Boolean oomKillDisable) {
        hostConfig.withOomKillDisable(oomKillDisable);
        return this;
    }

    @Override
    @Deprecated
    public CreateContainerCmd withPidMode(String pidMode) {
        hostConfig.withPidMode(pidMode);
        return this;
    }

    /**
     * @throws NotFoundException No such container
     * @throws ConflictException Named container already exists
     */
    @Override
    public CreateContainerResponse exec() throws NotFoundException, ConflictException {
        //code flow taken from https://github.com/docker/docker/blob/master/runconfig/opts/parse.go
        ContainerNetwork containerNetwork = null;

        if (ipv4Address != null || ipv6Address != null) {
            containerNetwork = new ContainerNetwork()
                    .withIpamConfig(new ContainerNetwork.Ipam()
                            .withIpv4Address(ipv4Address)
                            .withIpv6Address(ipv6Address)
                    );

        }

        if (hostConfig.isUserDefinedNetwork() && hostConfig.getLinks().length > 0) {
            if (containerNetwork == null) {
                containerNetwork = new ContainerNetwork();
            }

            containerNetwork.withLinks(hostConfig.getLinks());
        }

        if (aliases != null) {
            if (containerNetwork == null) {
                containerNetwork = new ContainerNetwork();
            }

            containerNetwork.withAliases(aliases);
        }

        if (containerNetwork != null) {
            networkingConfig = new NetworkingConfig()
                    .withEndpointsConfig(singletonMap(hostConfig.getNetworkMode(), containerNetwork));
        }

        return super.exec();
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

    public static class NetworkingConfig {
        @JsonProperty("EndpointsConfig")
        public Map<String, ContainerNetwork> endpointsConfig;

        public Map<String, ContainerNetwork> getEndpointsConfig() {
            return endpointsConfig;
        }

        public NetworkingConfig withEndpointsConfig(Map<String, ContainerNetwork> endpointsConfig) {
            this.endpointsConfig = endpointsConfig;
            return this;
        }
    }
}
