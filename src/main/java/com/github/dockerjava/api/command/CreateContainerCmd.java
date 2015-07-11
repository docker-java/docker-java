package com.github.dockerjava.api.command;

import java.util.Map;

import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Capability;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.LxcConf;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.VolumesFrom;

public interface CreateContainerCmd extends DockerCmd<CreateContainerResponse> {

    public static interface Exec extends DockerCmdExec<CreateContainerCmd, CreateContainerResponse> {
    }

    /**
     * @throws NotFoundException
     *             No such container
     * @throws ConflictException
     *             Named container already exists
     */
    @Override
    public CreateContainerResponse exec() throws NotFoundException, ConflictException;

    public Bind[] getBinds();

    public Capability[] getCapAdd();

    public Capability[] getCapDrop();

    public String[] getCmd();

    public String getCpuset();

    public int getCpuShares();

    /**
     * @since 1.19
     */
    public Integer getCpuPeriod();

    /**
     * @since 1.19
     */
    public String getCpusetMems();

    /**
     * @since 1.19
     */
    public Integer getBlkioWeight();

    /**
     * @since 1.19
     */
    public Boolean isOomKillDisable();

    public Device[] getDevices();

    public String[] getDns();

    public String[] getDnsSearch();

    public String[] getEntrypoint();

    public String[] getEnv();

    public ExposedPort[] getExposedPorts();

    public String[] getExtraHosts();

    public HostConfig getHostConfig();

    public String getHostName();

    public String getDomainName();

    public String getImage();

    public Link[] getLinks();

    public LxcConf[] getLxcConf();

    public String getMacAddress();

    public long getMemoryLimit();

    public long getMemorySwap();

    public String getName();

    public String getNetworkMode();

    public Ports getPortBindings();

    public String[] getPortSpecs();

    public RestartPolicy getRestartPolicy();

    public String getUser();

    public Volume[] getVolumes();

    public VolumesFrom[] getVolumesFrom();

    public String getWorkingDir();

    public boolean isAttachStderr();

    public boolean isAttachStdin();

    public boolean isAttachStdout();

    public boolean isNetworkDisabled();

    public Boolean isPrivileged();

    public Boolean isPublishAllPorts();

    public boolean isStdInOnce();

    public boolean isStdinOpen();

    public boolean isTty();

    public CreateContainerCmd withAttachStderr(boolean attachStderr);

    public CreateContainerCmd withAttachStdin(boolean attachStdin);

    public CreateContainerCmd withAttachStdout(boolean attachStdout);

    public CreateContainerCmd withBinds(Bind... binds);

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

    public CreateContainerCmd withCpuset(String cpuset);

    public CreateContainerCmd withCpuShares(int cpuShares);

    /**
     * @since 1.19
     */
    public CreateContainerCmd withCpuPeriod(Integer cpuPeriod);

    /**
     * @since 1.19
     */
    public CreateContainerCmd withCpusetMems(String cpusetMems);

    /**
     * @since 1.19
     */
    public CreateContainerCmd withBlkioWeight(Integer blkioWeight);

    /**
     * @since 1.19
     */
    public CreateContainerCmd withOomKillDisable(Boolean oomKillDisable);

    /**
     * Add host devices to the container
     */
    public CreateContainerCmd withDevices(Device... devices);

    public CreateContainerCmd withNetworkDisabled(boolean disableNetwork);

    /**
     * Set custom DNS servers
     */
    public CreateContainerCmd withDns(String... dns);

    /**
     * Set custom DNS search domains
     */
    public CreateContainerCmd withDnsSearch(String... dnsSearch);

    public CreateContainerCmd withEntrypoint(String... entrypoint);

    public CreateContainerCmd withEnv(String... env);

    public CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts);

    /**
     * Add hostnames to /etc/hosts in the container
     */
    public CreateContainerCmd withExtraHosts(String... extraHosts);

    public CreateContainerCmd withHostConfig(HostConfig hostConfig);

    public CreateContainerCmd withHostName(String hostName);

    public CreateContainerCmd withDomainName(String domainName);

    public CreateContainerCmd withImage(String image);

    public CreateContainerCmd withLabels(Map<String, String> labels);

    /**
     * Add link to another container.
     */
    public CreateContainerCmd withLinks(Link... links);

    public CreateContainerCmd withLxcConf(LxcConf... lxcConf);

    public CreateContainerCmd withMemoryLimit(long memoryLimit);

    public CreateContainerCmd withMemorySwap(long memorySwap);

    public CreateContainerCmd withName(String name);

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
     * Add one or more {@link PortBinding}s. This corresponds to the <code>--publish</code> (<code>-p</code>) option of
     * the <code>docker run</code> CLI command.
     */
    public CreateContainerCmd withPortBindings(PortBinding... portBindings);

    /**
     * Add the port bindings that are contained in the given {@link Ports} object.
     *
     * @see #withPortBindings(PortBinding...)
     */
    public CreateContainerCmd withPortBindings(Ports portBindings);

    public CreateContainerCmd withPortSpecs(String... portSpecs);

    public CreateContainerCmd withPrivileged(boolean privileged);

    public CreateContainerCmd withPublishAllPorts(boolean publishAllPorts);

    /**
     * Set custom {@link RestartPolicy} for the container. Defaults to {@link RestartPolicy#noRestart()}
     */
    public CreateContainerCmd withRestartPolicy(RestartPolicy restartPolicy);

    public CreateContainerCmd withStdInOnce(boolean stdInOnce);

    public CreateContainerCmd withStdinOpen(boolean stdinOpen);

    public CreateContainerCmd withTty(boolean tty);

    public CreateContainerCmd withUser(String user);

    public CreateContainerCmd withVolumes(Volume... volumes);

    public CreateContainerCmd withVolumesFrom(VolumesFrom... volumesFrom);

    public CreateContainerCmd withWorkingDir(String workingDir);

    public CreateContainerCmd withMacAddress(String macAddress);

    /**
     * @return
     */
    Map<String, String> getLabels();

}
