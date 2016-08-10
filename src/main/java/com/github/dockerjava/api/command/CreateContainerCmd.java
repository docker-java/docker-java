package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Capability;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.ExposedPort;
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

public interface CreateContainerCmd extends SyncDockerCmd<CreateContainerResponse> {

    @CheckForNull
    List<String> getAliases();

    @CheckForNull
    Bind[] getBinds();

    /**
     * @since 1.19
     */
    @CheckForNull
    Integer getBlkioWeight();

    @CheckForNull
    Capability[] getCapAdd();

    @CheckForNull
    Capability[] getCapDrop();

    @CheckForNull
    String[] getCmd();

    /**
     * @since 1.19
     */
    @CheckForNull
    Integer getCpuPeriod();

    @CheckForNull
    String getCpusetCpus();

    /**
     * @since 1.19
     */
    @CheckForNull
    String getCpusetMems();

    @CheckForNull
    Integer getCpuShares();

    @CheckForNull
    Device[] getDevices();

    @CheckForNull
    String[] getDns();

    @CheckForNull
    String[] getDnsSearch();

    @CheckForNull
    String getDomainName();

    @CheckForNull
    String[] getEntrypoint();

    @CheckForNull
    String[] getEnv();

    @CheckForNull
    ExposedPort[] getExposedPorts();

    @CheckForNull
    String getStopSignal();

    @CheckForNull
    String[] getExtraHosts();

    @CheckForNull
    String getHostName();

    @CheckForNull
    String getImage();

    @CheckForNull
    String getIpv4Address();

    @CheckForNull
    String getIpv6Address();

    @CheckForNull
    Map<String, String> getLabels();

    @CheckForNull
    Link[] getLinks();

    @CheckForNull
    LogConfig getLogConfig();

    @CheckForNull
    LxcConf[] getLxcConf();

    @CheckForNull
    String getMacAddress();

    @CheckForNull
    Long getMemory();

    @CheckForNull
    Long getMemorySwap();

    @CheckForNull
    String getName();

    @CheckForNull
    String getNetworkMode();

    @CheckForNull
    Ports getPortBindings();

    @CheckForNull
    String[] getPortSpecs();

    @CheckForNull
    RestartPolicy getRestartPolicy();

    @CheckForNull
    Ulimit[] getUlimits();

    @CheckForNull
    String getUser();

    @CheckForNull
    Volume[] getVolumes();

    @CheckForNull
    VolumesFrom[] getVolumesFrom();

    @CheckForNull
    String getWorkingDir();

    @CheckForNull
    Boolean isAttachStderr();

    @CheckForNull
    Boolean isAttachStdin();

    @CheckForNull
    Boolean isAttachStdout();

    @CheckForNull
    Boolean isNetworkDisabled();

    /**
     * @since 1.19
     */
    @CheckForNull
    Boolean getOomKillDisable();

    @CheckForNull
    Boolean getPrivileged();

    @CheckForNull
    Boolean getPublishAllPorts();

    @CheckForNull
    Boolean getReadonlyRootfs();

    @CheckForNull
    Boolean isStdInOnce();

    @CheckForNull
    Boolean isStdinOpen();

    @CheckForNull
    String getPidMode();

    @CheckForNull
    HostConfig getHostConfig();

    @CheckForNull
    String getCgroupParent();

    @CheckForNull
    Boolean isTty();

    /**
     * Add network-scoped alias for the container
     * @param aliases on ore more aliases
     */
    CreateContainerCmd withAliases(String... aliases);

    /**
     * Add network-scoped alias for the container
     * @param aliases on ore more aliases
     */
    CreateContainerCmd withAliases(List<String> aliases);

    CreateContainerCmd withAttachStderr(Boolean attachStderr);

    CreateContainerCmd withAttachStdin(Boolean attachStdin);

    CreateContainerCmd withAttachStdout(Boolean attachStdout);

    CreateContainerCmd withBinds(Bind... binds);

    CreateContainerCmd withBinds(List<Bind> binds);

    /**
     * @since 1.19
     */
    CreateContainerCmd withBlkioWeight(Integer blkioWeight);

    /**
     * Add linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> to the container. For example:
     * adding {@link Capability#MKNOD} allows the container to create special files using the 'mknod' command.
     */
    CreateContainerCmd withCapAdd(Capability... capAdd);

    /**
     * Add linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> to the container. For example:
     * adding {@link Capability#MKNOD} allows the container to create special files using the 'mknod' command.
     */
    CreateContainerCmd withCapAdd(List<Capability> capAdd);

    /**
     * Drop linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> from the container. For example:
     * dropping {@link Capability#CHOWN} prevents the container from changing the owner of any files.
     */
    CreateContainerCmd withCapDrop(Capability... capDrop);

    /**
     * Drop linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> from the container. For example:
     * dropping {@link Capability#CHOWN} prevents the container from changing the owner of any files.
     */
    CreateContainerCmd withCapDrop(List<Capability> capDrop);

    CreateContainerCmd withCmd(String... cmd);

    CreateContainerCmd withCmd(List<String> cmd);

    CreateContainerCmd withContainerIDFile(String containerIDFile);

    /**
     * @since 1.19
     */
    CreateContainerCmd withCpuPeriod(Integer cpuPeriod);

    CreateContainerCmd withCpusetCpus(String cpusetCpus);

    /**
     * @since 1.19
     */
    CreateContainerCmd withCpusetMems(String cpusetMems);

