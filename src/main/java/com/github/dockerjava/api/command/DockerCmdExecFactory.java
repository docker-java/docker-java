package com.github.dockerjava.api.command;

import java.io.Closeable;
import java.io.IOException;

import com.github.dockerjava.core.DockerClientConfig;

public interface DockerCmdExecFactory extends Closeable {

    public void init(DockerClientConfig dockerClientConfig);

    public AuthCmd.Exec createAuthCmdExec();

    public InfoCmd.Exec createInfoCmdExec();

    public PingCmd.Exec createPingCmdExec();

    public ExecCreateCmd.Exec createExecCmdExec();

    public VersionCmd.Exec createVersionCmdExec();

    public PullImageCmd.Exec createPullImageCmdExec();

    public PushImageCmd.Exec createPushImageCmdExec();

    public SaveImageCmd.Exec createSaveImageCmdExec();

    public CreateImageCmd.Exec createCreateImageCmdExec();

    public SearchImagesCmd.Exec createSearchImagesCmdExec();

    public RemoveImageCmd.Exec createRemoveImageCmdExec();

    public ListImagesCmd.Exec createListImagesCmdExec();

    public InspectImageCmd.Exec createInspectImageCmdExec();

    public ListContainersCmd.Exec createListContainersCmdExec();

    public CreateContainerCmd.Exec createCreateContainerCmdExec();

    public StartContainerCmd.Exec createStartContainerCmdExec();

    public InspectContainerCmd.Exec createInspectContainerCmdExec();

    public RemoveContainerCmd.Exec createRemoveContainerCmdExec();

    public WaitContainerCmd.Exec createWaitContainerCmdExec();

    public AttachContainerCmd.Exec createAttachContainerCmdExec();

    public ExecStartCmd.Exec createExecStartCmdExec();

    public InspectExecCmd.Exec createInspectExecCmdExec();

    public LogContainerCmd.Exec createLogContainerCmdExec();

    public CopyFileFromContainerCmd.Exec createCopyFileFromContainerCmdExec();

    public CopyArchiveFromContainerCmd.Exec createCopyArchiveFromContainerCmdExec();

    public CopyArchiveToContainerCmd.Exec createCopyArchiveToContainerCmdExec();

    public StopContainerCmd.Exec createStopContainerCmdExec();

    public ContainerDiffCmd.Exec createContainerDiffCmdExec();

    public KillContainerCmd.Exec createKillContainerCmdExec();

    public RestartContainerCmd.Exec createRestartContainerCmdExec();

    public CommitCmd.Exec createCommitCmdExec();

    public BuildImageCmd.Exec createBuildImageCmdExec();

    public TopContainerCmd.Exec createTopContainerCmdExec();

    public TagImageCmd.Exec createTagImageCmdExec();

    public PauseContainerCmd.Exec createPauseContainerCmdExec();

    public UnpauseContainerCmd.Exec createUnpauseContainerCmdExec();

    public EventsCmd.Exec createEventsCmdExec();

    public StatsCmd.Exec createStatsCmdExec();

    public CreateVolumeCmd.Exec createCreateVolumeCmdExec();

    public InspectVolumeCmd.Exec createInspectVolumeCmdExec();

    public RemoveVolumeCmd.Exec createRemoveVolumeCmdExec();

    public ListVolumesCmd.Exec createListVolumesCmdExec();

    public ListNetworksCmd.Exec createListNetworksCmdExec();

    public InspectNetworkCmd.Exec createInspectNetworkCmdExec();

    public CreateNetworkCmd.Exec createCreateNetworkCmdExec();

    @Override
    public void close() throws IOException;

}
