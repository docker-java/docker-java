package com.github.dockerjava.api.command;

import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;

import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Capability;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.LogConfig;
import com.github.dockerjava.api.model.LxcConf;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.Ulimit;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.VolumesFrom;

public interface CreateContainerCmd extends SyncDockerCmd<CreateContainerResponse> {

    @CheckForNull
    public Bind[] getBinds();

    /**
     * @since 1.19
     */
    @CheckForNull
    public Integer getBlkioWeight();

    @CheckForNull
    public Capability[] getCapAdd();

    @CheckForNull
    public Capability[] getCapDrop();

    @CheckForNull
    public String[] getCmd();

    /**
     * @since 1.19
     */
    @CheckForNull
    public Integer getCpuPeriod();

    @CheckForNull
    public String getCpusetCpus();

    /**
     * @since 1.19
     */
    @CheckForNull
    public String getCpusetMems();

    @CheckForNull
    public Integer getCpuShares();

    @CheckForNull
    public Device[] getDevices();

    @CheckForNull
    public String[] getDns();

    @CheckForNull
    public String[] getDnsSearch();

    @CheckForNull
    public String getDomainName();

    @CheckForNull
    public String[] getEntrypoint();

    @CheckForNull
    public String[] getEnv();

    @CheckForNull
    public ExposedPort[] getExposedPorts();

    @CheckForNull
    public String[] getExtraHosts();

    @CheckForNull
    public String getHostName();

    @CheckForNull
    public String getImage();

    @CheckForNull
    Map<String, String> getLabels();

    @CheckForNull
    public Link[] getLinks();

    @CheckForNull
    public LogConfig getLogConfig();

    @CheckForNull
    public LxcConf[] getLxcConf();

    @CheckForNull
    public String getMacAddress();

    @CheckForNull
    public Long getMemory();

    @CheckForNull
    public Long getMemorySwap();

    @CheckForNull
    public String getName();

    @CheckForNull
    public String getNetworkMode();

    @CheckForNull
    public Ports getPortBindings();

    @CheckForNull
    public String[] getPortSpecs();

    @CheckForNull
    public RestartPolicy getRestartPolicy();

    @CheckForNull
    public Ulimit[] getUlimits();

    @CheckForNull
    public String getUser();

    @CheckForNull
    public Volume[] getVolumes();

    @CheckForNull
    public VolumesFrom[] getVolumesFrom();

    @CheckForNull
    public String getWorkingDir();

    @CheckForNull
    public Boolean isAttachStderr();

    @CheckForNull
    public Boolean isAttachStdin();

    @CheckForNull
    public Boolean isAttachStdout();

    @CheckForNull
    public Boolean isNetworkDisabled();

    /**
     * @since 1.19
     */
    @CheckForNull
    public Boolean isOomKillDisable();

    @CheckForNull
    public Boolean isPrivileged();

    @CheckForNull
    public Boolean isPublishAllPorts();

    @CheckForNull
    public Boolean isReadonlyRootfs();

    @CheckForNull
    public Boolean isStdInOnce();

    @CheckForNull
    public Boolean isStdinOpen();

    @CheckForNull
    public Boolean isTty();

    public CreateContainerCmd withAttachStderr(Boolean attachStderr);

    public CreateContainerCmd withAttachStdin(Boolean attachStdin);

    public CreateContainerCmd withAttachStdout(Boolean attachStdout);

    public CreateContainerCmd withBinds(Bind... binds);

    /**
     * @since 1.19
     */
    public CreateContainerCmd withBlkioWeight(Integer blkioWeight);

    /**
     * Add linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> to the
     * container. For example: adding {@link Capability#MKNOD} allows the container to create special files using the
     * 'mknod' command.
     */
    public CreateContainerCmd withCapAdd(Capability... capAdd);

    /**
     * Drop linux <a href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel capability</a> from the
     * container. For example: dropping {@link Capability#CHOWN} prevents the container from changing the owner of any
     * files.
     */
    public CreateContainerCmd withCapDrop(Capability... capDrop);

    public CreateContainerCmd withCmd(String... cmd);

    public CreateContainerCmd withContainerIDFile(String containerIDFile);

    /**
     * @since 1.19
     */
    public CreateContainerCmd withCpuPeriod(Integer cpuPeriod);

    public CreateContainerCmd withCpusetCpus(String cpusetCpus);

    /**
     * @since 1.19
     */
    public CreateContainerCmd withCpusetMems(String cpusetMems);

    public CreateContainerCmd withCpuShares(Integer cpuShares);

    /**
     * Add host devices to the container
     */
    public CreateContainerCmd withDevices(Device... devices);

    /**
     * Add host devices to the container
     */
    public CreateContainerCmd withDevices(List<Device> devices);

    /**
     * Set custom DNS servers
     */
    public CreateContainerCmd withDns(String... dns);

    /**
     * Set custom DNS servers
     */
    public CreateContainerCmd withDns(List<String> dns);

