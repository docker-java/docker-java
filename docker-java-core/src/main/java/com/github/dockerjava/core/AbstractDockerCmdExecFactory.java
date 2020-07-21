package com.github.dockerjava.core;

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
import com.github.dockerjava.api.command.ResizeContainerCmd;
import com.github.dockerjava.api.command.ResizeExecCmd;
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
import com.github.dockerjava.core.exec.AttachContainerCmdExec;
import com.github.dockerjava.core.exec.AuthCmdExec;
import com.github.dockerjava.core.exec.BuildImageCmdExec;
import com.github.dockerjava.core.exec.CommitCmdExec;
import com.github.dockerjava.core.exec.ConnectToNetworkCmdExec;
import com.github.dockerjava.core.exec.ContainerDiffCmdExec;
import com.github.dockerjava.core.exec.CopyArchiveFromContainerCmdExec;
import com.github.dockerjava.core.exec.CopyArchiveToContainerCmdExec;
import com.github.dockerjava.core.exec.CopyFileFromContainerCmdExec;
import com.github.dockerjava.core.exec.CreateContainerCmdExec;
import com.github.dockerjava.core.exec.CreateImageCmdExec;
import com.github.dockerjava.core.exec.CreateNetworkCmdExec;
import com.github.dockerjava.core.exec.CreateSecretCmdExec;
import com.github.dockerjava.core.exec.CreateServiceCmdExec;
import com.github.dockerjava.core.exec.CreateVolumeCmdExec;
import com.github.dockerjava.core.exec.DisconnectFromNetworkCmdExec;
import com.github.dockerjava.core.exec.EventsCmdExec;
import com.github.dockerjava.core.exec.ExecCreateCmdExec;
import com.github.dockerjava.core.exec.ExecStartCmdExec;
import com.github.dockerjava.core.exec.ResizeContainerCmdExec;
import com.github.dockerjava.core.exec.ResizeExecCmdExec;
import com.github.dockerjava.core.exec.InfoCmdExec;
import com.github.dockerjava.core.exec.InitializeSwarmCmdExec;
import com.github.dockerjava.core.exec.InspectContainerCmdExec;
import com.github.dockerjava.core.exec.InspectExecCmdExec;
import com.github.dockerjava.core.exec.InspectImageCmdExec;
import com.github.dockerjava.core.exec.InspectNetworkCmdExec;
import com.github.dockerjava.core.exec.InspectServiceCmdExec;
import com.github.dockerjava.core.exec.InspectSwarmCmdExec;
import com.github.dockerjava.core.exec.InspectSwarmNodeCmdExec;
import com.github.dockerjava.core.exec.InspectVolumeCmdExec;
import com.github.dockerjava.core.exec.JoinSwarmCmdExec;
import com.github.dockerjava.core.exec.KillContainerCmdExec;
import com.github.dockerjava.core.exec.LeaveSwarmCmdExec;
import com.github.dockerjava.core.exec.ListContainersCmdExec;
import com.github.dockerjava.core.exec.ListImagesCmdExec;
import com.github.dockerjava.core.exec.ListNetworksCmdExec;
import com.github.dockerjava.core.exec.ListSecretsCmdExec;
import com.github.dockerjava.core.exec.ListServicesCmdExec;
import com.github.dockerjava.core.exec.ListSwarmNodesCmdExec;
import com.github.dockerjava.core.exec.ListTasksCmdExec;
import com.github.dockerjava.core.exec.ListVolumesCmdExec;
import com.github.dockerjava.core.exec.LoadImageCmdExec;
import com.github.dockerjava.core.exec.LogContainerCmdExec;
import com.github.dockerjava.core.exec.LogSwarmObjectExec;
import com.github.dockerjava.core.exec.PauseContainerCmdExec;
import com.github.dockerjava.core.exec.PingCmdExec;
import com.github.dockerjava.core.exec.PruneCmdExec;
import com.github.dockerjava.core.exec.PullImageCmdExec;
import com.github.dockerjava.core.exec.PushImageCmdExec;
import com.github.dockerjava.core.exec.RemoveContainerCmdExec;
import com.github.dockerjava.core.exec.RemoveImageCmdExec;
import com.github.dockerjava.core.exec.RemoveNetworkCmdExec;
import com.github.dockerjava.core.exec.RemoveSecretCmdExec;
import com.github.dockerjava.core.exec.RemoveServiceCmdExec;
import com.github.dockerjava.core.exec.RemoveSwarmNodeCmdExec;
import com.github.dockerjava.core.exec.RemoveVolumeCmdExec;
import com.github.dockerjava.core.exec.RenameContainerCmdExec;
import com.github.dockerjava.core.exec.RestartContainerCmdExec;
import com.github.dockerjava.core.exec.SaveImageCmdExec;
import com.github.dockerjava.core.exec.SaveImagesCmdExec;
import com.github.dockerjava.core.exec.SearchImagesCmdExec;
import com.github.dockerjava.core.exec.StartContainerCmdExec;
import com.github.dockerjava.core.exec.StatsCmdExec;
import com.github.dockerjava.core.exec.StopContainerCmdExec;
import com.github.dockerjava.core.exec.TagImageCmdExec;
import com.github.dockerjava.core.exec.TopContainerCmdExec;
import com.github.dockerjava.core.exec.UnpauseContainerCmdExec;
import com.github.dockerjava.core.exec.UpdateContainerCmdExec;
import com.github.dockerjava.core.exec.UpdateServiceCmdExec;
import com.github.dockerjava.core.exec.UpdateSwarmCmdExec;
import com.github.dockerjava.core.exec.UpdateSwarmNodeCmdExec;
import com.github.dockerjava.core.exec.VersionCmdExec;
import com.github.dockerjava.core.exec.WaitContainerCmdExec;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractDockerCmdExecFactory implements DockerCmdExecFactory, DockerClientConfigAware {

    private DockerClientConfig dockerClientConfig;

    protected Integer connectTimeout;
    protected Integer readTimeout;

    protected DockerClientConfig getDockerClientConfig() {
        checkNotNull(dockerClientConfig,
                "Factor not initialized, dockerClientConfig not set. You probably forgot to call init()!");
        return dockerClientConfig;
    }

    @Override
    public void init(DockerClientConfig dockerClientConfig) {
        checkNotNull(dockerClientConfig, "config was not specified");
        this.dockerClientConfig = dockerClientConfig;
    }

    @Override
    public CopyArchiveFromContainerCmd.Exec createCopyArchiveFromContainerCmdExec() {
        return new CopyArchiveFromContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CopyArchiveToContainerCmd.Exec createCopyArchiveToContainerCmdExec() {
        return new CopyArchiveToContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    /**
     * Configure connection timeout in milliseconds
     */
    public AbstractDockerCmdExecFactory withConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * Configure read timeout in milliseconds
     */
    public AbstractDockerCmdExecFactory withReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    @Override
    public AuthCmd.Exec createAuthCmdExec() {
        return new AuthCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InfoCmd.Exec createInfoCmdExec() {
        return new InfoCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public PingCmd.Exec createPingCmdExec() {
        return new PingCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public VersionCmd.Exec createVersionCmdExec() {
        return new VersionCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public PullImageCmd.Exec createPullImageCmdExec() {
        return new PullImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public PushImageCmd.Exec createPushImageCmdExec() {
        return new PushImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public SaveImageCmd.Exec createSaveImageCmdExec() {
        return new SaveImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public SaveImagesCmd.Exec createSaveImagesCmdExec() {
        return new SaveImagesCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CreateImageCmd.Exec createCreateImageCmdExec() {
        return new CreateImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public LoadImageCmd.Exec createLoadImageCmdExec() {
        return new LoadImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public SearchImagesCmd.Exec createSearchImagesCmdExec() {
        return new SearchImagesCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveImageCmd.Exec createRemoveImageCmdExec() {
        return new RemoveImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ListImagesCmd.Exec createListImagesCmdExec() {
        return new ListImagesCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectImageCmd.Exec createInspectImageCmdExec() {
        return new InspectImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ListContainersCmd.Exec createListContainersCmdExec() {
        return new ListContainersCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CreateContainerCmd.Exec createCreateContainerCmdExec() {
        return new CreateContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public StartContainerCmd.Exec createStartContainerCmdExec() {
        return new StartContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectContainerCmd.Exec createInspectContainerCmdExec() {
        return new InspectContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ExecCreateCmd.Exec createExecCmdExec() {
        return new ExecCreateCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveContainerCmd.Exec createRemoveContainerCmdExec() {
        return new RemoveContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public WaitContainerCmd.Exec createWaitContainerCmdExec() {
        return new WaitContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public AttachContainerCmd.Exec createAttachContainerCmdExec() {
        return new AttachContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ResizeContainerCmd.Exec createResizeContainerCmdExec() {
        return new ResizeContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ExecStartCmd.Exec createExecStartCmdExec() {
        return new ExecStartCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ResizeExecCmd.Exec createResizeExecCmdExec() {
        return new ResizeExecCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectExecCmd.Exec createInspectExecCmdExec() {
        return new InspectExecCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public LogContainerCmd.Exec createLogContainerCmdExec() {
        return new LogContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CopyFileFromContainerCmd.Exec createCopyFileFromContainerCmdExec() {
        return new CopyFileFromContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public StopContainerCmd.Exec createStopContainerCmdExec() {
        return new StopContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ContainerDiffCmd.Exec createContainerDiffCmdExec() {
        return new ContainerDiffCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public KillContainerCmd.Exec createKillContainerCmdExec() {
        return new KillContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public UpdateContainerCmd.Exec createUpdateContainerCmdExec() {
        return new UpdateContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RenameContainerCmd.Exec createRenameContainerCmdExec() {
        return new RenameContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RestartContainerCmd.Exec createRestartContainerCmdExec() {
        return new RestartContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CommitCmd.Exec createCommitCmdExec() {
        return new CommitCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public BuildImageCmd.Exec createBuildImageCmdExec() {
        return new BuildImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public TopContainerCmd.Exec createTopContainerCmdExec() {
        return new TopContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public TagImageCmd.Exec createTagImageCmdExec() {
        return new TagImageCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public PauseContainerCmd.Exec createPauseContainerCmdExec() {
        return new PauseContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public UnpauseContainerCmd.Exec createUnpauseContainerCmdExec() {
        return new UnpauseContainerCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public EventsCmd.Exec createEventsCmdExec() {
        return new EventsCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public StatsCmd.Exec createStatsCmdExec() {
        return new StatsCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CreateVolumeCmd.Exec createCreateVolumeCmdExec() {
        return new CreateVolumeCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectVolumeCmd.Exec createInspectVolumeCmdExec() {
        return new InspectVolumeCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveVolumeCmd.Exec createRemoveVolumeCmdExec() {
        return new RemoveVolumeCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ListVolumesCmd.Exec createListVolumesCmdExec() {
        return new ListVolumesCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ListNetworksCmd.Exec createListNetworksCmdExec() {
        return new ListNetworksCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectNetworkCmd.Exec createInspectNetworkCmdExec() {
        return new InspectNetworkCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CreateNetworkCmd.Exec createCreateNetworkCmdExec() {
        return new CreateNetworkCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveNetworkCmd.Exec createRemoveNetworkCmdExec() {
        return new RemoveNetworkCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ConnectToNetworkCmd.Exec createConnectToNetworkCmdExec() {
        return new ConnectToNetworkCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public DisconnectFromNetworkCmd.Exec createDisconnectFromNetworkCmdExec() {
        return new DisconnectFromNetworkCmdExec(getBaseResource(), getDockerClientConfig());
    }

    // swarm
    @Override
    public InitializeSwarmCmd.Exec createInitializeSwarmCmdExec() {
        return new InitializeSwarmCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectSwarmCmd.Exec createInspectSwarmCmdExec() {
        return new InspectSwarmCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public JoinSwarmCmd.Exec createJoinSwarmCmdExec() {
        return new JoinSwarmCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public LeaveSwarmCmd.Exec createLeaveSwarmCmdExec() {
        return new LeaveSwarmCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public UpdateSwarmCmd.Exec createUpdateSwarmCmdExec() {
        return new UpdateSwarmCmdExec(getBaseResource(), getDockerClientConfig());
    }

    // services
    @Override
    public ListServicesCmd.Exec createListServicesCmdExec() {
        return new ListServicesCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CreateServiceCmd.Exec createCreateServiceCmdExec() {
        return new CreateServiceCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectServiceCmd.Exec createInspectServiceCmdExec() {
        return new InspectServiceCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public UpdateServiceCmd.Exec createUpdateServiceCmdExec() {
        return new UpdateServiceCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveServiceCmd.Exec createRemoveServiceCmdExec() {
        return new RemoveServiceCmdExec(getBaseResource(), getDockerClientConfig());
    }

    // nodes
    @Override
    public ListSwarmNodesCmd.Exec listSwarmNodeCmdExec() {
        return new ListSwarmNodesCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public InspectSwarmNodeCmd.Exec inspectSwarmNodeCmdExec() {
        return new InspectSwarmNodeCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveSwarmNodeCmd.Exec removeSwarmNodeCmdExec() {
        return new RemoveSwarmNodeCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public UpdateSwarmNodeCmd.Exec updateSwarmNodeCmdExec() {
        return new UpdateSwarmNodeCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ListTasksCmd.Exec listTasksCmdExec() {
        return new ListTasksCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public LogSwarmObjectCmd.Exec logSwarmObjectExec(String endpoint) {
        return new LogSwarmObjectExec(getBaseResource(), getDockerClientConfig(), endpoint);
    }

    @Override
    public PruneCmd.Exec pruneCmdExec() {
        return new PruneCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public ListSecretsCmd.Exec createListSecretsCmdExec() {
        return new ListSecretsCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public CreateSecretCmd.Exec createCreateSecretCmdExec() {
        return new CreateSecretCmdExec(getBaseResource(), getDockerClientConfig());
    }

    @Override
    public RemoveSecretCmd.Exec createRemoveSecretCmdExec() {
        return new RemoveSecretCmdExec(getBaseResource(), getDockerClientConfig());
    }

    protected abstract WebTarget getBaseResource();
}