    CreateContainerCmd withCpuShares(Integer cpuShares);

    /**
     * Add host devices to the container
     */
    CreateContainerCmd withDevices(Device... devices);

    /**
     * Add host devices to the container
     */
    CreateContainerCmd withDevices(List<Device> devices);

    /**
     * Set custom DNS servers
     */
    CreateContainerCmd withDns(String... dns);

    /**
     * Set custom DNS servers
     */
    CreateContainerCmd withDns(List<String> dns);

    /**
     * Set custom DNS search domains
     */
    CreateContainerCmd withDnsSearch(String... dnsSearch);

    /**
     * Set custom DNS search domains
     */
    CreateContainerCmd withDnsSearch(List<String> dnsSearch);

    CreateContainerCmd withDomainName(String domainName);

    CreateContainerCmd withEntrypoint(String... entrypoint);

    CreateContainerCmd withEntrypoint(List<String> entrypoint);

    CreateContainerCmd withEnv(String... env);

    CreateContainerCmd withEnv(List<String> env);

    CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts);

    CreateContainerCmd withStopSignal(String stopSignal);

    CreateContainerCmd withExposedPorts(List<ExposedPort> exposedPorts);

    /**
     * Add hostnames to /etc/hosts in the container
     */
    CreateContainerCmd withExtraHosts(String... extraHosts);

    /**
     * Add hostnames to /etc/hosts in the container
     */
    CreateContainerCmd withExtraHosts(List<String> extraHosts);

    CreateContainerCmd withHostName(String hostName);

    CreateContainerCmd withImage(String image);

    CreateContainerCmd withIpv4Address(String ipv4Address);

    CreateContainerCmd withIpv6Address(String ipv6Address);

    CreateContainerCmd withLabels(Map<String, String> labels);

    /**
     * Add link to another container.
     */
    CreateContainerCmd withLinks(Link... links);

    /**
     * Add link to another container.
     */
    CreateContainerCmd withLinks(List<Link> links);

    CreateContainerCmd withLogConfig(LogConfig logConfig);

    CreateContainerCmd withLxcConf(LxcConf... lxcConf);

    CreateContainerCmd withLxcConf(List<LxcConf> lxcConf);

    CreateContainerCmd withMacAddress(String macAddress);

    CreateContainerCmd withMemory(Long memory);

    CreateContainerCmd withMemorySwap(Long memorySwap);

    CreateContainerCmd withName(String name);

    CreateContainerCmd withNetworkDisabled(Boolean disableNetwork);

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
    CreateContainerCmd withNetworkMode(String networkMode);

    /**
     * @since 1.19
     */
    CreateContainerCmd withOomKillDisable(Boolean oomKillDisable);

    /**
     * Add one or more {@link PortBinding}s. This corresponds to the <code>--publish</code> (<code>-p</code>) option of the
     * <code>docker run</code> CLI command.
     */
    CreateContainerCmd withPortBindings(PortBinding... portBindings);

    /**
     * Add one or more {@link PortBinding}s. This corresponds to the <code>--publish</code> (<code>-p</code>) option of the
     * <code>docker run</code> CLI command.
     */
    CreateContainerCmd withPortBindings(List<PortBinding> portBindings);

    /**
     * Add the port bindings that are contained in the given {@link Ports} object.
     *
     * @see #withPortBindings(PortBinding...)
     */
    CreateContainerCmd withPortBindings(Ports portBindings);

    CreateContainerCmd withPortSpecs(String... portSpecs);

    CreateContainerCmd withPortSpecs(List<String> portSpecs);

    CreateContainerCmd withPrivileged(Boolean privileged);

    CreateContainerCmd withPublishAllPorts(Boolean publishAllPorts);

    CreateContainerCmd withReadonlyRootfs(Boolean readonlyRootfs);

    /**
     * Set custom {@link RestartPolicy} for the container. Defaults to {@link RestartPolicy#noRestart()}
     */
    CreateContainerCmd withRestartPolicy(RestartPolicy restartPolicy);

    CreateContainerCmd withStdInOnce(Boolean stdInOnce);

    CreateContainerCmd withStdinOpen(Boolean stdinOpen);

    CreateContainerCmd withTty(Boolean tty);

    CreateContainerCmd withUlimits(Ulimit... ulimits);

    CreateContainerCmd withUlimits(List<Ulimit> ulimits);

    CreateContainerCmd withUser(String user);

    CreateContainerCmd withVolumes(Volume... volumes);

    CreateContainerCmd withVolumes(List<Volume> volumes);

    CreateContainerCmd withVolumesFrom(VolumesFrom... volumesFrom);

    CreateContainerCmd withVolumesFrom(List<VolumesFrom> volumesFrom);

    CreateContainerCmd withWorkingDir(String workingDir);

    CreateContainerCmd withCgroupParent(String cgroupParent);

    /**
     * Set the PID (Process) Namespace mode for the container, 'host': use the host's PID namespace inside the container
     */
    CreateContainerCmd withPidMode(String pidMode);

    CreateContainerCmd withHostConfig(HostConfig hostConfig);

    /**
     * @throws NotFoundException
     *             No such container
     * @throws ConflictException
     *             Named container already exists
     */
    @Override
    CreateContainerResponse exec() throws NotFoundException, ConflictException;

    interface Exec extends DockerCmdSyncExec<CreateContainerCmd, CreateContainerResponse> {
    }
}
