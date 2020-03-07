package com.github.dockerjava.api;

import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.api.command.ConnectToNetworkCmd;
import com.github.dockerjava.api.command.ContainerDiffCmd;
import com.github.dockerjava.api.command.CopyArchiveFromContainerCmd;
import com.github.dockerjava.api.command.CopyArchiveToContainerCmd;
import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.CreateSecretCmd;
import com.github.dockerjava.api.command.CreateServiceCmd;
import com.github.dockerjava.api.command.CreateVolumeCmd;
import com.github.dockerjava.api.command.DisconnectFromNetworkCmd;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.InitializeSwarmCmd;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectExecCmd;
import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectNetworkCmd;
import com.github.dockerjava.api.command.InspectServiceCmd;
import com.github.dockerjava.api.command.InspectSwarmCmd;
import com.github.dockerjava.api.command.InspectVolumeCmd;
import com.github.dockerjava.api.command.JoinSwarmCmd;
import com.github.dockerjava.api.command.KillContainerCmd;
import com.github.dockerjava.api.command.LeaveSwarmCmd;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.ListNetworksCmd;
import com.github.dockerjava.api.command.ListSecretsCmd;
import com.github.dockerjava.api.command.ListServicesCmd;
import com.github.dockerjava.api.command.ListSwarmNodesCmd;
import com.github.dockerjava.api.command.ListTasksCmd;
import com.github.dockerjava.api.command.ListVolumesCmd;
import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.LogSwarmObjectCmd;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.api.command.PruneCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.api.command.RemoveNetworkCmd;
import com.github.dockerjava.api.command.RemoveSecretCmd;
import com.github.dockerjava.api.command.RemoveServiceCmd;
import com.github.dockerjava.api.command.RemoveVolumeCmd;
import com.github.dockerjava.api.command.RenameContainerCmd;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.command.SaveImageCmd;
import com.github.dockerjava.api.command.SaveImagesCmd;
import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.api.command.UpdateContainerCmd;
import com.github.dockerjava.api.command.UpdateServiceCmd;
import com.github.dockerjava.api.command.UpdateSwarmCmd;
import com.github.dockerjava.api.command.UpdateSwarmNodeCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Identifier;
import com.github.dockerjava.api.model.PruneType;
import com.github.dockerjava.api.model.SecretSpec;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.SwarmSpec;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

// https://godoc.org/github.com/fsouza/go-dockerclient
public interface DockerClient extends Closeable {

    AuthConfig authConfig() throws DockerException;

    /**
     * Authenticate with the server, useful for checking authentication.
     */
    AuthCmd authCmd();

    InfoCmd infoCmd();

    PingCmd pingCmd();

    VersionCmd versionCmd();

    /**
     * * IMAGE API *
     */

    PullImageCmd pullImageCmd(@Nonnull String repository);

    PushImageCmd pushImageCmd(@Nonnull String name);

    PushImageCmd pushImageCmd(@Nonnull Identifier identifier);

    CreateImageCmd createImageCmd(@Nonnull String repository, @Nonnull InputStream imageStream);

    /**
     * Loads a tarball with a set of images and tags into a Docker repository.
     *
     * Corresponds to POST /images/load API endpoint.
     *
     * @param imageStream
     *            stream of the tarball file
     * @return created command
     * @since {@link RemoteApiVersion#VERSION_1_7}
     */
    LoadImageCmd loadImageCmd(@Nonnull InputStream imageStream);

    SearchImagesCmd searchImagesCmd(@Nonnull String term);

    RemoveImageCmd removeImageCmd(@Nonnull String imageId);

    ListImagesCmd listImagesCmd();

    InspectImageCmd inspectImageCmd(@Nonnull String imageId);

    /**
     * @param name
     *            The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
     */
    SaveImageCmd saveImageCmd(@Nonnull String name);

    /**
     * Command to download multiple images at once.
     * @return command (builder)
     */
    SaveImagesCmd saveImagesCmd();

    /**
     * * CONTAINER API *
     */

    ListContainersCmd listContainersCmd();

    CreateContainerCmd createContainerCmd(@Nonnull String image);

