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
 * 
 * TODO: Almost all methods are deprecated as they have corresponding siblings in {@link CreateContainerCmd} now.
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

	public String getContainerId();

	public Device[] getDevices();

	public String[] getDns();

	public String[] getDnsSearch();

	public String[] getExtraHosts();

	public Link[] getLinks();

	public LxcConf[] getLxcConf();

	public String getNetworkMode();

	public Ports getPortBindings();

	public RestartPolicy getRestartPolicy();

	public String getVolumesFrom();

	public Boolean isPrivileged();

	public Boolean isPublishAllPorts();

	@Deprecated
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
	@Deprecated
	public StartContainerCmd withCapDrop(Capability... capDrop);

	@Deprecated
	public StartContainerCmd withContainerId(String containerId);

	/**
	 * Add host devices to the container
	 */
	@Deprecated
	public StartContainerCmd withDevices(Device... devices);

	/**
	 * Set custom DNS servers
	 */
	@Deprecated
	public StartContainerCmd withDns(String... dns);

	/**
	 * Set custom DNS search domains
	 */
	@Deprecated
	public StartContainerCmd withDnsSearch(String... dnsSearch);

	/**
	 * Add hostnames to /etc/hosts in the container
	 */
	@Deprecated
	public StartContainerCmd withExtraHosts(String... extraHosts);

	/**
	 * Add link to another container.
	 */
	@Deprecated
	public StartContainerCmd withLinks(Link... links);

	@Deprecated
	public StartContainerCmd withLxcConf(LxcConf... lxcConf);

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
	@Deprecated
	public StartContainerCmd withNetworkMode(String networkMode);

	/**
	 * Add one or more {@link PortBinding}s.
	 * This corresponds to the <code>--publish</code> (<code>-p</code>)
	 * option of the <code>docker run</code> CLI command.
	 */
	@Deprecated
	public StartContainerCmd withPortBindings(PortBinding... portBindings);

	/**
	 * Add the port bindings that are contained in the given {@link Ports}
	 * object.
	 * 
	 * @see #withPortBindings(PortBinding...)
	 */
	@Deprecated
	public StartContainerCmd withPortBindings(Ports portBindings);

	@Deprecated
	public StartContainerCmd withPrivileged(Boolean privileged);

	@Deprecated
	public StartContainerCmd withPublishAllPorts(Boolean publishAllPorts);

	/**
	 * Set custom {@link RestartPolicy} for the container. Defaults to
	 * {@link RestartPolicy#noRestart()}
	 */
	@Deprecated
	public StartContainerCmd withRestartPolicy(RestartPolicy restartPolicy);

	@Deprecated
	public StartContainerCmd withVolumesFrom(String volumesFrom);

}
