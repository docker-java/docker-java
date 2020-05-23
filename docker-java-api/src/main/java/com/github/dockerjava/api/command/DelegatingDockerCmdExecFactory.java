package com.github.dockerjava.api.command;

import java.io.IOException;

public class DelegatingDockerCmdExecFactory implements DockerCmdExecFactory {

    // We're not using abstract class because we want
    // the compiler to force us to implement new DockerCmdExecFactory when added
    public DockerCmdExecFactory getDockerCmdExecFactory() {
        throw new IllegalStateException("Implement me!");
    }

    @Override
    public AuthCmd.Exec createAuthCmdExec() {
        return getDockerCmdExecFactory().createAuthCmdExec();
    }

    @Override
    public InfoCmd.Exec createInfoCmdExec() {
        return getDockerCmdExecFactory().createInfoCmdExec();
    }

    @Override
    public PingCmd.Exec createPingCmdExec() {
        return getDockerCmdExecFactory().createPingCmdExec();
    }

    @Override
    public ExecCreateCmd.Exec createExecCmdExec() {
        return getDockerCmdExecFactory().createExecCmdExec();
    }

    @Override
    public VersionCmd.Exec createVersionCmdExec() {
        return getDockerCmdExecFactory().createVersionCmdExec();
    }

    @Override
    public PullImageCmd.Exec createPullImageCmdExec() {
        return getDockerCmdExecFactory().createPullImageCmdExec();
    }

    @Override
    public PushImageCmd.Exec createPushImageCmdExec() {
        return getDockerCmdExecFactory().createPushImageCmdExec();
    }

    @Override
    public SaveImageCmd.Exec createSaveImageCmdExec() {
        return getDockerCmdExecFactory().createSaveImageCmdExec();
    }

    @Override
    public SaveImagesCmd.Exec createSaveImagesCmdExec() {
        return getDockerCmdExecFactory().createSaveImagesCmdExec();
    }

    @Override
    public CreateImageCmd.Exec createCreateImageCmdExec() {
        return getDockerCmdExecFactory().createCreateImageCmdExec();
    }

    @Override
    public LoadImageCmd.Exec createLoadImageCmdExec() {
        return getDockerCmdExecFactory().createLoadImageCmdExec();
    }

    @Override
    public SearchImagesCmd.Exec createSearchImagesCmdExec() {
        return getDockerCmdExecFactory().createSearchImagesCmdExec();
    }

    @Override
    public RemoveImageCmd.Exec createRemoveImageCmdExec() {
        return getDockerCmdExecFactory().createRemoveImageCmdExec();
    }

    @Override
    public ListImagesCmd.Exec createListImagesCmdExec() {
        return getDockerCmdExecFactory().createListImagesCmdExec();
    }

    @Override
    public InspectImageCmd.Exec createInspectImageCmdExec() {
        return getDockerCmdExecFactory().createInspectImageCmdExec();
    }

    @Override
    public ListContainersCmd.Exec createListContainersCmdExec() {
        return getDockerCmdExecFactory().createListContainersCmdExec();
    }

    @Override
    public CreateContainerCmd.Exec createCreateContainerCmdExec() {
        return getDockerCmdExecFactory().createCreateContainerCmdExec();
    }

    @Override
    public StartContainerCmd.Exec createStartContainerCmdExec() {
        return getDockerCmdExecFactory().createStartContainerCmdExec();
    }

    @Override
    public InspectContainerCmd.Exec createInspectContainerCmdExec() {
        return getDockerCmdExecFactory().createInspectContainerCmdExec();
    }

    @Override
    public RemoveContainerCmd.Exec createRemoveContainerCmdExec() {
        return getDockerCmdExecFactory().createRemoveContainerCmdExec();
    }

    @Override
    public WaitContainerCmd.Exec createWaitContainerCmdExec() {
        return getDockerCmdExecFactory().createWaitContainerCmdExec();
    }

    @Override
    public AttachContainerCmd.Exec createAttachContainerCmdExec() {
        return getDockerCmdExecFactory().createAttachContainerCmdExec();
    }

    @Override
    public ExecStartCmd.Exec createExecStartCmdExec() {
        return getDockerCmdExecFactory().createExecStartCmdExec();
    }

