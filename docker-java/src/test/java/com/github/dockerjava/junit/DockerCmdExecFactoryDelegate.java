package com.github.dockerjava.junit;


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
import com.github.dockerjava.api.command.DockerCmdExecFactory;
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
import com.github.dockerjava.api.command.InspectSwarmNodeCmd;
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
import com.github.dockerjava.api.command.RemoveSwarmNodeCmd;
import com.github.dockerjava.api.command.RemoveVolumeCmd;
import com.github.dockerjava.api.command.RenameContainerCmd;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.command.SaveImageCmd;
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
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientConfigAware;

import java.io.IOException;

class DockerCmdExecFactoryDelegate implements DockerCmdExecFactory, DockerClientConfigAware {

    final DockerCmdExecFactory delegate;

    DockerCmdExecFactoryDelegate(DockerCmdExecFactory delegate) {
        this.delegate = delegate;
    }

    @Override
    public void init(DockerClientConfig dockerClientConfig) {
        if (delegate instanceof DockerClientConfigAware) {
            ((DockerClientConfigAware) delegate).init(dockerClientConfig);
        }
    }

    @Override
    public AuthCmd.Exec createAuthCmdExec() {
        return delegate.createAuthCmdExec();
    }

    @Override
    public InfoCmd.Exec createInfoCmdExec() {
        return delegate.createInfoCmdExec();
    }

    @Override
    public PingCmd.Exec createPingCmdExec() {
        return delegate.createPingCmdExec();
    }

    @Override
    public ExecCreateCmd.Exec createExecCmdExec() {
        return delegate.createExecCmdExec();
    }

    @Override
    public VersionCmd.Exec createVersionCmdExec() {
        return delegate.createVersionCmdExec();
    }

    @Override
    public PullImageCmd.Exec createPullImageCmdExec() {
        return delegate.createPullImageCmdExec();
    }

    @Override
    public PushImageCmd.Exec createPushImageCmdExec() {
        return delegate.createPushImageCmdExec();
    }

    @Override
    public SaveImageCmd.Exec createSaveImageCmdExec() {
        return delegate.createSaveImageCmdExec();
    }

    @Override
    public CreateImageCmd.Exec createCreateImageCmdExec() {
        return delegate.createCreateImageCmdExec();
    }

    @Override
    public LoadImageCmd.Exec createLoadImageCmdExec() {
        return delegate.createLoadImageCmdExec();
    }

    @Override
    public SearchImagesCmd.Exec createSearchImagesCmdExec() {
        return delegate.createSearchImagesCmdExec();
    }

    @Override
    public RemoveImageCmd.Exec createRemoveImageCmdExec() {
        return delegate.createRemoveImageCmdExec();
    }

    @Override
    public ListImagesCmd.Exec createListImagesCmdExec() {
        return delegate.createListImagesCmdExec();
    }

    @Override
    public InspectImageCmd.Exec createInspectImageCmdExec() {
        return delegate.createInspectImageCmdExec();
    }

    @Override
    public ListContainersCmd.Exec createListContainersCmdExec() {
        return delegate.createListContainersCmdExec();
    }

    @Override
    public CreateContainerCmd.Exec createCreateContainerCmdExec() {
        return delegate.createCreateContainerCmdExec();
    }

    @Override
    public StartContainerCmd.Exec createStartContainerCmdExec() {
        return delegate.createStartContainerCmdExec();
    }

    @Override
    public InspectContainerCmd.Exec createInspectContainerCmdExec() {
        return delegate.createInspectContainerCmdExec();
    }

    @Override
    public RemoveContainerCmd.Exec createRemoveContainerCmdExec() {
        return delegate.createRemoveContainerCmdExec();
    }

    @Override
    public WaitContainerCmd.Exec createWaitContainerCmdExec() {
        return delegate.createWaitContainerCmdExec();
    }

    @Override
    public AttachContainerCmd.Exec createAttachContainerCmdExec() {
        return delegate.createAttachContainerCmdExec();
    }

    @Override
    public ExecStartCmd.Exec createExecStartCmdExec() {
        return delegate.createExecStartCmdExec();
    }

    @Override
    public InspectExecCmd.Exec createInspectExecCmdExec() {
        return delegate.createInspectExecCmdExec();
    }

    @Override
    public LogContainerCmd.Exec createLogContainerCmdExec() {
        return delegate.createLogContainerCmdExec();
    }

    @Override
    public CopyFileFromContainerCmd.Exec createCopyFileFromContainerCmdExec() {
        return delegate.createCopyFileFromContainerCmdExec();
    }

    @Override
    public CopyArchiveFromContainerCmd.Exec createCopyArchiveFromContainerCmdExec() {
        return delegate.createCopyArchiveFromContainerCmdExec();
    }

    @Override
    public CopyArchiveToContainerCmd.Exec createCopyArchiveToContainerCmdExec() {
        return delegate.createCopyArchiveToContainerCmdExec();
    }

    @Override
    public StopContainerCmd.Exec createStopContainerCmdExec() {
        return delegate.createStopContainerCmdExec();
    }

    @Override
    public ContainerDiffCmd.Exec createContainerDiffCmdExec() {
        return delegate.createContainerDiffCmdExec();
    }

    @Override
    public KillContainerCmd.Exec createKillContainerCmdExec() {
        return delegate.createKillContainerCmdExec();
    }

    @Override
    public UpdateContainerCmd.Exec createUpdateContainerCmdExec() {
        return delegate.createUpdateContainerCmdExec();
    }

    @Override
    public RenameContainerCmd.Exec createRenameContainerCmdExec() {
        return delegate.createRenameContainerCmdExec();
    }

    @Override
    public RestartContainerCmd.Exec createRestartContainerCmdExec() {
        return delegate.createRestartContainerCmdExec();
    }

