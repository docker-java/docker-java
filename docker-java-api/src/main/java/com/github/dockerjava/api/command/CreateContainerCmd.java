package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Capability;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.ExposedPort;
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
import com.github.dockerjava.api.model.VolumesFrom;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@DockerCommand
public interface CreateContainerCmd extends CreateContainer, SyncDockerCmd<CreateContainerResponse> {

    /**
     * While using swarm classic, you can provide an optional auth config which will be used to pull images from a private registry,
     * if the swarm node does not already have the docker image.
     * Note: This option does not have any effect in normal docker
     *
     * @param authConfig The optional auth config
     */
    CreateContainerCmd withAuthConfig(AuthConfig authConfig);

    @CheckForNull
    List<String> getAliases();

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Bind[] getBinds() {
        return getHostConfig().getBinds();
    }

    @Deprecated
    default CreateContainerCmd withBinds(Bind... binds) {
        Objects.requireNonNull(binds, "binds was not specified");
        getHostConfig().setBinds(binds);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withBinds(List<Bind> binds) {
        Objects.requireNonNull(binds, "binds was not specified");
        return withBinds(binds.toArray(new Bind[binds.size()]));
    }

    /**
     * Add network-scoped alias for the container
     *
     * @param aliases on ore more aliases
     */
    CreateContainerCmd withAliases(List<String> aliases);

    /**
     * Add network-scoped alias for the container
     *
     * @param aliases on ore more aliases
     */
    CreateContainerCmd withAliases(String... aliases);

    CreateContainerCmd withCmd(String... cmd);

    CreateContainerCmd withCmd(List<String> cmd);

    CreateContainerCmd withHealthcheck(HealthCheck healthCheck);

    CreateContainerCmd withArgsEscaped(Boolean argsEscaped);

    CreateContainerCmd withDomainName(String domainName);

    CreateContainerCmd withEntrypoint(String... entrypoint);

    CreateContainerCmd withEntrypoint(List<String> entrypoint);

    /**
     * Adds environment-variables. NB: Not additive, i.e. in case of multiple calls to the method, only the most recent
     * values will be injected. Prior env-variables will be deleted.
     *
     * @param env the String(s) to set as ENV in the container
     */
    CreateContainerCmd withEnv(String... env);

    /**
     * Adds environment-variables. NB: Not additive, i.e. in case of multiple calls to the method, only the most recent
     * values will be injected. Prior env-variables will be deleted.
     *
     * @param env the list of Strings to set as ENV in the container
     */
    CreateContainerCmd withEnv(List<String> env);

    CreateContainerCmd withExposedPorts(List<ExposedPort> exposedPorts);

    CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts);

    CreateContainerCmd withStopSignal(String stopSignal);

    CreateContainerCmd withStopTimeout(Integer stopTimeout);

    CreateContainerCmd withHostName(String hostName);

    CreateContainerCmd withImage(String image);

    CreateContainerCmd withIpv4Address(String ipv4Address);

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Link[] getLinks() {
        return getHostConfig().getLinks();
    }

    /**
     * Add link to another container.
     */
    @Deprecated
    default CreateContainerCmd withLinks(Link... links) {
        requireNonNull(links, "links was not specified");
        getHostConfig().setLinks(links);
        return this;
    }

    /**
     * Add link to another container.
     */
    @Deprecated
    default CreateContainerCmd withLinks(List<Link> links) {
        requireNonNull(links, "links was not specified");
        return withLinks(links.toArray(new Link[links.size()]));
    }

    @CheckForNull
    String getIpv6Address();

    CreateContainerCmd withIpv6Address(String ipv6Address);

    CreateContainerCmd withLabels(Map<String, String> labels);

    CreateContainerCmd withMacAddress(String macAddress);

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Long getMemory() {
        return getHostConfig().getMemory();
    }

