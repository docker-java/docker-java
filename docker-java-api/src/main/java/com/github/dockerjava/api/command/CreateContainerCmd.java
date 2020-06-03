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

public interface CreateContainerCmd extends SyncDockerCmd<CreateContainerResponse> {

    @CheckForNull
    AuthConfig getAuthConfig();

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

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Bind[] getBinds() {
        return getHostConfig().getBinds();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withBinds(Bind... binds) {
        Objects.requireNonNull(binds, "binds was not specified");
        getHostConfig().setBinds(binds);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
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

    @CheckForNull
    String[] getCmd();

    CreateContainerCmd withCmd(String... cmd);

    CreateContainerCmd withCmd(List<String> cmd);

    @CheckForNull
    HealthCheck getHealthcheck();

    CreateContainerCmd withHealthcheck(HealthCheck healthCheck);

    @CheckForNull
    Boolean getArgsEscaped();

    CreateContainerCmd withArgsEscaped(Boolean argsEscaped);

    @CheckForNull
    String getDomainName();

    CreateContainerCmd withDomainName(String domainName);

    @CheckForNull
    String[] getEntrypoint();

    CreateContainerCmd withEntrypoint(String... entrypoint);

    CreateContainerCmd withEntrypoint(List<String> entrypoint);

    @CheckForNull
    String[] getEnv();

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

    @CheckForNull
    ExposedPort[] getExposedPorts();

    CreateContainerCmd withExposedPorts(List<ExposedPort> exposedPorts);

    CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts);

    @CheckForNull
    String getStopSignal();

    CreateContainerCmd withStopSignal(String stopSignal);

    @CheckForNull
    Integer getStopTimeout();

    CreateContainerCmd withStopTimeout(Integer stopTimeout);

    @CheckForNull
    String getHostName();

    CreateContainerCmd withHostName(String hostName);

    @CheckForNull
    String getImage();

    CreateContainerCmd withImage(String image);

    @CheckForNull
    String getIpv4Address();

    CreateContainerCmd withIpv4Address(String ipv4Address);

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Link[] getLinks() {
        return getHostConfig().getLinks();
    }

    /**
     * Add link to another container.
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withLinks(Link... links) {
        requireNonNull(links, "links was not specified");
        getHostConfig().setLinks(links);
        return this;
    }

    /**
     * Add link to another container.
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withLinks(List<Link> links) {
        requireNonNull(links, "links was not specified");
        return withLinks(links.toArray(new Link[links.size()]));
    }

    @CheckForNull
    String getIpv6Address();

    CreateContainerCmd withIpv6Address(String ipv6Address);

    @CheckForNull
    Map<String, String> getLabels();

    CreateContainerCmd withLabels(Map<String, String> labels);

    @CheckForNull
    String getMacAddress();

    CreateContainerCmd withMacAddress(String macAddress);

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Long getMemory() {
        return getHostConfig().getMemory();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withMemory(Long memory) {
        Objects.requireNonNull(memory, "memory was not specified");
        getHostConfig().withMemory(memory);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Long getMemorySwap() {
        return getHostConfig().getMemorySwap();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withMemorySwap(Long memorySwap) {
        Objects.requireNonNull(memorySwap, "memorySwap was not specified");
        getHostConfig().withMemorySwap(memorySwap);
        return this;
    }

    @CheckForNull
    String getName();

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
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
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withNetworkMode(String networkMode) {
        Objects.requireNonNull(networkMode, "networkMode was not specified");
        getHostConfig().withNetworkMode(networkMode);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Ports getPortBindings() {
        return getHostConfig().getPortBindings();
    }

    /**
     * Add one or more {@link PortBinding}s. This corresponds to the <code>--publish</code> (<code>-p</code>) option of the
     * <code>docker run</code> CLI command.
     *
     * @deprecated see {@link #getHostConfig()}
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
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withPortBindings(List<PortBinding> portBindings) {
        Objects.requireNonNull(portBindings, "portBindings was not specified");
        return withPortBindings(portBindings.toArray(new PortBinding[0]));
    }

    /**
     * Add the port bindings that are contained in the given {@link Ports} object.
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withPortBindings(Ports portBindings) {
        Objects.requireNonNull(portBindings, "portBindings was not specified");
        getHostConfig().withPortBindings(portBindings);
        return this;
    }

    CreateContainerCmd withName(String name);

    @CheckForNull
    String[] getPortSpecs();

    CreateContainerCmd withPortSpecs(String... portSpecs);

    CreateContainerCmd withPortSpecs(List<String> portSpecs);

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Boolean getPrivileged() {
        return getHostConfig().getPrivileged();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withPrivileged(Boolean privileged) {
        Objects.requireNonNull(privileged, "no privileged was specified");
        getHostConfig().withPrivileged(privileged);
        return this;
    }

    @CheckForNull
    String getUser();

    CreateContainerCmd withUser(String user);

    @CheckForNull
    Volume[] getVolumes();

    CreateContainerCmd withVolumes(Volume... volumes);

    CreateContainerCmd withVolumes(List<Volume> volumes);

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default VolumesFrom[] getVolumesFrom() {
        return getHostConfig().getVolumesFrom();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withVolumesFrom(VolumesFrom... volumesFrom) {
        Objects.requireNonNull(volumesFrom, "volumesFrom was not specified");
        getHostConfig().withVolumesFrom(volumesFrom);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withVolumesFrom(List<VolumesFrom> volumesFrom) {
        requireNonNull(volumesFrom, "volumesFrom was not specified");
        return withVolumesFrom(volumesFrom.toArray(new VolumesFrom[volumesFrom.size()]));
    }

    @CheckForNull
    String getWorkingDir();

    CreateContainerCmd withWorkingDir(String workingDir);

    @CheckForNull
    Boolean isAttachStderr();

    CreateContainerCmd withAttachStderr(Boolean attachStderr);

    @CheckForNull
    Boolean isAttachStdin();

    CreateContainerCmd withAttachStdin(Boolean attachStdin);

    @CheckForNull
    Boolean isAttachStdout();

    CreateContainerCmd withAttachStdout(Boolean attachStdout);

    @CheckForNull
    Boolean isNetworkDisabled();

    CreateContainerCmd withNetworkDisabled(Boolean disableNetwork);

    @CheckForNull
    Boolean isStdInOnce();

    CreateContainerCmd withStdInOnce(Boolean stdInOnce);

    @CheckForNull
    Boolean isStdinOpen();

    CreateContainerCmd withStdinOpen(Boolean stdinOpen);

    @CheckForNull
    Boolean isTty();

    CreateContainerCmd withTty(Boolean tty);

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Boolean getPublishAllPorts() {
        return getHostConfig().getPublishAllPorts();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withPublishAllPorts(Boolean publishAllPorts) {
        requireNonNull(publishAllPorts, "no publishAllPorts was specified");
        getHostConfig().withPublishAllPorts(publishAllPorts);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @CheckForNull
    @Deprecated
    @JsonIgnore
    default String[] getExtraHosts() {
        return getHostConfig().getExtraHosts();
    }

    /**
     * Add hostnames to /etc/hosts in the container
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withExtraHosts(String... extraHosts) {
        requireNonNull(extraHosts, "extraHosts was not specified");
        getHostConfig().withExtraHosts(extraHosts);
        return this;
    }

    /**
     * Add hostnames to /etc/hosts in the container
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withExtraHosts(List<String> extraHosts) {
        requireNonNull(extraHosts, "extraHosts was not specified");
        return withExtraHosts(extraHosts.toArray(new String[extraHosts.size()]));
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @CheckForNull
    @Deprecated
    @JsonIgnore
    default Capability[] getCapAdd() {
        return getHostConfig().getCapAdd();
    }

    /**
     * Add linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> to the container. For example:
     * adding {@link Capability#MKNOD} allows the container to create special files using the 'mknod' command.
     *
     * @deprecated see {@link #getHostConfig()}
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
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withCapAdd(List<Capability> capAdd) {
        requireNonNull(capAdd, "capAdd was not specified");
        return withCapAdd(capAdd.toArray(new Capability[capAdd.size()]));
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @CheckForNull
    @Deprecated
    @JsonIgnore
    default Capability[] getCapDrop() {
        return getHostConfig().getCapDrop();
    }

    /**
     * Drop linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> from the container. For example:
     * dropping {@link Capability#CHOWN} prevents the container from changing the owner of any files.
     *
     * @deprecated see {@link #getHostConfig()}
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
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withCapDrop(List<Capability> capDrop) {
        requireNonNull(capDrop, "capDrop was not specified");
        return withCapDrop(capDrop.toArray(new Capability[capDrop.size()]));
    }


    @CheckForNull
    List<String> getOnBuild();

    CreateContainerCmd withOnBuild(List<String> onBuild);

    @CheckForNull
    HostConfig getHostConfig();

    CreateContainerCmd withHostConfig(HostConfig hostConfig);

    // The following methods are deprecated and should be set on {@link #getHostConfig()} instead.
    // TODO remove in the next big release

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Integer getBlkioWeight() {
        return getHostConfig().getBlkioWeight();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @CheckForNull
    @Deprecated
    @JsonIgnore
    default String getCgroupParent() {
        return getHostConfig().getCgroupParent();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Integer getCpuPeriod() {
        Long result = getHostConfig().getCpuPeriod();
        return result != null ? result.intValue() : null;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Integer getCpuShares() {
        return getHostConfig().getCpuShares();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default String getCpusetCpus() {
        return getHostConfig().getCpusetCpus();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default String getCpusetMems() {
        return getHostConfig().getCpusetMems();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Device[] getDevices() {
        return getHostConfig().getDevices();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default String[] getDns() {
        return getHostConfig().getDns();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default String[] getDnsSearch() {
        return getHostConfig().getDnsSearch();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default LogConfig getLogConfig() {
        return getHostConfig().getLogConfig();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default LxcConf[] getLxcConf() {
        return getHostConfig().getLxcConf();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Boolean getOomKillDisable() {
        return getHostConfig().getOomKillDisable();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default String getPidMode() {
        return getHostConfig().getPidMode();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Boolean getReadonlyRootfs() {
        return getHostConfig().getReadonlyRootfs();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default RestartPolicy getRestartPolicy() {
        return getHostConfig().getRestartPolicy();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @CheckForNull
    @JsonIgnore
    default Ulimit[] getUlimits() {
        return getHostConfig().getUlimits();
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withBlkioWeight(Integer blkioWeight) {
        getHostConfig().withBlkioWeight(blkioWeight);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withCgroupParent(String cgroupParent) {
        getHostConfig().withCgroupParent(cgroupParent);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withContainerIDFile(String containerIDFile) {
        getHostConfig().withContainerIDFile(containerIDFile);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withCpuPeriod(Integer cpuPeriod) {
        getHostConfig().withCpuPeriod(cpuPeriod != null ? cpuPeriod.longValue() : null);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withCpuShares(Integer cpuShares) {
        getHostConfig().withCpuShares(cpuShares);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withCpusetCpus(String cpusetCpus) {
        getHostConfig().withCpusetCpus(cpusetCpus);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withCpusetMems(String cpusetMems) {
        getHostConfig().withCpusetMems(cpusetMems);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withDevices(Device... devices) {
        getHostConfig().withDevices(devices);
        return this;
    }

    /**
     * Add host devices to the container
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withDevices(List<Device> devices) {
        getHostConfig().withDevices(devices);
        return this;
    }

    /**
     * Set custom DNS servers
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withDns(String... dns) {
        getHostConfig().withDns(dns);
        return this;
    }

    /**
     * Set custom DNS servers
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withDns(List<String> dns) {
        getHostConfig().withDns(dns);
        return this;
    }

    /**
     * Set custom DNS search domains
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withDnsSearch(String... dnsSearch) {
        getHostConfig().withDnsSearch(dnsSearch);
        return this;
    }

    /**
     * Set custom DNS search domains
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withDnsSearch(List<String> dnsSearch) {
        getHostConfig().withDnsSearch(dnsSearch);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withLogConfig(LogConfig logConfig) {
        getHostConfig().withLogConfig(logConfig);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withLxcConf(LxcConf... lxcConf) {
        getHostConfig().withLxcConf(lxcConf);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withLxcConf(List<LxcConf> lxcConf) {
        getHostConfig().withLxcConf(lxcConf.toArray(new LxcConf[0]));
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withOomKillDisable(Boolean oomKillDisable) {
        getHostConfig().withOomKillDisable(oomKillDisable);
        return this;
    }

    /**
     * Set the PID (Process) Namespace mode for the container, 'host': use the host's PID namespace inside the container
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withPidMode(String pidMode) {
        getHostConfig().withPidMode(pidMode);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withReadonlyRootfs(Boolean readonlyRootfs) {
        getHostConfig().withReadonlyRootfs(readonlyRootfs);
        return this;
    }

    /**
     * Set custom {@link RestartPolicy} for the container. Defaults to {@link RestartPolicy#noRestart()}
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    default CreateContainerCmd withRestartPolicy(RestartPolicy restartPolicy) {
        getHostConfig().withRestartPolicy(restartPolicy);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
    @Deprecated
    @JsonIgnore
    default CreateContainerCmd withUlimits(Ulimit... ulimits) {
        getHostConfig().withUlimits(ulimits);
        return this;
    }

    /**
     *
     * @deprecated see {@link #getHostConfig()}
     */
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
