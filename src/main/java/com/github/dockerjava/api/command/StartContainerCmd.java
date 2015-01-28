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
 * Start a container
 */
public interface StartContainerCmd extends DockerCmd<Void> {

	public Bind[] getBinds();

	public Link[] getLinks();

	public LxcConf[] getLxcConf();

	public Ports getPortBindings();

	public Boolean isPublishAllPorts();

	public Boolean isPrivileged();

	public String[] getDns();

	public String[] getDnsSearch();

	public String getVolumesFrom();

	public String getContainerId();

	public String getNetworkMode();

	public Device[] getDevices();

	public RestartPolicy getRestartPolicy();

	public Capability[] getCapAdd();

	public Capability[] getCapDrop();

	public StartContainerCmd withBinds(Bind... binds);

	/**
	 * Add link to another container.
	 */
	public StartContainerCmd withLinks(Link... links);

	public StartContainerCmd withLxcConf(LxcConf... lxcConf);

	/**
	 * Add the port bindings that are contained in the given {@link Ports}
	 * object.
	 * 
	 * @see #withPortBindings(PortBinding...)
	 */
	public StartContainerCmd withPortBindings(Ports portBindings);

	/**
	 * Add one or more {@link PortBinding}s.
	 * This corresponds to the <code>--publish</code> (<code>-p</code>)
	 * option of the <code>docker run</code> CLI command.
	 */
	public StartContainerCmd withPortBindings(PortBinding... portBindings);

	public StartContainerCmd withPrivileged(Boolean privileged);

	public StartContainerCmd withPublishAllPorts(Boolean publishAllPorts);

	/**
	 * Set custom DNS servers
	 */
	public StartContainerCmd withDns(String... dns);

	/**
	 * Set custom DNS search domains
	 */
	public StartContainerCmd withDnsSearch(String... dnsSearch);

	public StartContainerCmd withVolumesFrom(String volumesFrom);

	public StartContainerCmd withContainerId(String containerId);

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
	 * Add host devices to the container
	 */
	public StartContainerCmd withDevices(Device... devices);

	/**
	 * Set custom {@link RestartPolicy} for the container. Defaults to
	 * {@link RestartPolicy#noRestart()}
	 */
	public StartContainerCmd withRestartPolicy(RestartPolicy restartPolicy);

	/**
	 * Add linux <a
	 * href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel
	 * capability</a> to the container. For example: adding {@link Capability#MKNOD}
	 * allows the container to create special files using the 'mknod' command.
	 */
	public StartContainerCmd withCapAdd(Capability... capAdd);

	/**
	 * Drop linux <a
	 * href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel
	 * capability</a> from the container. For example: dropping {@link Capability#CHOWN}
	 * prevents the container from changing the owner of any files.
	 */
	public StartContainerCmd withCapDrop(Capability... capDrop);

	/**
	 * @throws NotFoundException
	 *             No such container
	 * @throws NotModifiedException
	 *             Container already started
	 */
	@Override
	public Void exec() throws NotFoundException, NotModifiedException;

	public static interface Exec extends DockerCmdExec<StartContainerCmd, Void> {
	}

}