    @Override
    public InspectExecCmd.Exec createInspectExecCmdExec() {
        return getDockerCmdExecFactory().createInspectExecCmdExec();
    }

    @Override
    public LogContainerCmd.Exec createLogContainerCmdExec() {
        return getDockerCmdExecFactory().createLogContainerCmdExec();
    }

    @Override
    public CopyFileFromContainerCmd.Exec createCopyFileFromContainerCmdExec() {
        return getDockerCmdExecFactory().createCopyFileFromContainerCmdExec();
    }

    @Override
    public CopyArchiveFromContainerCmd.Exec createCopyArchiveFromContainerCmdExec() {
        return getDockerCmdExecFactory().createCopyArchiveFromContainerCmdExec();
    }

    @Override
    public CopyArchiveToContainerCmd.Exec createCopyArchiveToContainerCmdExec() {
        return getDockerCmdExecFactory().createCopyArchiveToContainerCmdExec();
    }

    @Override
    public StopContainerCmd.Exec createStopContainerCmdExec() {
        return getDockerCmdExecFactory().createStopContainerCmdExec();
    }

    @Override
    public ContainerDiffCmd.Exec createContainerDiffCmdExec() {
        return getDockerCmdExecFactory().createContainerDiffCmdExec();
    }

    @Override
    public KillContainerCmd.Exec createKillContainerCmdExec() {
        return getDockerCmdExecFactory().createKillContainerCmdExec();
    }

    @Override
    public UpdateContainerCmd.Exec createUpdateContainerCmdExec() {
        return getDockerCmdExecFactory().createUpdateContainerCmdExec();
    }

    @Override
    public RenameContainerCmd.Exec createRenameContainerCmdExec() {
        return getDockerCmdExecFactory().createRenameContainerCmdExec();
    }

    @Override
    public RestartContainerCmd.Exec createRestartContainerCmdExec() {
        return getDockerCmdExecFactory().createRestartContainerCmdExec();
    }

    @Override
    public CommitCmd.Exec createCommitCmdExec() {
        return getDockerCmdExecFactory().createCommitCmdExec();
    }

    @Override
    public BuildImageCmd.Exec createBuildImageCmdExec() {
        return getDockerCmdExecFactory().createBuildImageCmdExec();
    }

    @Override
    public TopContainerCmd.Exec createTopContainerCmdExec() {
        return getDockerCmdExecFactory().createTopContainerCmdExec();
    }

    @Override
    public TagImageCmd.Exec createTagImageCmdExec() {
        return getDockerCmdExecFactory().createTagImageCmdExec();
    }

    @Override
    public PauseContainerCmd.Exec createPauseContainerCmdExec() {
        return getDockerCmdExecFactory().createPauseContainerCmdExec();
    }

    @Override
    public UnpauseContainerCmd.Exec createUnpauseContainerCmdExec() {
        return getDockerCmdExecFactory().createUnpauseContainerCmdExec();
    }

    @Override
    public EventsCmd.Exec createEventsCmdExec() {
        return getDockerCmdExecFactory().createEventsCmdExec();
    }

    @Override
    public StatsCmd.Exec createStatsCmdExec() {
        return getDockerCmdExecFactory().createStatsCmdExec();
    }

    @Override
    public CreateVolumeCmd.Exec createCreateVolumeCmdExec() {
        return getDockerCmdExecFactory().createCreateVolumeCmdExec();
    }

    @Override
    public InspectVolumeCmd.Exec createInspectVolumeCmdExec() {
        return getDockerCmdExecFactory().createInspectVolumeCmdExec();
    }

    @Override
    public RemoveVolumeCmd.Exec createRemoveVolumeCmdExec() {
        return getDockerCmdExecFactory().createRemoveVolumeCmdExec();
    }

    @Override
    public ListVolumesCmd.Exec createListVolumesCmdExec() {
        return getDockerCmdExecFactory().createListVolumesCmdExec();
    }

    @Override
    public ListNetworksCmd.Exec createListNetworksCmdExec() {
        return getDockerCmdExecFactory().createListNetworksCmdExec();
    }

    @Override
    public InspectNetworkCmd.Exec createInspectNetworkCmdExec() {
        return getDockerCmdExecFactory().createInspectNetworkCmdExec();
    }

