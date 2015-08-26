package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Capability;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.ExposedPorts;
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

/**
 *
 * Creates a new container.
 *
 */
@JsonInclude(Include.NON_NULL)
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

    @JsonProperty("NetworkDisabled")
    private Boolean networkDisabled;

    @JsonProperty("ExposedPorts")
    private ExposedPorts exposedPorts = new ExposedPorts();

    @JsonProperty("HostConfig")
    private HostConfig hostConfig = new HostConfig();

    @JsonProperty("Labels")
    private Map<String, String> labels;


    public CreateContainerCmdImpl(CreateContainerCmd.Exec exec, String image) {
        super(exec);
        checkNotNull(image, "image was not specified");
        withImage(image);
    }

    /**
     * @throws NotFoundException
     *             No such container
     * @throws ConflictException
     *             Named container already exists
     */
    @Override
    public CreateContainerResponse exec() throws NotFoundException, ConflictException {
        return super.exec();
    }

    @Override
    @JsonIgnore
    public Bind[] getBinds() {
        return hostConfig.getBinds();
    }

    @Override
    @JsonIgnore
    public Integer getBlkioWeight() {
        return hostConfig.getBlkioWeight();
    }

    @Override
    @JsonIgnore
    public Capability[] getCapAdd() {
        return hostConfig.getCapAdd();
    }

    @Override
    @JsonIgnore
    public Capability[] getCapDrop() {
        return hostConfig.getCapDrop();
    }

    @Override
    public String[] getCmd() {
        return cmd;
    }

    @Override
    @JsonIgnore
    public Integer getCpuPeriod() {
        return hostConfig.getCpuPeriod();
    }

    @Override
    @JsonIgnore
    public String getCpusetCpus() {
        return hostConfig.getCpusetCpus();
    }

    @Override
    @JsonIgnore
    public String getCpusetMems() {
        return hostConfig.getCpusetMems();
    }

    @Override
    @JsonIgnore
    public Integer getCpuShares() {
        return hostConfig.getCpuShares();
    }

    @Override
    @JsonIgnore
    public Device[] getDevices() {
        return hostConfig.getDevices();
    }

    @Override
    @JsonIgnore
    public String[] getDns() {
        return hostConfig.getDns();
    }

    @Override
    @JsonIgnore
    public String[] getDnsSearch() {
        return hostConfig.getDnsSearch();
    }

    @Override
    public String getDomainName() {
        return domainName;
    }

    @Override
    public String[] getEntrypoint() {
        return entrypoint;
    }

    @Override
    public String[] getEnv() {
        return env;
    }

    @Override
    @JsonIgnore
    public ExposedPort[] getExposedPorts() {
        return exposedPorts.getExposedPorts();
    }

    @Override
    @JsonIgnore
    public String[] getExtraHosts() {
        return hostConfig.getExtraHosts();
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    @JsonIgnore
    public Map<String, String> getLabels() {
        return labels;
    }

    @Override
    @JsonIgnore
    public Link[] getLinks() {
        return hostConfig.getLinks();
    }

    @Override
    @JsonIgnore
    public LxcConf[] getLxcConf() {
        return hostConfig.getLxcConf();
    }

    @Override
    @JsonIgnore
    public LogConfig getLogConfig() {
        return hostConfig.getLogConfig();
    }

    @Override
    public String getMacAddress() {
        return macAddress;
    }

    @Override
    @JsonIgnore
    public Long getMemory() {
        return hostConfig.getMemory();
    }

    @Override
    @JsonIgnore
    public Long getMemorySwap() {
        return hostConfig.getMemorySwap();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @JsonIgnore
    public String getNetworkMode() {
        return hostConfig.getNetworkMode();
    }

    @Override
    @JsonIgnore
    public Ports getPortBindings() {
        return hostConfig.getPortBindings();
    }

    @Override
    public String[] getPortSpecs() {
        return portSpecs;
    }

    @Override
    @JsonIgnore
    public RestartPolicy getRestartPolicy() {
        return hostConfig.getRestartPolicy();
    }

    @Override
    @JsonIgnore
    public Ulimit[] getUlimits() {
        return hostConfig.getUlimits();
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    @JsonIgnore
    public Volume[] getVolumes() {
        return volumes.getVolumes();
    }

    @Override
    @JsonIgnore
    public VolumesFrom[] getVolumesFrom() {
        return hostConfig.getVolumesFrom();
    }

    @Override
    public String getWorkingDir() {
        return workingDir;
    }

    @Override
    public Boolean isAttachStderr() {
        return attachStderr;
    }

    @Override
    public Boolean isAttachStdin() {
        return attachStdin;
    }

    @Override
    public Boolean isAttachStdout() {
        return attachStdout;
    }

    @Override
    public Boolean isNetworkDisabled() {
        return networkDisabled;
    }

    @Override
    @JsonIgnore
    public Boolean isOomKillDisable() {
        return hostConfig.isOomKillDisable();
    }

    @Override
    @JsonIgnore
    public Boolean isPrivileged() {
        return hostConfig.isPrivileged();
    }

    @Override
    @JsonIgnore
    public Boolean isPublishAllPorts() {
        return hostConfig.isPublishAllPorts();
    }

    @Override
    @JsonIgnore
    public Boolean isReadonlyRootfs() {
        return hostConfig.isReadonlyRootfs();
    }

    @Override
    public Boolean isStdInOnce() {
        return stdInOnce;
    }

    @Override
    public Boolean isStdinOpen() {
        return stdinOpen;
    }

    @Override
    public Boolean isTty() {
        return tty;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("create container ").append(name != null ? "name=" + name + " " : "")
                .append(this).toString();
    }

    @Override
    public CreateContainerCmdImpl withAttachStderr(Boolean attachStderr) {
        checkNotNull(attachStderr, "attachStderr was not specified");
        this.attachStderr = attachStderr;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withAttachStdin(Boolean attachStdin) {
        checkNotNull(attachStdin, "attachStdin was not specified");
        this.attachStdin = attachStdin;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withAttachStdout(Boolean attachStdout) {
        checkNotNull(attachStdout, "attachStdout was not specified");
        this.attachStdout = attachStdout;
        return this;
    }

    @Override
    public CreateContainerCmd withBinds(Bind... binds) {
        checkNotNull(binds, "binds was not specified");
        hostConfig.setBinds(binds);
        return this;
    }

    @Override
    public CreateContainerCmd withBlkioWeight(Integer blkioWeight) {
        checkNotNull(blkioWeight, "blkioWeight was not specified");
        hostConfig.setBlkioWeight(blkioWeight);
        return this;
    }

    @Override
    public CreateContainerCmd withCapAdd(Capability... capAdd) {
        checkNotNull(capAdd, "capAdd was not specified");
        hostConfig.setCapAdd(capAdd);
        return this;
    }

    @Override
    public CreateContainerCmd withCapDrop(Capability... capDrop) {
        checkNotNull(capDrop, "capDrop was not specified");
        hostConfig.setCapDrop(capDrop);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withCmd(String... cmd) {
        checkNotNull(cmd, "cmd was not specified");
        this.cmd = cmd;
        return this;
    }

    @Override
    public CreateContainerCmd withContainerIDFile(String containerIDFile) {
        checkNotNull(containerIDFile, "no containerIDFile was specified");
        hostConfig.setContainerIDFile(containerIDFile);
        return this;
    }

    @Override
    public CreateContainerCmd withCpuPeriod(Integer cpuPeriod) {
        checkNotNull(cpuPeriod, "cpuPeriod was not specified");
        hostConfig.setCpuPeriod(cpuPeriod);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withCpusetCpus(String cpusetCpus) {
        checkNotNull(cpusetCpus, "cpusetCpus was not specified");
        hostConfig.setCpusetCpus(cpusetCpus);
        return this;
    }

    @Override
    public CreateContainerCmd withCpusetMems(String cpusetMems) {
        checkNotNull(cpusetMems, "cpusetMems was not specified");
        hostConfig.setCpusetMems(cpusetMems);
        return null;
    }

    @Override
    public CreateContainerCmdImpl withCpuShares(Integer cpuShares) {
        checkNotNull(cpuShares, "cpuShares was not specified");
        hostConfig.setCpuShares(cpuShares);
        return this;
    }

    @Override
    public CreateContainerCmd withDevices(Device... devices) {
        checkNotNull(devices, "devices was not specified");
        this.hostConfig.setDevices(devices);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withDns(String... dns) {
        checkNotNull(dns, "dns was not specified");
        this.hostConfig.setDns(dns);
        return this;
    }

    @Override
    public CreateContainerCmd withDnsSearch(String... dnsSearch) {
        checkNotNull(dnsSearch, "dnsSearch was not specified");
        this.hostConfig.setDnsSearch(dnsSearch);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withDomainName(String domainName) {
        checkNotNull(domainName, "no domainName was specified");
        this.domainName = domainName;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withEntrypoint(String... entrypoint) {
        checkNotNull(entrypoint, "entrypoint was not specified");
        this.entrypoint = entrypoint;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withEnv(String... env) {
        checkNotNull(env, "env was not specified");
        this.env = env;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withExposedPorts(ExposedPort... exposedPorts) {
        checkNotNull(exposedPorts, "exposedPorts was not specified");
        this.exposedPorts = new ExposedPorts(exposedPorts);
        return this;
    }

    @Override
    public CreateContainerCmd withExtraHosts(String... extraHosts) {
        checkNotNull(extraHosts, "extraHosts was not specified");
        this.hostConfig.setExtraHosts(extraHosts);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withHostName(String hostName) {
        checkNotNull(hostConfig, "no hostName was specified");
        this.hostName = hostName;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withImage(String image) {
        checkNotNull(image, "no image was specified");
        this.image = image;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withLabels(Map<String, String> labels) {
        checkNotNull(labels, "labels was not specified");
        this.labels = labels;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withLinks(Link... links) {
        checkNotNull(links, "links was not specified");
        this.hostConfig.setLinks(links);
        return this;
    }

    @Override
    public CreateContainerCmd withLxcConf(LxcConf... lxcConf) {
        checkNotNull(lxcConf, "lxcConf was not specified");
        this.hostConfig.setLxcConf(lxcConf);
        return this;
    }

    @Override
    public CreateContainerCmd withLogConfig(LogConfig logConfig) {
        checkNotNull(logConfig, "logConfig was not specified");
        this.hostConfig.setLogConfig(logConfig);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withMacAddress(String macAddress) {
        checkNotNull(macAddress, "macAddress was not specified");
        this.macAddress = macAddress;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withMemory(Long memory) {
        checkNotNull(memory, "memory was not specified");
        hostConfig.setMemory(memory);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withMemorySwap(Long memorySwap) {
        checkNotNull(memorySwap, "memorySwap was not specified");
        hostConfig.setMemorySwap(memorySwap);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withName(String name) {
        checkNotNull(name, "name was not specified");
        this.name = name;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withNetworkDisabled(Boolean disableNetwork) {
        checkNotNull(disableNetwork, "disableNetwork was not specified");
        this.networkDisabled = disableNetwork;
        return this;
    }

    @Override
    public CreateContainerCmd withNetworkMode(String networkMode) {
        checkNotNull(networkMode, "networkMode was not specified");
        this.hostConfig.setNetworkMode(networkMode);
        return this;
    }

    @Override
    public CreateContainerCmd withOomKillDisable(Boolean oomKillDisable) {
        checkNotNull(oomKillDisable, "oomKillDisable was not specified");
        hostConfig.setOomKillDisable(oomKillDisable);
        return this;
    }

    @Override
    public CreateContainerCmd withPortBindings(PortBinding... portBindings) {
        checkNotNull(portBindings, "portBindings was not specified");
        this.hostConfig.setPortBindings(new Ports(portBindings));
        return this;
    }

    @Override
    public CreateContainerCmd withPortBindings(Ports portBindings) {
        checkNotNull(portBindings, "portBindings was not specified");
        this.hostConfig.setPortBindings(portBindings);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withPortSpecs(String... portSpecs) {
        checkNotNull(portSpecs, "portSpecs was not specified");
        this.portSpecs = portSpecs;
        return this;
    }

    @Override
    public CreateContainerCmd withPrivileged(Boolean privileged) {
        checkNotNull(privileged, "no privileged was specified");
        this.hostConfig.setPrivileged(privileged);
        return this;
    }

    @Override
    public CreateContainerCmd withPublishAllPorts(Boolean publishAllPorts) {
        checkNotNull(publishAllPorts, "no publishAllPorts was specified");
        this.hostConfig.setPublishAllPorts(publishAllPorts);
        return this;
    }

    @Override
    public CreateContainerCmd withReadonlyRootfs(Boolean readonlyRootfs) {
        checkNotNull(readonlyRootfs, "no readonlyRootfs was specified");
        hostConfig.setReadonlyRootfs(readonlyRootfs);
        return this;
    }

    @Override
    public CreateContainerCmd withRestartPolicy(RestartPolicy restartPolicy) {
        checkNotNull(restartPolicy, "restartPolicy was not specified");
        this.hostConfig.setRestartPolicy(restartPolicy);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withStdInOnce(Boolean stdInOnce) {
        checkNotNull(stdInOnce, "no stdInOnce was specified");
        this.stdInOnce = stdInOnce;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withStdinOpen(Boolean stdinOpen) {
        checkNotNull(stdinOpen, "no stdinOpen was specified");
        this.stdinOpen = stdinOpen;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withTty(Boolean tty) {
        checkNotNull(tty, "no tty was specified");
        this.tty = tty;
        return this;
    }

    @Override
    public CreateContainerCmd withUlimits(Ulimit[] ulimits) {
        checkNotNull(ulimits, "no ulimits was specified");
        hostConfig.setUlimits(ulimits);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withUser(String user) {
        checkNotNull(user, "user was not specified");
        this.user = user;
        return this;
    }

    @Override
    public CreateContainerCmdImpl withVolumes(Volume... volumes) {
        checkNotNull(volumes, "volumes was not specified");
        this.volumes = new Volumes(volumes);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withVolumesFrom(VolumesFrom... volumesFrom) {
        checkNotNull(volumesFrom, "volumesFrom was not specified");
        this.hostConfig.setVolumesFrom(volumesFrom);
        return this;
    }

    @Override
    public CreateContainerCmdImpl withWorkingDir(String workingDir) {
        checkNotNull(workingDir, "workingDir was not specified");
        this.workingDir = workingDir;
        return this;
    }

}
