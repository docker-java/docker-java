package com.github.dockerjava.api;

import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.ConnectToNetworkCmd;
import com.github.dockerjava.api.command.CopyArchiveToContainerCmd;
import com.github.dockerjava.api.command.CreateConfigCmd;
import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.CreateSecretCmd;
import com.github.dockerjava.api.command.CreateServiceCmd;
import com.github.dockerjava.api.command.CreateVolumeCmd;
import com.github.dockerjava.api.command.DisconnectFromNetworkCmd;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.InitializeSwarmCmd;
import com.github.dockerjava.api.command.InspectConfigCmd;
import com.github.dockerjava.api.command.InspectNetworkCmd;
import com.github.dockerjava.api.command.InspectServiceCmd;
import com.github.dockerjava.api.command.InspectSwarmCmd;
import com.github.dockerjava.api.command.InspectVolumeCmd;
import com.github.dockerjava.api.command.JoinSwarmCmd;
import com.github.dockerjava.api.command.LeaveSwarmCmd;
import com.github.dockerjava.api.command.ListConfigsCmd;
import com.github.dockerjava.api.command.ListNetworksCmd;
import com.github.dockerjava.api.command.ListSecretsCmd;
import com.github.dockerjava.api.command.ListServicesCmd;
import com.github.dockerjava.api.command.ListSwarmNodesCmd;
import com.github.dockerjava.api.command.ListTasksCmd;
import com.github.dockerjava.api.command.ListVolumesCmd;
import com.github.dockerjava.api.command.LogSwarmObjectCmd;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.api.command.PruneCmd;
import com.github.dockerjava.api.command.RemoveConfigCmd;
import com.github.dockerjava.api.command.RemoveNetworkCmd;
import com.github.dockerjava.api.command.RemoveSecretCmd;
import com.github.dockerjava.api.command.RemoveServiceCmd;
import com.github.dockerjava.api.command.RemoveSwarmNodeCmd;
import com.github.dockerjava.api.command.RemoveVolumeCmd;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.api.command.UpdateServiceCmd;
import com.github.dockerjava.api.command.UpdateSwarmCmd;
import com.github.dockerjava.api.command.UpdateSwarmNodeCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.AuthConfig;
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
public interface DockerClient extends Closeable, DockerClientImage, DockerClientContainer {

    AuthConfig authConfig() throws DockerException;

    /**
     * Authenticate with the server, useful for checking authentication.
     */
    AuthCmd authCmd();

    InfoCmd infoCmd();

    PingCmd pingCmd();

    VersionCmd versionCmd();

    /**
     * Copy archive from local machine to remote container
     *
     * @param containerId
     *            id of the container
     * @return created command
     * @since {@link RemoteApiVersion#VERSION_1_20}
     */
    CopyArchiveToContainerCmd copyArchiveToContainerCmd(@Nonnull String containerId);

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
     * Remove the swarm node
     *
     * @param swarmNodeId swarmNodeId
     * @return the command
     * @since 1.24
     */
    RemoveSwarmNodeCmd removeSwarmNodeCmd(String swarmNodeId);

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
     *
     * @since {@link RemoteApiVersion#VERSION_1_25}
     * @param secretId secret id or secret name
     * @return command
     */
    RemoveSecretCmd removeSecretCmd(String secretId);


    /**
     *  Command to list all configs. Only applicable if docker runs in swarm mode.
     *
     * @since {@link RemoteApiVersion#VERSION_1_30}
     * @return command
     */
    ListConfigsCmd listConfigsCmd();

    /**
     * Command to create a config in a docker swarm. Only applicable if docker runs in swarm mode.
     *
     * @since {@link RemoteApiVersion#VERSION_1_30}
     * @return command
     */
    CreateConfigCmd createConfigCmd();

    /**
     * Command to inspect a service
     *
     * @since {@link RemoteApiVersion#VERSION_1_30}
     * @param configId config id or config name
     * @return command
     */
    InspectConfigCmd inspectConfigCmd(String configId);

    /**
     * Command to remove a config
     * @since {@link RemoteApiVersion#VERSION_1_30}
     * @param configId config id or config name
     * @return command
     */
    RemoveConfigCmd removeConfigCmd(String configId);


    @Override
    void close() throws IOException;

}