    /**
     * Set custom DNS search domains
     */
    public CreateContainerCmd withDnsSearch(String... dnsSearch);

    /**
     * Set custom DNS search domains
     */
    public CreateContainerCmd withDnsSearch(List<String> dnsSearch);

    public CreateContainerCmd withDomainName(String domainName);

    public CreateContainerCmd withEntrypoint(String... entrypoint);

    public CreateContainerCmd withEntrypoint(List<String> entrypoint);

    public CreateContainerCmd withEnv(String... env);

    public CreateContainerCmd withEnv(List<String> env);

    public CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts);

    public CreateContainerCmd withExposedPorts(List<ExposedPort> exposedPorts);

    /**
     * Add hostnames to /etc/hosts in the container
     */
    public CreateContainerCmd withExtraHosts(String... extraHosts);

    /**
     * Add hostnames to /etc/hosts in the container
     */
    public CreateContainerCmd withExtraHosts(List<String> extraHosts);

    public CreateContainerCmd withHostName(String hostName);

    public CreateContainerCmd withImage(String image);

    public CreateContainerCmd withLabels(Map<String, String> labels);

    /**
     * Add link to another container.
     */
    public CreateContainerCmd withLinks(Link... links);

    /**
     * Add link to another container.
     */
    public CreateContainerCmd withLinks(List<Link> links);

    public CreateContainerCmd withLogConfig(LogConfig logConfig);

    public CreateContainerCmd withLxcConf(LxcConf... lxcConf);

    public CreateContainerCmd withLxcConf(List<LxcConf> lxcConf);

    public CreateContainerCmd withMacAddress(String macAddress);

    public CreateContainerCmd withMemory(Long memory);

    public CreateContainerCmd withMemorySwap(Long memorySwap);

    public CreateContainerCmd withName(String name);

    public CreateContainerCmd withNetworkDisabled(Boolean disableNetwork);

    /**
     * Set the Network mode for the container
     * <ul>
     * <li>'bridge': creates a new network stack for the container on the docker bridge</li>
     * <li>'none': no networking for this container</li>
     * <li>'container:<name|id>': reuses another container network stack</li>
     * <li>'host': use the host network stack inside the container. Note: the host mode gives the container full access
     * to local system services such as D-bus and is therefore considered insecure.</li>
     * </ul>
     */
    public CreateContainerCmd withNetworkMode(String networkMode);

    /**
     * @since 1.19
     */
    public CreateContainerCmd withOomKillDisable(Boolean oomKillDisable);

    /**
     * Add one or more {@link PortBinding}s. This corresponds to the <code>--publish</code> (<code>-p</code>) option of
     * the <code>docker run</code> CLI command.
     */
    public CreateContainerCmd withPortBindings(PortBinding... portBindings);

    /**
     * Add one or more {@link PortBinding}s. This corresponds to the <code>--publish</code> (<code>-p</code>) option of
     * the <code>docker run</code> CLI command.
     */
    public CreateContainerCmd withPortBindings(List<PortBinding> portBindings);

    /**
     * Add the port bindings that are contained in the given {@link Ports} object.
     *
     * @see #withPortBindings(PortBinding...)
     */
    public CreateContainerCmd withPortBindings(Ports portBindings);

    public CreateContainerCmd withPortSpecs(String... portSpecs);

    public CreateContainerCmd withPortSpecs(List<String> portSpecs);

    public CreateContainerCmd withPrivileged(Boolean privileged);

    public CreateContainerCmd withPublishAllPorts(Boolean publishAllPorts);

    public CreateContainerCmd withReadonlyRootfs(Boolean readonlyRootfs);

    /**
     * Set custom {@link RestartPolicy} for the container. Defaults to {@link RestartPolicy#noRestart()}
     */
    public CreateContainerCmd withRestartPolicy(RestartPolicy restartPolicy);

    public CreateContainerCmd withStdInOnce(Boolean stdInOnce);

    public CreateContainerCmd withStdinOpen(Boolean stdinOpen);

    public CreateContainerCmd withTty(Boolean tty);

    public CreateContainerCmd withUlimits(Ulimit... ulimits);

    public CreateContainerCmd withUlimits(List<Ulimit> ulimits);

    public CreateContainerCmd withUser(String user);

    public CreateContainerCmd withVolumes(Volume... volumes);

    public CreateContainerCmd withVolumes(List<Volume> volumes);

    public CreateContainerCmd withVolumesFrom(VolumesFrom... volumesFrom);

    public CreateContainerCmd withVolumesFrom(List<VolumesFrom> volumesFrom);

    public CreateContainerCmd withWorkingDir(String workingDir);

    /**
     * @throws NotFoundException
     *             No such container
     * @throws ConflictException
     *             Named container already exists
     */
    @Override
    public CreateContainerResponse exec() throws NotFoundException, ConflictException;

    public static interface Exec extends DockerCmdSyncExec<CreateContainerCmd, CreateContainerResponse> {
    }
}
