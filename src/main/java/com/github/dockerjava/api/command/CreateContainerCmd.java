package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HealthCheck;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.VolumesFrom;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

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

    @Deprecated
    @CheckForNull
    Bind[] getBinds();

    @Deprecated
    CreateContainerCmd withBinds(Bind... binds);

    @Deprecated
    CreateContainerCmd withBinds(List<Bind> binds);

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

    CreateContainerCmd withEnv(String... env);

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

    @Deprecated
    @CheckForNull
    Link[] getLinks();

    /**
     * Add link to another container.
     */
    @Deprecated
    CreateContainerCmd withLinks(Link... links);

    /**
     * Add link to another container.
     */
    @Deprecated
    CreateContainerCmd withLinks(List<Link> links);

    @CheckForNull
    String getIpv6Address();

    CreateContainerCmd withIpv6Address(String ipv6Address);

    @CheckForNull
    Map<String, String> getLabels();

    CreateContainerCmd withLabels(Map<String, String> labels);

    @CheckForNull
    String getMacAddress();

    CreateContainerCmd withMacAddress(String macAddress);

    @CheckForNull
    String getName();

    @Deprecated
    @CheckForNull
    String getNetworkMode();

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
    CreateContainerCmd withNetworkMode(String networkMode);

    @Deprecated
    @CheckForNull
    Ports getPortBindings();

    /**
     * Add one or more {@link PortBinding}s. This corresponds to the <code>--publish</code> (<code>-p</code>) option of the
     * <code>docker run</code> CLI command.
     */
    @Deprecated
    CreateContainerCmd withPortBindings(PortBinding... portBindings);

    /**
     * Add one or more {@link PortBinding}s. This corresponds to the <code>--publish</code> (<code>-p</code>) option of the
     * <code>docker run</code> CLI command.
     */
    @Deprecated
    CreateContainerCmd withPortBindings(List<PortBinding> portBindings);

    /**
     * Add the port bindings that are contained in the given {@link Ports} object.
     *
     * @see #withPortBindings(PortBinding...)
     */
    @Deprecated
    CreateContainerCmd withPortBindings(Ports portBindings);

    CreateContainerCmd withName(String name);

    @CheckForNull
    String[] getPortSpecs();

    CreateContainerCmd withPortSpecs(String... portSpecs);

    CreateContainerCmd withPortSpecs(List<String> portSpecs);

    @Deprecated
    @CheckForNull
    Boolean getPrivileged();

    @Deprecated
    CreateContainerCmd withPrivileged(Boolean privileged);

    @CheckForNull
    String getUser();

    CreateContainerCmd withUser(String user);

    @CheckForNull
    Volume[] getVolumes();

    CreateContainerCmd withVolumes(Volume... volumes);

    CreateContainerCmd withVolumes(List<Volume> volumes);

    @Deprecated
    @CheckForNull
    VolumesFrom[] getVolumesFrom();

    @Deprecated
    CreateContainerCmd withVolumesFrom(VolumesFrom... volumesFrom);

    @Deprecated
    CreateContainerCmd withVolumesFrom(List<VolumesFrom> volumesFrom);

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

    @Deprecated
    @CheckForNull
    Boolean getPublishAllPorts();

    @Deprecated
    CreateContainerCmd withPublishAllPorts(Boolean publishAllPorts);

    @CheckForNull
    @Deprecated
    String[] getExtraHosts();

    /**
     * Add hostnames to /etc/hosts in the container
     */
    @Deprecated
    CreateContainerCmd withExtraHosts(String... extraHosts);

    /**
     * Add hostnames to /etc/hosts in the container
     */
    @Deprecated
    CreateContainerCmd withExtraHosts(List<String> extraHosts);

    @CheckForNull
    List<String> getOnBuild();

    CreateContainerCmd withOnBuild(List<String> onBuild);

    @CheckForNull
    HostConfig getHostConfig();

    CreateContainerCmd withHostConfig(HostConfig hostConfig);

    /**
     * @throws NotFoundException No such container
     * @throws ConflictException Named container already exists
     */
    @Override
    CreateContainerResponse exec() throws NotFoundException, ConflictException;

    interface Exec extends DockerCmdSyncExec<CreateContainerCmd, CreateContainerResponse> {
    }
}
