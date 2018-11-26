package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

public interface CreateContainerCmd extends SyncDockerCmd<CreateContainerResponse> {

    @CheckForNull
    AuthConfig getAuthConfig();

    @CheckForNull
    List<String> getAliases();

    @CheckForNull
    String[] getCmd();

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
    public Integer getStopTimeout();

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
    String getMacAddress();

    @CheckForNull
    String getName();

    @CheckForNull
    String[] getPortSpecs();

    @CheckForNull
    String getUser();

    @CheckForNull
    Volume[] getVolumes();

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

    @CheckForNull
    Boolean isStdInOnce();

    @CheckForNull
    Boolean isStdinOpen();

    @CheckForNull
    HostConfig getHostConfig();

    @CheckForNull
    Boolean isTty();

    /**
     * While using swarm classic, you can provide an optional auth config which will be used to pull images from a private registry,
     * if the swarm node does not already have the docker image.
     * Note: This option does not have any effect in normal docker
     *
     * @param authConfig The optional auth config
     */
    CreateContainerCmd withAuthConfig(AuthConfig authConfig);

    /**
     * Add network-scoped alias for the container
     *
     * @param aliases on ore more aliases
     */
    CreateContainerCmd withAliases(String... aliases);

    /**
     * Add network-scoped alias for the container
     *
     * @param aliases on ore more aliases
     */
    CreateContainerCmd withAliases(List<String> aliases);

    CreateContainerCmd withAttachStderr(Boolean attachStderr);

    CreateContainerCmd withAttachStdin(Boolean attachStdin);

    CreateContainerCmd withAttachStdout(Boolean attachStdout);

    CreateContainerCmd withCmd(String... cmd);

    CreateContainerCmd withCmd(List<String> cmd);

    CreateContainerCmd withDomainName(String domainName);

    CreateContainerCmd withEntrypoint(String... entrypoint);

    CreateContainerCmd withEntrypoint(List<String> entrypoint);

    CreateContainerCmd withEnv(String... env);

    CreateContainerCmd withEnv(List<String> env);

    CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts);

    CreateContainerCmd withStopSignal(String stopSignal);

    CreateContainerCmd withStopTimeout(Integer stopTimeout);

    CreateContainerCmd withExposedPorts(List<ExposedPort> exposedPorts);

    CreateContainerCmd withHostName(String hostName);

    CreateContainerCmd withImage(String image);

    CreateContainerCmd withIpv4Address(String ipv4Address);

    CreateContainerCmd withIpv6Address(String ipv6Address);

    CreateContainerCmd withLabels(Map<String, String> labels);

    CreateContainerCmd withMacAddress(String macAddress);

    CreateContainerCmd withName(String name);

    CreateContainerCmd withNetworkDisabled(Boolean disableNetwork);

    CreateContainerCmd withPortSpecs(String... portSpecs);

    CreateContainerCmd withPortSpecs(List<String> portSpecs);

    CreateContainerCmd withStdInOnce(Boolean stdInOnce);

    CreateContainerCmd withStdinOpen(Boolean stdinOpen);

    CreateContainerCmd withTty(Boolean tty);

    CreateContainerCmd withUser(String user);

    CreateContainerCmd withVolumes(Volume... volumes);

    CreateContainerCmd withVolumes(List<Volume> volumes);

    CreateContainerCmd withWorkingDir(String workingDir);

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