    @Override
    public CommitCmd.Exec createCommitCmdExec() {
        return delegate.createCommitCmdExec();
    }

    @Override
    public BuildImageCmd.Exec createBuildImageCmdExec() {
        return delegate.createBuildImageCmdExec();
    }

    @Override
    public TopContainerCmd.Exec createTopContainerCmdExec() {
        return delegate.createTopContainerCmdExec();
    }

    @Override
    public TagImageCmd.Exec createTagImageCmdExec() {
        return delegate.createTagImageCmdExec();
    }

    @Override
    public PauseContainerCmd.Exec createPauseContainerCmdExec() {
        return delegate.createPauseContainerCmdExec();
    }

    @Override
    public UnpauseContainerCmd.Exec createUnpauseContainerCmdExec() {
        return delegate.createUnpauseContainerCmdExec();
    }

    @Override
    public EventsCmd.Exec createEventsCmdExec() {
        return delegate.createEventsCmdExec();
    }

    @Override
    public StatsCmd.Exec createStatsCmdExec() {
        return delegate.createStatsCmdExec();
    }

    @Override
    public CreateVolumeCmd.Exec createCreateVolumeCmdExec() {
        return delegate.createCreateVolumeCmdExec();
    }

    @Override
    public InspectVolumeCmd.Exec createInspectVolumeCmdExec() {
        return delegate.createInspectVolumeCmdExec();
    }

    @Override
    public RemoveVolumeCmd.Exec createRemoveVolumeCmdExec() {
        return delegate.createRemoveVolumeCmdExec();
    }

    @Override
    public ListVolumesCmd.Exec createListVolumesCmdExec() {
        return delegate.createListVolumesCmdExec();
    }

    @Override
    public ListNetworksCmd.Exec createListNetworksCmdExec() {
        return delegate.createListNetworksCmdExec();
    }

    @Override
    public InspectNetworkCmd.Exec createInspectNetworkCmdExec() {
        return delegate.createInspectNetworkCmdExec();
    }

    @Override
    public CreateNetworkCmd.Exec createCreateNetworkCmdExec() {
        return delegate.createCreateNetworkCmdExec();
    }

    @Override
    public RemoveNetworkCmd.Exec createRemoveNetworkCmdExec() {
        return delegate.createRemoveNetworkCmdExec();
    }

    @Override
    public ConnectToNetworkCmd.Exec createConnectToNetworkCmdExec() {
        return delegate.createConnectToNetworkCmdExec();
    }

    @Override
    public DisconnectFromNetworkCmd.Exec createDisconnectFromNetworkCmdExec() {
        return delegate.createDisconnectFromNetworkCmdExec();
    }

    @Override
    public InitializeSwarmCmd.Exec createInitializeSwarmCmdExec() {
        return delegate.createInitializeSwarmCmdExec();
    }

    @Override
    public InspectSwarmCmd.Exec createInspectSwarmCmdExec() {
        return delegate.createInspectSwarmCmdExec();
    }

    @Override
    public JoinSwarmCmd.Exec createJoinSwarmCmdExec() {
        return delegate.createJoinSwarmCmdExec();
    }

    @Override
    public LeaveSwarmCmd.Exec createLeaveSwarmCmdExec() {
        return delegate.createLeaveSwarmCmdExec();
    }

    @Override
    public UpdateSwarmCmd.Exec createUpdateSwarmCmdExec() {
        return delegate.createUpdateSwarmCmdExec();
    }

    @Override
    public ListServicesCmd.Exec createListServicesCmdExec() {
        return delegate.createListServicesCmdExec();
    }

    @Override
    public CreateServiceCmd.Exec createCreateServiceCmdExec() {
        return delegate.createCreateServiceCmdExec();
    }

    @Override
    public InspectServiceCmd.Exec createInspectServiceCmdExec() {
        return delegate.createInspectServiceCmdExec();
    }

    @Override
    public UpdateServiceCmd.Exec createUpdateServiceCmdExec() {
        return delegate.createUpdateServiceCmdExec();
    }

    @Override
    public RemoveServiceCmd.Exec createRemoveServiceCmdExec() {
        return delegate.createRemoveServiceCmdExec();
    }

    @Override
    public LogSwarmObjectCmd.Exec logSwarmObjectExec(String endpoint) {
        return delegate.logSwarmObjectExec(endpoint);
    }

    @Override
    public ListSwarmNodesCmd.Exec listSwarmNodeCmdExec() {
        return delegate.listSwarmNodeCmdExec();
    }

    @Override
    public InspectSwarmNodeCmd.Exec inspectSwarmNodeCmdExec() {
        return delegate.inspectSwarmNodeCmdExec();
    }

    @Override
    public RemoveSwarmNodeCmd.Exec removeSwarmNodeCmdExec() {
        return delegate.removeSwarmNodeCmdExec();
    }

    @Override
    public UpdateSwarmNodeCmd.Exec updateSwarmNodeCmdExec() {
        return delegate.updateSwarmNodeCmdExec();
    }

    @Override
    public ListTasksCmd.Exec listTasksCmdExec() {
        return delegate.listTasksCmdExec();
    }

    @Override
    public PruneCmd.Exec pruneCmdExec() {
        return delegate.pruneCmdExec();
    }

    @Override
    public ListSecretsCmd.Exec createListSecretsCmdExec() {
        return delegate.createListSecretsCmdExec();
    }

    @Override
    public CreateSecretCmd.Exec createCreateSecretCmdExec() {
        return delegate.createCreateSecretCmdExec();
    }

    @Override
    public RemoveSecretCmd.Exec createRemoveSecretCmdExec() {
        return delegate.createRemoveSecretCmdExec();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
