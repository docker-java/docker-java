package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Capability;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.LxcConf;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.RestartPolicy;

/**
 * Start a container.
 */
public interface StartContainerCmd extends DockerCmd<Void> {

	public static interface Exec extends DockerCmdExec<StartContainerCmd, Void> {
	}

	/**
	 * @throws NotFoundException
	 *             No such container
	 * @throws NotModifiedException
	 *             Container already started
	 */
	@Override
	public Void exec() throws NotFoundException, NotModifiedException;

	public Bind[] getBinds();

	public Capability[] getCapAdd();

	public Capability[] getCapDrop();

	public Boolean getReadonlyRootfs();

	public long getCpuShares();

	public String getCpusetCpus();

	public Boolean getPublishAllPorts();

	public Boolean getPrivileged();

	public String getContainerId();

	public Device[] getDevices();

	public String[] getDns();

	public String[] getDnsSearch();

	public String[] getExtraHosts();

	public Link[] getLinks();

	public LxcConf[] getLxcConf();

	public long getMemoryLimit();

	public long getMemorySwap();

	public String getNetworkMode();

	public Ports getPortBindings();

	public RestartPolicy getRestartPolicy();

	public String getVolumesFrom();

	public Boolean isPrivileged();

	public Boolean isReadonlyRootfs();

	public Boolean isPublishAllPorts();

	public StartContainerCmd withBinds(Bind... binds);

	/**
	 * Add linux <a
	 * href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel
	 * capability</a> to the container. For example: adding {@link Capability#MKNOD}
	 * allows the container to create special files using the 'mknod' command.
	 */
	@Deprecated
	public StartContainerCmd withCapAdd(Capability... capAdd);

	/**
	 * Drop linux <a
	 * href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel
	 * capability</a> from the container. For example: dropping {@link Capability#CHOWN}
	 * prevents the container from changing the owner of any files.
	 */
	public StartContainerCmd withCapDrop(Capability... capDrop);

	@Deprecated
	public StartContainerCmd withContainerId(String containerId);

	/**
	 * Add host devices to the container
	 */
	public StartContainerCmd withDevices(Device... devices);

	/**
	 * Set custom DNS servers
	 */
	public StartContainerCmd withDns(String... dns);

	/**
	 * Set custom DNS search domains
	 */
	public StartContainerCmd withDnsSearch(String... dnsSearch);

	/**
	 * Add hostnames to /etc/hosts in the container
	 */
	public StartContainerCmd withExtraHosts(String... extraHosts);

	/**
	 * Add link to another container.
	 */
	public StartContainerCmd withLinks(Link... links);

	public StartContainerCmd withLxcConf(LxcConf... lxcConf);

	public StartContainerCmd withMemoryLimit(long memoryLimit);

	public StartContainerCmd withMemorySwap(long memorySwap);

	public StartContainerCmd withCpusetCpus(String cpuset);

	public StartContainerCmd withCpuShares(int cpuShares);

	/**
	 * Set the Network mode for the container
	 * <ul>
	 * <li>'bridge': creates a new network stack for the container on the docker
	 * bridge</li>
	 * <li>'none': no networking for this container</li>
	 * <li>'container:<name|id>': reuses another container network stack</li>
	 * <li>'host': use the host network stack inside the container. Note: the
	 * host mode gives the container full access to local system services such
	 * as D-bus and is therefore considered insecure.</li>
	 * </ul>
	 */
	public StartContainerCmd withNetworkMode(String networkMode);

	/**
	 * Add one or more {@link PortBinding}s.
	 * This corresponds to the <code>--publish</code> (<code>-p</code>)
	 * option of the <code>docker run</code> CLI command.
	 */
	public StartContainerCmd withPortBindings(PortBinding... portBindings);

	/**
	 * Add the port bindings that are contained in the given {@link Ports}
	 * object.
	 *
	 * @see #withPortBindings(PortBinding...)
	 */
	public StartContainerCmd withPortBindings(Ports portBindings);

	public StartContainerCmd withPrivileged(Boolean privileged);

	public StartContainerCmd withReadonlyRootfs(Boolean readonlyRootfs);

	public StartContainerCmd withPublishAllPorts(Boolean publishAllPorts);

	/**
	 * Set custom {@link RestartPolicy} for the container. Defaults to
	 * {@link RestartPolicy#noRestart()}
	 */
	public StartContainerCmd withRestartPolicy(RestartPolicy restartPolicy);

	public StartContainerCmd withVolumesFrom(String volumesFrom);

}