    @Deprecated
    default CreateContainerCmd withMemory(Long memory) {
        Objects.requireNonNull(memory, "memory was not specified");
        getHostConfig().withMemory(memory);
        return this;
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Long getMemorySwap() {
        return getHostConfig().getMemorySwap();
    }

    @Deprecated
    default CreateContainerCmd withMemorySwap(Long memorySwap) {
        Objects.requireNonNull(memorySwap, "memorySwap was not specified");
        getHostConfig().withMemorySwap(memorySwap);
        return this;
    }

    @CheckForNull
    String getName();

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default String getNetworkMode() {
        return getHostConfig().getNetworkMode();
    }

    /**
     * Set the Network mode for the container
     * <ul>
     * <li>'bridge': creates a new network stack for the container on the docker bridge</li>
     * <li>'none': no networking for this container</li>
     * <li>'container:<name|id>': reuses another container network stack</li>
     * <li>'host': use the host network stack inside the container. Note: the host mode gives the container full access to local system
     * services such as D-bus and is therefore considered insecure.</li>
     * </ul>
     */
    @Deprecated
    default CreateContainerCmd withNetworkMode(String networkMode) {
        Objects.requireNonNull(networkMode, "networkMode was not specified");
        getHostConfig().withNetworkMode(networkMode);
        return this;
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Ports getPortBindings() {
        return getHostConfig().getPortBindings();
    }

    /**
     * Add one or more {@link PortBinding}s. This corresponds to the <code>--publish</code> (<code>-p</code>) option of the
     * <code>docker run</code> CLI command.
     */
    @Deprecated
    default CreateContainerCmd withPortBindings(PortBinding... portBindings) {
        Objects.requireNonNull(portBindings, "portBindings was not specified");
        getHostConfig().withPortBindings(new Ports(portBindings));
        return this;
    }

    /**
     * Add one or more {@link PortBinding}s. This corresponds to the <code>--publish</code> (<code>-p</code>) option of the
     * <code>docker run</code> CLI command.
     */
    @Deprecated
    default CreateContainerCmd withPortBindings(List<PortBinding> portBindings) {
        Objects.requireNonNull(portBindings, "portBindings was not specified");
        return withPortBindings(portBindings.toArray(new PortBinding[0]));
    }

    /**
     * Add the port bindings that are contained in the given {@link Ports} object.
     *
     * @see #withPortBindings(PortBinding...)
     */
    @Deprecated
    default CreateContainerCmd withPortBindings(Ports portBindings) {
        Objects.requireNonNull(portBindings, "portBindings was not specified");
        getHostConfig().withPortBindings(portBindings);
        return this;
    }

    CreateContainerCmd withName(String name);

    CreateContainerCmd withPortSpecs(String... portSpecs);

    CreateContainerCmd withPortSpecs(List<String> portSpecs);

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Boolean getPrivileged() {
        return getHostConfig().getPrivileged();
    }

    @Deprecated
    default CreateContainerCmd withPrivileged(Boolean privileged) {
        Objects.requireNonNull(privileged, "no privileged was specified");
        getHostConfig().withPrivileged(privileged);
        return this;
    }

    @CheckForNull
    String getUser();

    CreateContainerCmd withUser(String user);

    CreateContainerCmd withVolumes(Volume... volumes);

    CreateContainerCmd withVolumes(List<Volume> volumes);

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default VolumesFrom[] getVolumesFrom() {
        return getHostConfig().getVolumesFrom();
    }

    @Deprecated
    default CreateContainerCmd withVolumesFrom(VolumesFrom... volumesFrom) {
        Objects.requireNonNull(volumesFrom, "volumesFrom was not specified");
        getHostConfig().withVolumesFrom(volumesFrom);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withVolumesFrom(List<VolumesFrom> volumesFrom) {
        requireNonNull(volumesFrom, "volumesFrom was not specified");
        return withVolumesFrom(volumesFrom.toArray(new VolumesFrom[volumesFrom.size()]));
    }

    @CheckForNull
    String getWorkingDir();

    CreateContainerCmd withWorkingDir(String workingDir);

    CreateContainerCmd withAttachStderr(Boolean attachStderr);

    CreateContainerCmd withAttachStdin(Boolean attachStdin);

    CreateContainerCmd withAttachStdout(Boolean attachStdout);

    CreateContainerCmd withNetworkDisabled(Boolean disableNetwork);

    CreateContainerCmd withStdInOnce(Boolean stdInOnce);

    CreateContainerCmd withStdinOpen(Boolean stdinOpen);

    CreateContainerCmd withTty(Boolean tty);

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Boolean getPublishAllPorts() {
        return getHostConfig().getPublishAllPorts();
    }

    @Deprecated
    default CreateContainerCmd withPublishAllPorts(Boolean publishAllPorts) {
        requireNonNull(publishAllPorts, "no publishAllPorts was specified");
        getHostConfig().withPublishAllPorts(publishAllPorts);
        return this;
    }

    @CheckForNull
    @Deprecated
    @JsonIgnore
    default String[] getExtraHosts() {
        return getHostConfig().getExtraHosts();
    }

    /**
     * Add hostnames to /etc/hosts in the container
     */
    @Deprecated
    default CreateContainerCmd withExtraHosts(String... extraHosts) {
        requireNonNull(extraHosts, "extraHosts was not specified");
        getHostConfig().withExtraHosts(extraHosts);
        return this;
    }

    /**
     * Add hostnames to /etc/hosts in the container
     */
    @Deprecated
    default CreateContainerCmd withExtraHosts(List<String> extraHosts) {
        requireNonNull(extraHosts, "extraHosts was not specified");
        return withExtraHosts(extraHosts.toArray(new String[extraHosts.size()]));
    }

    @CheckForNull
    @Deprecated
    @JsonIgnore
    default Capability[] getCapAdd() {
        return getHostConfig().getCapAdd();
    }

    /**
     * Add linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> to the container. For example:
     * adding {@link Capability#MKNOD} allows the container to create special files using the 'mknod' command.
     */
    @Deprecated
    default CreateContainerCmd withCapAdd(Capability... capAdd) {
        requireNonNull(capAdd, "capAdd was not specified");
        getHostConfig().withCapAdd(capAdd);
        return this;
    }

    /**
     * Add linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> to the container. For example:
     * adding {@link Capability#MKNOD} allows the container to create special files using the 'mknod' command.
     */
    @Deprecated
    default CreateContainerCmd withCapAdd(List<Capability> capAdd) {
        requireNonNull(capAdd, "capAdd was not specified");
        return withCapAdd(capAdd.toArray(new Capability[capAdd.size()]));
    }

    @CheckForNull
    @Deprecated
    @JsonIgnore
    default Capability[] getCapDrop() {
        return getHostConfig().getCapDrop();
    }

    /**
     * Drop linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> from the container. For example:
     * dropping {@link Capability#CHOWN} prevents the container from changing the owner of any files.
     */
    @Deprecated
    default CreateContainerCmd withCapDrop(Capability... capDrop) {
        requireNonNull(capDrop, "capDrop was not specified");
        getHostConfig().withCapDrop(capDrop);
        return this;
    }

    /**
     * Drop linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> from the container. For example:
     * dropping {@link Capability#CHOWN} prevents the container from changing the owner of any files.
     */
    @Deprecated
    default CreateContainerCmd withCapDrop(List<Capability> capDrop) {
        requireNonNull(capDrop, "capDrop was not specified");
        return withCapDrop(capDrop.toArray(new Capability[capDrop.size()]));
    }


    CreateContainerCmd withOnBuild(List<String> onBuild);

    @CheckForNull
    HostConfig getHostConfig();

    CreateContainerCmd withHostConfig(HostConfig hostConfig);

    // The following methods are deprecated and should be set on {@link #getHostConfig()} instead.
    // TODO remove in the next big release

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Integer getBlkioWeight() {
        return getHostConfig().getBlkioWeight();
    }

    @CheckForNull
    @Deprecated
    @JsonIgnore
    default String getCgroupParent() {
        return getHostConfig().getCgroupParent();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Integer getCpuPeriod() {
        Long result = getHostConfig().getCpuPeriod();
        return result != null ? result.intValue() : null;
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Integer getCpuShares() {
        return getHostConfig().getCpuShares();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default String getCpusetCpus() {
        return getHostConfig().getCpusetCpus();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default String getCpusetMems() {
        return getHostConfig().getCpusetMems();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Device[] getDevices() {
        return getHostConfig().getDevices();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default String[] getDns() {
        return getHostConfig().getDns();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default String[] getDnsSearch() {
        return getHostConfig().getDnsSearch();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default LogConfig getLogConfig() {
        return getHostConfig().getLogConfig();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default LxcConf[] getLxcConf() {
        return getHostConfig().getLxcConf();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Boolean getOomKillDisable() {
        return getHostConfig().getOomKillDisable();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default String getPidMode() {
        return getHostConfig().getPidMode();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Boolean getReadonlyRootfs() {
        return getHostConfig().getReadonlyRootfs();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default RestartPolicy getRestartPolicy() {
        return getHostConfig().getRestartPolicy();
    }

    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Ulimit[] getUlimits() {
        return getHostConfig().getUlimits();
    }

    @Deprecated
    default CreateContainerCmd withBlkioWeight(Integer blkioWeight) {
        getHostConfig().withBlkioWeight(blkioWeight);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withCgroupParent(String cgroupParent) {
        getHostConfig().withCgroupParent(cgroupParent);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withContainerIDFile(String containerIDFile) {
        getHostConfig().withContainerIDFile(containerIDFile);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withCpuPeriod(Integer cpuPeriod) {
        getHostConfig().withCpuPeriod(cpuPeriod != null ? cpuPeriod.longValue() : null);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withCpuShares(Integer cpuShares) {
        getHostConfig().withCpuShares(cpuShares);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withCpusetCpus(String cpusetCpus) {
        getHostConfig().withCpusetCpus(cpusetCpus);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withCpusetMems(String cpusetMems) {
        getHostConfig().withCpusetMems(cpusetMems);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withDevices(Device... devices) {
        getHostConfig().withDevices(devices);
        return this;
    }

    /**
     * Add host devices to the container
     */
    @Deprecated
    default CreateContainerCmd withDevices(List<Device> devices) {
        getHostConfig().withDevices(devices);
        return this;
    }

    /**
     * Set custom DNS servers
     */
    @Deprecated
    default CreateContainerCmd withDns(String... dns) {
        getHostConfig().withDns(dns);
        return this;
    }

    /**
     * Set custom DNS servers
     */
    @Deprecated
    default CreateContainerCmd withDns(List<String> dns) {
        getHostConfig().withDns(dns);
        return this;
    }

    /**
     * Set custom DNS search domains
     */
    @Deprecated
    default CreateContainerCmd withDnsSearch(String... dnsSearch) {
        getHostConfig().withDnsSearch(dnsSearch);
        return this;
    }

    /**
     * Set custom DNS search domains
     */
    @Deprecated
    default CreateContainerCmd withDnsSearch(List<String> dnsSearch) {
        getHostConfig().withDnsSearch(dnsSearch);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withLogConfig(LogConfig logConfig) {
        getHostConfig().withLogConfig(logConfig);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withLxcConf(LxcConf... lxcConf) {
        getHostConfig().withLxcConf(lxcConf);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withLxcConf(List<LxcConf> lxcConf) {
        getHostConfig().withLxcConf(lxcConf.toArray(new LxcConf[0]));
        return this;
    }

    @Deprecated
    default CreateContainerCmd withOomKillDisable(Boolean oomKillDisable) {
        getHostConfig().withOomKillDisable(oomKillDisable);
        return this;
    }

    /**
     * Set the PID (Process) Namespace mode for the container, 'host': use the host's PID namespace inside the container
     */
    @Deprecated
    default CreateContainerCmd withPidMode(String pidMode) {
        getHostConfig().withPidMode(pidMode);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withReadonlyRootfs(Boolean readonlyRootfs) {
        getHostConfig().withReadonlyRootfs(readonlyRootfs);
        return this;
    }

    /**
     * Set custom {@link RestartPolicy} for the container. Defaults to {@link RestartPolicy#noRestart()}
     */
    @Deprecated
    default CreateContainerCmd withRestartPolicy(RestartPolicy restartPolicy) {
        getHostConfig().withRestartPolicy(restartPolicy);
        return this;
    }

    @Deprecated
    @JsonIgnore
    default CreateContainerCmd withUlimits(Ulimit... ulimits) {
        getHostConfig().withUlimits(ulimits);
        return this;
    }

    @Deprecated
    default CreateContainerCmd withUlimits(List<Ulimit> ulimits) {
        getHostConfig().withUlimits(ulimits);
        return this;
    }

    /**
     * @throws NotFoundException No such container
     * @throws ConflictException Named container already exists
     */
    @Override
    CreateContainerResponse exec() throws NotFoundException, ConflictException;

    interface Exec extends DockerCmdSyncExec<CreateContainerCmd, CreateContainerResponse> {
    }
}