    /**
     * Creates a new {@link StartContainerCmd} for the container with the given ID. The command can then be further customized by using
     * builder methods on it like {@link StartContainerCmd#withDns(String...)}.
     * <p>
     * <b>If you customize the command, any existing configuration of the target container will get reset to its default before applying the
     * new configuration. To preserve the existing configuration, use an unconfigured {@link StartContainerCmd}.</b>
     * <p>
     * This command corresponds to the <code>/containers/{id}/start</code> endpoint of the Docker Remote API.
     */
    StartContainerCmd startContainerCmd(@Nonnull String containerId);

    ExecCreateCmd execCreateCmd(@Nonnull String containerId);

    InspectContainerCmd inspectContainerCmd(@Nonnull String containerId);

    RemoveContainerCmd removeContainerCmd(@Nonnull String containerId);

    WaitContainerCmd waitContainerCmd(@Nonnull String containerId);

    AttachContainerCmd attachContainerCmd(@Nonnull String containerId);

    ExecStartCmd execStartCmd(@Nonnull String execId);

    InspectExecCmd inspectExecCmd(@Nonnull String execId);

    LogContainerCmd logContainerCmd(@Nonnull String containerId);

    /**
     * Copy resource from container to local machine.
     *
     * @param containerId
     *            id of the container
     * @param resource
     *            path to container's resource
     * @return created command
     * @since {@link RemoteApiVersion#VERSION_1_20}
     */
    CopyArchiveFromContainerCmd copyArchiveFromContainerCmd(@Nonnull String containerId, @Nonnull String resource);

    /**
     * Copy resource from container to local machine.
     *
     * @param containerId
     *            id of the container
     * @param resource
     *            path to container's resource
     * @return created command
     * @see #copyArchiveFromContainerCmd(String, String)
     * @deprecated since docker API version 1.20, replaced by {@link #copyArchiveFromContainerCmd(String, String)}
     * since 1.24 fails.
     */
    @Deprecated
    CopyFileFromContainerCmd copyFileFromContainerCmd(@Nonnull String containerId, @Nonnull String resource);

    /**
     * Copy archive from local machine to remote container
     *
     * @param containerId
     *            id of the container
     * @return created command
     * @since {@link RemoteApiVersion#VERSION_1_20}
     */
    CopyArchiveToContainerCmd copyArchiveToContainerCmd(@Nonnull String containerId);

    ContainerDiffCmd containerDiffCmd(@Nonnull String containerId);

    StopContainerCmd stopContainerCmd(@Nonnull String containerId);

    KillContainerCmd killContainerCmd(@Nonnull String containerId);

    /**
     * Update container settings
     *
     * @param containerId id of the container
     * @return command
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    UpdateContainerCmd updateContainerCmd(@Nonnull String containerId);

    /**
     * Rename container.
     *
     * @param containerId id of the container
     * @return command
     * @since {@link RemoteApiVersion#VERSION_1_17}
     */
    RenameContainerCmd renameContainerCmd(@Nonnull String containerId);

    RestartContainerCmd restartContainerCmd(@Nonnull String containerId);

    CommitCmd commitCmd(@Nonnull String containerId);

    BuildImageCmd buildImageCmd();

    BuildImageCmd buildImageCmd(File dockerFileOrFolder);

    BuildImageCmd buildImageCmd(InputStream tarInputStream);

    TopContainerCmd topContainerCmd(String containerId);

    TagImageCmd tagImageCmd(String imageId, String imageNameWithRepository, String tag);

    PauseContainerCmd pauseContainerCmd(String containerId);

    UnpauseContainerCmd unpauseContainerCmd(String containerId);

    EventsCmd eventsCmd();

    StatsCmd statsCmd(String containerId);

    CreateVolumeCmd createVolumeCmd();

    InspectVolumeCmd inspectVolumeCmd(String name);

    RemoveVolumeCmd removeVolumeCmd(String name);

    ListVolumesCmd listVolumesCmd();

    ListNetworksCmd listNetworksCmd();

    InspectNetworkCmd inspectNetworkCmd();