    @Override
    public CreateNetworkCmd.Exec createCreateNetworkCmdExec() {
        return getDockerCmdExecFactory().createCreateNetworkCmdExec();
    }

    @Override
    public RemoveNetworkCmd.Exec createRemoveNetworkCmdExec() {
        return getDockerCmdExecFactory().createRemoveNetworkCmdExec();
    }

    @Override
    public ConnectToNetworkCmd.Exec createConnectToNetworkCmdExec() {
        return getDockerCmdExecFactory().createConnectToNetworkCmdExec();
    }

    @Override
    public DisconnectFromNetworkCmd.Exec createDisconnectFromNetworkCmdExec() {
        return getDockerCmdExecFactory().createDisconnectFromNetworkCmdExec();
    }

    @Override
    public InitializeSwarmCmd.Exec createInitializeSwarmCmdExec() {
        return getDockerCmdExecFactory().createInitializeSwarmCmdExec();
    }

    @Override
    public InspectSwarmCmd.Exec createInspectSwarmCmdExec() {
        return getDockerCmdExecFactory().createInspectSwarmCmdExec();
    }

    @Override
    public JoinSwarmCmd.Exec createJoinSwarmCmdExec() {
        return getDockerCmdExecFactory().createJoinSwarmCmdExec();
    }

    @Override
    public LeaveSwarmCmd.Exec createLeaveSwarmCmdExec() {
        return getDockerCmdExecFactory().createLeaveSwarmCmdExec();
    }

    @Override
    public UpdateSwarmCmd.Exec createUpdateSwarmCmdExec() {
        return getDockerCmdExecFactory().createUpdateSwarmCmdExec();
    }

    @Override
    public ListServicesCmd.Exec createListServicesCmdExec() {
        return getDockerCmdExecFactory().createListServicesCmdExec();
    }

    @Override
    public CreateServiceCmd.Exec createCreateServiceCmdExec() {
        return getDockerCmdExecFactory().createCreateServiceCmdExec();
    }

    @Override
    public InspectServiceCmd.Exec createInspectServiceCmdExec() {
        return getDockerCmdExecFactory().createInspectServiceCmdExec();
    }

    @Override
    public UpdateServiceCmd.Exec createUpdateServiceCmdExec() {
        return getDockerCmdExecFactory().createUpdateServiceCmdExec();
    }

    @Override
    public RemoveServiceCmd.Exec createRemoveServiceCmdExec() {
        return getDockerCmdExecFactory().createRemoveServiceCmdExec();
    }

    @Override
    public LogSwarmObjectCmd.Exec logSwarmObjectExec(String endpoint) {
        return getDockerCmdExecFactory().logSwarmObjectExec(endpoint);
    }

    @Override
    public ListSwarmNodesCmd.Exec listSwarmNodeCmdExec() {
        return getDockerCmdExecFactory().listSwarmNodeCmdExec();
    }

    @Override
    public InspectSwarmNodeCmd.Exec inspectSwarmNodeCmdExec() {
        return getDockerCmdExecFactory().inspectSwarmNodeCmdExec();
    }

    @Override
    public RemoveSwarmNodeCmd.Exec removeSwarmNodeCmdExec() {
        return getDockerCmdExecFactory().removeSwarmNodeCmdExec();
    }

    @Override
    public UpdateSwarmNodeCmd.Exec updateSwarmNodeCmdExec() {
        return getDockerCmdExecFactory().updateSwarmNodeCmdExec();
    }

    @Override
    public ListTasksCmd.Exec listTasksCmdExec() {
        return getDockerCmdExecFactory().listTasksCmdExec();
    }

    @Override
    public PruneCmd.Exec pruneCmdExec() {
        return getDockerCmdExecFactory().pruneCmdExec();
    }

    @Override
    public ListSecretsCmd.Exec createListSecretsCmdExec() {
        return getDockerCmdExecFactory().createListSecretsCmdExec();
    }

    @Override
    public CreateSecretCmd.Exec createCreateSecretCmdExec() {
        return getDockerCmdExecFactory().createCreateSecretCmdExec();
    }

    @Override
    public RemoveSecretCmd.Exec createRemoveSecretCmdExec() {
        return getDockerCmdExecFactory().createRemoveSecretCmdExec();
    }

    @Override
    public void close() throws IOException {
        getDockerCmdExecFactory().close();
    }
}
