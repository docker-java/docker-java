package com.github.dockerjava.api.command;

import java.io.Closeable;
import java.io.IOException;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.RemoteApiVersion;

public interface DockerCmdExecFactory extends Closeable {

    void init(DockerClientConfig dockerClientConfig);

    AuthCmd.Exec createAuthCmdExec();

    InfoCmd.Exec createInfoCmdExec();

    PingCmd.Exec createPingCmdExec();

    ExecCreateCmd.Exec createExecCmdExec();

    VersionCmd.Exec createVersionCmdExec();

    PullImageCmd.Exec createPullImageCmdExec();

    PushImageCmd.Exec createPushImageCmdExec();

    SaveImageCmd.Exec createSaveImageCmdExec();

    CreateImageCmd.Exec createCreateImageCmdExec();

    LoadImageCmd.Exec createLoadImageCmdExec();

    SearchImagesCmd.Exec createSearchImagesCmdExec();

    RemoveImageCmd.Exec createRemoveImageCmdExec();

    ListImagesCmd.Exec createListImagesCmdExec();

    InspectImageCmd.Exec createInspectImageCmdExec();

    ListContainersCmd.Exec createListContainersCmdExec();

    CreateContainerCmd.Exec createCreateContainerCmdExec();

    StartContainerCmd.Exec createStartContainerCmdExec();

    InspectContainerCmd.Exec createInspectContainerCmdExec();

    RemoveContainerCmd.Exec createRemoveContainerCmdExec();

    WaitContainerCmd.Exec createWaitContainerCmdExec();

    AttachContainerCmd.Exec createAttachContainerCmdExec();

    ExecStartCmd.Exec createExecStartCmdExec();

    InspectExecCmd.Exec createInspectExecCmdExec();

    LogContainerCmd.Exec createLogContainerCmdExec();

    CopyFileFromContainerCmd.Exec createCopyFileFromContainerCmdExec();

    CopyArchiveFromContainerCmd.Exec createCopyArchiveFromContainerCmdExec();

    CopyArchiveToContainerCmd.Exec createCopyArchiveToContainerCmdExec();

    StopContainerCmd.Exec createStopContainerCmdExec();

    ContainerDiffCmd.Exec createContainerDiffCmdExec();

    KillContainerCmd.Exec createKillContainerCmdExec();

    UpdateContainerCmd.Exec createUpdateContainerCmdExec();

    /**
     * Rename container.
     *
     * @since {@link RemoteApiVersion#VERSION_1_17}
     */
    RenameContainerCmd.Exec createRenameContainerCmdExec();

    RestartContainerCmd.Exec createRestartContainerCmdExec();

    CommitCmd.Exec createCommitCmdExec();

    BuildImageCmd.Exec createBuildImageCmdExec();

    TopContainerCmd.Exec createTopContainerCmdExec();

    TagImageCmd.Exec createTagImageCmdExec();

    PauseContainerCmd.Exec createPauseContainerCmdExec();

    UnpauseContainerCmd.Exec createUnpauseContainerCmdExec();

    EventsCmd.Exec createEventsCmdExec();

    StatsCmd.Exec createStatsCmdExec();

    CreateVolumeCmd.Exec createCreateVolumeCmdExec();

    InspectVolumeCmd.Exec createInspectVolumeCmdExec();

    RemoveVolumeCmd.Exec createRemoveVolumeCmdExec();

    ListVolumesCmd.Exec createListVolumesCmdExec();

    ListNetworksCmd.Exec createListNetworksCmdExec();

    InspectNetworkCmd.Exec createInspectNetworkCmdExec();

    CreateNetworkCmd.Exec createCreateNetworkCmdExec();

    RemoveNetworkCmd.Exec createRemoveNetworkCmdExec();

    ConnectToNetworkCmd.Exec createConnectToNetworkCmdExec();

    DisconnectFromNetworkCmd.Exec createDisconnectFromNetworkCmdExec();

    // swarm
    InitializeSwarmCmd.Exec createInitializeSwarmCmdExec();

    InspectSwarmCmd.Exec createInspectSwarmCmdExec();

    JoinSwarmCmd.Exec createJoinSwarmCmdExec();

    LeaveSwarmCmd.Exec createLeaveSwarmCmdExec();

    UpdateSwarmCmd.Exec createUpdateSwarmCmdExec();

    /**
     * Command to list all services in a docker swarm. Only applicable if docker runs in swarm mode.
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     */
    ListServicesCmd.Exec createListServicesCmdExec();

    /**
     * Command to create a new service in a docker swarm. Only applicable if docker runs in swarm mode.
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     */
    CreateServiceCmd.Exec createCreateServiceCmdExec();

    /**
     * Command to inspect a service in a docker swarm. Only applicable if docker runs in swarm mode.
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     */
    InspectServiceCmd.Exec createInspectServiceCmdExec();

    /**
     * Command to update a service specification in a docker swarm. Only applicable if docker runs in swarm mode.
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     */
    UpdateServiceCmd.Exec createUpdateServiceCmdExec();

    /**
     * Command to remove a service in a docker swarm. Only applicable if docker runs in swarm mode.
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     */
    RemoveServiceCmd.Exec createRemoveServiceCmdExec();

    /**
     * @param endpoint endpoint name to tail logs
     * @return
     * @since {@link RemoteApiVersion#VERSION_1_29}
     */
    LogSwarmObjectCmd.Exec logSwarmObjectExec(String endpoint);

    // nodes

    /**
     * List all nodes. Node operations require the engine to be part of a swarm
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     */
    ListSwarmNodesCmd.Exec listSwarmNodeCmdExec();

    /**
     * Return low-level information on the node. Node operations require the engine to be part of a swarm
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     */
    InspectSwarmNodeCmd.Exec inspectSwarmNodeCmdExec();

    /**
     * Remove a node from the swarm. Node operations require the engine to be part of a swarm
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     */
    RemoveSwarmNodeCmd.Exec removeSwarmNodeCmdExec();

    /**
     * Update a node. Node operations require the engine to be part of a swarm
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     */
    UpdateSwarmNodeCmd.Exec updateSwarmNodeCmdExec();

    /**
     * Update a node. Node operations require the engine to be part of a swarm
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     */
    ListTasksCmd.Exec listTasksCmdExec();

    @Override
    void close() throws IOException;

}