    CreateNetworkCmd createNetworkCmd();

    RemoveNetworkCmd removeNetworkCmd(@Nonnull String networkId);

    ConnectToNetworkCmd connectToNetworkCmd();

    DisconnectFromNetworkCmd disconnectFromNetworkCmd();

    /**
     * Enables swarm mode for the docker engine and creates a new swarm cluster
     *
     * @since 1.24
     * @param swarmSpec the specification for the swarm
     * @return the command
     */
    InitializeSwarmCmd initializeSwarmCmd(SwarmSpec swarmSpec);

    /**
     * Gets information about the swarm the docker engine is currently in
     *
     * @since 1.24
     * @return the command
     */
    InspectSwarmCmd inspectSwarmCmd();

    /**
     * Enables swarm mode for the docker engine and joins an existing swarm cluster
     *
     * @since 1.24
     * @return the command
     */
    JoinSwarmCmd joinSwarmCmd();

    /**
     * Disables swarm node for the docker engine and leaves the swarm cluster
     *
     * @since 1.24
     * @return the command
     */
    LeaveSwarmCmd leaveSwarmCmd();

    /**
     * Updates the swarm specification
     *
     * @since 1.24
     * @param swarmSpec the specification for the swarm
     * @return the command
     */
    UpdateSwarmCmd updateSwarmCmd(SwarmSpec swarmSpec);

    /**
     * Updates the swarm node
     *
     * @return the command
     * @since 1.24
     */
    UpdateSwarmNodeCmd updateSwarmNodeCmd();

    /**
     * List nodes in swarm
     *
     * @return the command
     * @since 1.24
     */
    ListSwarmNodesCmd listSwarmNodesCmd();

    /**
     * Command to list all services in a docker swarm. Only applicable if docker runs in swarm mode.
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     * @return command
     */
    ListServicesCmd listServicesCmd();

    /**
     * Command to create a service in a docker swarm. Only applicable if docker runs in swarm mode.
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     * @param serviceSpec the service specification
     * @return command
     */
    CreateServiceCmd createServiceCmd(ServiceSpec serviceSpec);

    /**
     * Command to inspect a service
     * @param serviceId service id or service name
     * @return command
     */
    InspectServiceCmd inspectServiceCmd(String serviceId);

    /**
     * Command to update a service specification
     * @param serviceId service id
     * @param serviceSpec the new service specification
     * @return command
     */
    UpdateServiceCmd updateServiceCmd(String serviceId, ServiceSpec serviceSpec);

    /**
     * Command to remove a service
     * @param serviceId service id or service name
     * @return command
     */
    RemoveServiceCmd removeServiceCmd(String serviceId);

    /**
     * List tasks in the swarm cluster
     *
     * @return the command
     * @since 1.24
     */
    ListTasksCmd listTasksCmd();

    /**
     * Command to get service log
     *
     * @return the command
     * @since 1.29
     */
    LogSwarmObjectCmd logServiceCmd(String serviceId);

    /**
     * Command to get task log
     *
     * @return the command
     * @since 1.29
     */
    LogSwarmObjectCmd logTaskCmd(String taskId);

    /**
     * Command to delete unused containers/images/networks/volumes
     *
     * @since {@link RemoteApiVersion#VERSION_1_25}
     */
    PruneCmd pruneCmd(PruneType pruneType);

    /**
     *  Command to list all secrets. Only applicable if docker runs in swarm mode.
     *
     * @since {@link RemoteApiVersion#VERSION_1_25}
     * @return command
     */
    ListSecretsCmd listSecretsCmd();

    /**
     * Command to create a secret in a docker swarm. Only applicable if docker runs in swarm mode.
     *
     * @since {@link RemoteApiVersion#VERSION_1_25}
     * @param secretSpec the secret specification
     * @return command
     */
    CreateSecretCmd createSecretCmd(SecretSpec secretSpec);

    /**
     * Command to remove a secret
     * @param secretId secret id or secret name
     * @return command
     */
    RemoveSecretCmd removeSecretCmd(String secretId);



    @Override
    void close() throws IOException;

}
