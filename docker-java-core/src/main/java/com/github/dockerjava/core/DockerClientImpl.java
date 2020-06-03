package com.github.dockerjava.core;

import com.github.dockerjava.api.DockerClient;
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
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Identifier;
import com.github.dockerjava.api.model.PruneType;
import com.github.dockerjava.api.model.SecretSpec;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.core.command.AttachContainerCmdImpl;
import com.github.dockerjava.core.command.AuthCmdImpl;
import com.github.dockerjava.core.command.BuildImageCmdImpl;
import com.github.dockerjava.core.command.CommitCmdImpl;
import com.github.dockerjava.core.command.ConnectToNetworkCmdImpl;
import com.github.dockerjava.core.command.ContainerDiffCmdImpl;
import com.github.dockerjava.core.command.CopyArchiveFromContainerCmdImpl;
import com.github.dockerjava.core.command.CopyArchiveToContainerCmdImpl;
import com.github.dockerjava.core.command.CopyFileFromContainerCmdImpl;
import com.github.dockerjava.core.command.CreateContainerCmdImpl;
import com.github.dockerjava.core.command.CreateImageCmdImpl;
import com.github.dockerjava.core.command.CreateNetworkCmdImpl;
import com.github.dockerjava.core.command.CreateSecretCmdImpl;
import com.github.dockerjava.core.command.CreateServiceCmdImpl;
import com.github.dockerjava.core.command.CreateVolumeCmdImpl;
import com.github.dockerjava.core.command.DisconnectFromNetworkCmdImpl;
import com.github.dockerjava.core.command.EventsCmdImpl;
import com.github.dockerjava.core.command.ExecCreateCmdImpl;
import com.github.dockerjava.core.command.ExecStartCmdImpl;
import com.github.dockerjava.core.command.InfoCmdImpl;
import com.github.dockerjava.core.command.InitializeSwarmCmdImpl;
import com.github.dockerjava.core.command.InpectNetworkCmdImpl;
import com.github.dockerjava.core.command.InspectContainerCmdImpl;
import com.github.dockerjava.core.command.InspectExecCmdImpl;
import com.github.dockerjava.core.command.InspectImageCmdImpl;
import com.github.dockerjava.core.command.InspectServiceCmdImpl;
import com.github.dockerjava.core.command.InspectSwarmCmdImpl;
import com.github.dockerjava.core.command.InspectVolumeCmdImpl;
import com.github.dockerjava.core.command.JoinSwarmCmdImpl;
import com.github.dockerjava.core.command.KillContainerCmdImpl;
import com.github.dockerjava.core.command.LeaveSwarmCmdImpl;
import com.github.dockerjava.core.command.ListContainersCmdImpl;
import com.github.dockerjava.core.command.ListImagesCmdImpl;
import com.github.dockerjava.core.command.ListNetworksCmdImpl;
import com.github.dockerjava.core.command.ListSecretsCmdImpl;
import com.github.dockerjava.core.command.ListServicesCmdImpl;
import com.github.dockerjava.core.command.ListSwarmNodesCmdImpl;
import com.github.dockerjava.core.command.ListTasksCmdImpl;
import com.github.dockerjava.core.command.ListVolumesCmdImpl;
import com.github.dockerjava.core.command.LoadImageCmdImpl;
import com.github.dockerjava.core.command.LogContainerCmdImpl;
import com.github.dockerjava.core.command.LogSwarmObjectImpl;
import com.github.dockerjava.core.command.PauseContainerCmdImpl;
import com.github.dockerjava.core.command.PingCmdImpl;
import com.github.dockerjava.core.command.PruneCmdImpl;
import com.github.dockerjava.core.command.PullImageCmdImpl;
import com.github.dockerjava.core.command.PushImageCmdImpl;
import com.github.dockerjava.core.command.RemoveContainerCmdImpl;
import com.github.dockerjava.core.command.RemoveImageCmdImpl;
import com.github.dockerjava.core.command.RemoveNetworkCmdImpl;
import com.github.dockerjava.core.command.RemoveSecretCmdImpl;
import com.github.dockerjava.core.command.RemoveServiceCmdImpl;
import com.github.dockerjava.core.command.RemoveVolumeCmdImpl;
import com.github.dockerjava.core.command.RenameContainerCmdImpl;
import com.github.dockerjava.core.command.RestartContainerCmdImpl;
import com.github.dockerjava.core.command.SaveImageCmdImpl;
import com.github.dockerjava.core.command.SaveImagesCmdImpl;
import com.github.dockerjava.core.command.SearchImagesCmdImpl;
import com.github.dockerjava.core.command.StartContainerCmdImpl;
import com.github.dockerjava.core.command.StatsCmdImpl;
import com.github.dockerjava.core.command.StopContainerCmdImpl;
import com.github.dockerjava.core.command.TagImageCmdImpl;
import com.github.dockerjava.core.command.TopContainerCmdImpl;
import com.github.dockerjava.core.command.UnpauseContainerCmdImpl;
import com.github.dockerjava.core.command.UpdateContainerCmdImpl;
import com.github.dockerjava.core.command.UpdateServiceCmdImpl;
import com.github.dockerjava.core.command.UpdateSwarmCmdImpl;
import com.github.dockerjava.core.command.UpdateSwarmNodeCmdImpl;
import com.github.dockerjava.core.command.VersionCmdImpl;
import com.github.dockerjava.core.command.WaitContainerCmdImpl;
import com.github.dockerjava.transport.DockerHttpClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 * @see "https://github.com/docker/docker/blob/master/api/client/commands.go"
 */
public class DockerClientImpl implements Closeable, DockerClient {

    private final DockerClientConfig dockerClientConfig;

    DockerCmdExecFactory dockerCmdExecFactory;

    DockerClientImpl(DockerClientConfig dockerClientConfig) {
        checkNotNull(dockerClientConfig, "config was not specified");
        this.dockerClientConfig = dockerClientConfig;
    }

    /**
     *
     * @deprecated use {@link #getInstance(DockerClientConfig, DockerHttpClient)}
     */
    @Deprecated
    public static DockerClientImpl getInstance() {
        return new DockerClientImpl(DefaultDockerClientConfig.createDefaultConfigBuilder().build());
    }

    /**
     *
     * @deprecated use {@link #getInstance(DockerClientConfig, DockerHttpClient)}
     */
    @Deprecated
    public static DockerClientImpl getInstance(DockerClientConfig dockerClientConfig) {
        return new DockerClientImpl(dockerClientConfig);
    }

    public static DockerClient getInstance(DockerClientConfig dockerClientConfig, DockerHttpClient dockerHttpClient) {
        return new DockerClientImpl(dockerClientConfig)
            .withHttpClient(dockerHttpClient);
    }

    /**
     *
     * @deprecated use {@link #getInstance(DockerClientConfig, DockerHttpClient)}
     */
    @Deprecated
    public static DockerClientImpl getInstance(String serverUrl) {
        return new DockerClientImpl(
            DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(serverUrl)
                .build()
        );
    }

    DockerClientImpl withHttpClient(DockerHttpClient httpClient) {
        return withDockerCmdExecFactory(new DefaultDockerCmdExecFactory(httpClient, dockerClientConfig.getObjectMapper()));
    }

    /**
     *
     * @return {@link DockerHttpClient} or null if not set
     */
    @Nullable
    public DockerHttpClient getHttpClient() {
        if (dockerCmdExecFactory instanceof DefaultDockerCmdExecFactory) {
            return ((DefaultDockerCmdExecFactory) dockerCmdExecFactory).getDockerHttpClient();
        } else {
            return null;
        }
    }

    /**
     * @deprecated use {@link #getInstance(DockerClientConfig, DockerHttpClient)}
     */
    @Deprecated
    public DockerClientImpl withDockerCmdExecFactory(DockerCmdExecFactory dockerCmdExecFactory) {
        checkNotNull(dockerCmdExecFactory, "dockerCmdExecFactory was not specified");
        this.dockerCmdExecFactory = dockerCmdExecFactory;
        if (dockerCmdExecFactory instanceof DockerClientConfigAware) {
            ((DockerClientConfigAware) dockerCmdExecFactory).init(dockerClientConfig);
        }
        return this;
    }

    @Deprecated
    private DockerCmdExecFactory getDockerCmdExecFactory() {
        checkNotNull(dockerCmdExecFactory, "dockerCmdExecFactory was not specified");
        return dockerCmdExecFactory;
    }

    @Override
    public AuthConfig authConfig() {
        checkNotNull(dockerClientConfig.getRegistryUsername(), "Configured username is null.");
        checkNotNull(dockerClientConfig.getRegistryUrl(), "Configured serverAddress is null.");

        return new AuthConfig()
                .withUsername(dockerClientConfig.getRegistryUsername())
                .withPassword(dockerClientConfig.getRegistryPassword())
                .withEmail(dockerClientConfig.getRegistryEmail())
                .withRegistryAddress(dockerClientConfig.getRegistryUrl());
    }

    /**
     * * MISC API *
     */

    /**
     * Authenticate with the server, useful for checking authentication.
     */
    @Override
    public AuthCmd authCmd() {
        return new AuthCmdImpl(getDockerCmdExecFactory().createAuthCmdExec(), authConfig());
    }

    @Override
    public InfoCmd infoCmd() {
        return new InfoCmdImpl(getDockerCmdExecFactory().createInfoCmdExec());
    }

    @Override
    public PingCmd pingCmd() {
        return new PingCmdImpl(getDockerCmdExecFactory().createPingCmdExec());
    }

    @Override
    public VersionCmd versionCmd() {
        return new VersionCmdImpl(getDockerCmdExecFactory().createVersionCmdExec());
    }

    /**
     * * IMAGE API *
     */
    @Override
    public PullImageCmd pullImageCmd(String repository) {
        return new PullImageCmdImpl(getDockerCmdExecFactory().createPullImageCmdExec(),
                dockerClientConfig.effectiveAuthConfig(repository), repository);
    }

    @Override
    public PushImageCmd pushImageCmd(String name) {
        PushImageCmd cmd = new PushImageCmdImpl(getDockerCmdExecFactory().createPushImageCmdExec(),
                dockerClientConfig.effectiveAuthConfig(name), name);
        return cmd;
    }

    @Override
    public PushImageCmd pushImageCmd(Identifier identifier) {
        PushImageCmd cmd = pushImageCmd(identifier.repository.name);
        if (identifier.tag.isPresent()) {
            cmd.withTag(identifier.tag.get());
        }

        AuthConfig cfg = dockerClientConfig.effectiveAuthConfig(identifier.repository.name);
        if (cfg != null) {
            cmd.withAuthConfig(cfg);
        }

        return cmd;
    }

    @Override
    public SaveImageCmd saveImageCmd(String name) {
        return new SaveImageCmdImpl(getDockerCmdExecFactory().createSaveImageCmdExec(), name);
    }

    @Override
    public SaveImagesCmd saveImagesCmd() {
        return new SaveImagesCmdImpl(getDockerCmdExecFactory().createSaveImagesCmdExec());
    }

    @Override
    public CreateImageCmd createImageCmd(String repository, InputStream imageStream) {
        return new CreateImageCmdImpl(getDockerCmdExecFactory().createCreateImageCmdExec(), repository, imageStream);
    }

    @Override
    public LoadImageCmd loadImageCmd(@Nonnull InputStream imageStream) {
        return new LoadImageCmdImpl(getDockerCmdExecFactory().createLoadImageCmdExec(), imageStream);
    }

    @Override
    public SearchImagesCmd searchImagesCmd(String term) {
        return new SearchImagesCmdImpl(getDockerCmdExecFactory().createSearchImagesCmdExec(), term);
    }

    @Override
    public RemoveImageCmd removeImageCmd(String imageId) {
        return new RemoveImageCmdImpl(getDockerCmdExecFactory().createRemoveImageCmdExec(), imageId);
    }

    @Override
    public ListImagesCmd listImagesCmd() {
        return new ListImagesCmdImpl(getDockerCmdExecFactory().createListImagesCmdExec());
    }

    @Override
    public InspectImageCmd inspectImageCmd(String imageId) {
        return new InspectImageCmdImpl(getDockerCmdExecFactory().createInspectImageCmdExec(), imageId);
    }

    /**
     * * CONTAINER API *
     */

    @Override
    public ListContainersCmd listContainersCmd() {
        return new ListContainersCmdImpl(getDockerCmdExecFactory().createListContainersCmdExec());
    }

    @Override
    public CreateContainerCmd createContainerCmd(String image) {
        return new CreateContainerCmdImpl(getDockerCmdExecFactory()
                .createCreateContainerCmdExec(), dockerClientConfig.effectiveAuthConfig(image), image);
    }

    @Override
    public StartContainerCmd startContainerCmd(String containerId) {
        return new StartContainerCmdImpl(getDockerCmdExecFactory().createStartContainerCmdExec(), containerId);
    }

    @Override
    public InspectContainerCmd inspectContainerCmd(String containerId) {
        return new InspectContainerCmdImpl(getDockerCmdExecFactory().createInspectContainerCmdExec(), containerId);
    }

    @Override
    public ExecCreateCmd execCreateCmd(String containerId) {
        return new ExecCreateCmdImpl(getDockerCmdExecFactory().createExecCmdExec(), containerId);
    }

    @Override
    public RemoveContainerCmd removeContainerCmd(String containerId) {
        return new RemoveContainerCmdImpl(getDockerCmdExecFactory().createRemoveContainerCmdExec(), containerId);
    }

    @Override
    public WaitContainerCmd waitContainerCmd(String containerId) {
        return new WaitContainerCmdImpl(getDockerCmdExecFactory().createWaitContainerCmdExec(), containerId);
    }

    @Override
    public AttachContainerCmd attachContainerCmd(String containerId) {
        return new AttachContainerCmdImpl(getDockerCmdExecFactory().createAttachContainerCmdExec(), containerId);
    }

    @Override
    public ExecStartCmd execStartCmd(String execId) {
        return new ExecStartCmdImpl(getDockerCmdExecFactory().createExecStartCmdExec(), execId);
    }

    @Override
    public InspectExecCmd inspectExecCmd(String execId) {
        return new InspectExecCmdImpl(getDockerCmdExecFactory().createInspectExecCmdExec(), execId);
    }

    @Override
    public LogContainerCmd logContainerCmd(String containerId) {
        return new LogContainerCmdImpl(getDockerCmdExecFactory().createLogContainerCmdExec(), containerId);
    }

    @Override
    public CopyFileFromContainerCmd copyFileFromContainerCmd(String containerId, String resource) {
        return new CopyFileFromContainerCmdImpl(getDockerCmdExecFactory().createCopyFileFromContainerCmdExec(),
                containerId, resource);
    }

    @Override
    public CopyArchiveFromContainerCmd copyArchiveFromContainerCmd(String containerId, String resource) {
        return new CopyArchiveFromContainerCmdImpl(getDockerCmdExecFactory().createCopyArchiveFromContainerCmdExec(),
                containerId, resource);
    }

    @Override
    public CopyArchiveToContainerCmd copyArchiveToContainerCmd(String containerId) {
        return new CopyArchiveToContainerCmdImpl(getDockerCmdExecFactory().createCopyArchiveToContainerCmdExec(),
                containerId);
    }

    @Override
    public ContainerDiffCmd containerDiffCmd(String containerId) {
        return new ContainerDiffCmdImpl(getDockerCmdExecFactory().createContainerDiffCmdExec(), containerId);
    }

    @Override
    public StopContainerCmd stopContainerCmd(String containerId) {
        return new StopContainerCmdImpl(getDockerCmdExecFactory().createStopContainerCmdExec(), containerId);
    }

    @Override
    public KillContainerCmd killContainerCmd(String containerId) {
        return new KillContainerCmdImpl(getDockerCmdExecFactory().createKillContainerCmdExec(), containerId);
    }

    @Override
    public UpdateContainerCmd updateContainerCmd(@Nonnull String containerId) {
        return new UpdateContainerCmdImpl(getDockerCmdExecFactory().createUpdateContainerCmdExec(), containerId);
    }

    @Override
    public RenameContainerCmd renameContainerCmd(@Nonnull String containerId) {
        return new RenameContainerCmdImpl(getDockerCmdExecFactory().createRenameContainerCmdExec(), containerId);
    }

    @Override
    public RestartContainerCmd restartContainerCmd(String containerId) {
        return new RestartContainerCmdImpl(getDockerCmdExecFactory().createRestartContainerCmdExec(), containerId);
    }

    @Override
    public CommitCmd commitCmd(String containerId) {
        return new CommitCmdImpl(getDockerCmdExecFactory().createCommitCmdExec(), containerId);
    }

    @Override
    public BuildImageCmd buildImageCmd() {
        return new BuildImageCmdImpl(getDockerCmdExecFactory().createBuildImageCmdExec());
    }

    @Override
    public BuildImageCmd buildImageCmd(File dockerFileOrFolder) {
        return new BuildImageCmdImpl(getDockerCmdExecFactory().createBuildImageCmdExec(), dockerFileOrFolder);
    }

    @Override
    public BuildImageCmd buildImageCmd(InputStream tarInputStream) {
        return new BuildImageCmdImpl(getDockerCmdExecFactory().createBuildImageCmdExec(), tarInputStream);
    }

    @Override
    public TopContainerCmd topContainerCmd(String containerId) {
        return new TopContainerCmdImpl(getDockerCmdExecFactory().createTopContainerCmdExec(), containerId);
    }

    @Override
    public TagImageCmd tagImageCmd(String imageId, String imageNameWithRepository, String tag) {
        return new TagImageCmdImpl(getDockerCmdExecFactory().createTagImageCmdExec(), imageId, imageNameWithRepository, tag);
    }

    @Override
    public PauseContainerCmd pauseContainerCmd(String containerId) {
        return new PauseContainerCmdImpl(getDockerCmdExecFactory().createPauseContainerCmdExec(), containerId);
    }

    @Override
    public UnpauseContainerCmd unpauseContainerCmd(String containerId) {
        return new UnpauseContainerCmdImpl(getDockerCmdExecFactory().createUnpauseContainerCmdExec(), containerId);
    }

    @Override
    public EventsCmd eventsCmd() {
        return new EventsCmdImpl(getDockerCmdExecFactory().createEventsCmdExec());
    }

    @Override
    public StatsCmd statsCmd(String containerId) {
        return new StatsCmdImpl(getDockerCmdExecFactory().createStatsCmdExec(), containerId);
    }

    @Override
    public CreateVolumeCmd createVolumeCmd() {
        return new CreateVolumeCmdImpl(getDockerCmdExecFactory().createCreateVolumeCmdExec());
    }

    @Override
    public InspectVolumeCmd inspectVolumeCmd(String name) {
        return new InspectVolumeCmdImpl(getDockerCmdExecFactory().createInspectVolumeCmdExec(), name);
    }

    @Override
    public RemoveVolumeCmd removeVolumeCmd(String name) {
        return new RemoveVolumeCmdImpl(getDockerCmdExecFactory().createRemoveVolumeCmdExec(), name);
    }

    @Override
    public ListVolumesCmd listVolumesCmd() {
        return new ListVolumesCmdImpl(getDockerCmdExecFactory().createListVolumesCmdExec());
    }

    @Override
    public ListNetworksCmd listNetworksCmd() {
        return new ListNetworksCmdImpl(getDockerCmdExecFactory().createListNetworksCmdExec());
    }

    @Override
    public InspectNetworkCmd inspectNetworkCmd() {
        return new InpectNetworkCmdImpl(getDockerCmdExecFactory().createInspectNetworkCmdExec());
    }

    @Override
    public CreateNetworkCmd createNetworkCmd() {
        return new CreateNetworkCmdImpl(getDockerCmdExecFactory().createCreateNetworkCmdExec());
    }

    @Override
    public RemoveNetworkCmd removeNetworkCmd(String networkId) {
        return new RemoveNetworkCmdImpl(getDockerCmdExecFactory().createRemoveNetworkCmdExec(), networkId);
    }

    @Override
    public ConnectToNetworkCmd connectToNetworkCmd() {
        return new ConnectToNetworkCmdImpl(getDockerCmdExecFactory().createConnectToNetworkCmdExec());
    }

    @Override
    public DisconnectFromNetworkCmd disconnectFromNetworkCmd() {
        return new DisconnectFromNetworkCmdImpl(getDockerCmdExecFactory().createDisconnectFromNetworkCmdExec());
    }

    @Override
    public InitializeSwarmCmd initializeSwarmCmd(SwarmSpec swarmSpec) {
        return new InitializeSwarmCmdImpl(getDockerCmdExecFactory().createInitializeSwarmCmdExec(), swarmSpec);
    }

    @Override
    public InspectSwarmCmd inspectSwarmCmd() {
        return new InspectSwarmCmdImpl(getDockerCmdExecFactory().createInspectSwarmCmdExec());
    }

    @Override
    public JoinSwarmCmd joinSwarmCmd() {
        return new JoinSwarmCmdImpl(getDockerCmdExecFactory().createJoinSwarmCmdExec());
    }

    @Override
    public LeaveSwarmCmd leaveSwarmCmd() {
        return new LeaveSwarmCmdImpl(getDockerCmdExecFactory().createLeaveSwarmCmdExec());
    }

    @Override
    public UpdateSwarmCmd updateSwarmCmd(SwarmSpec swarmSpec) {
        return new UpdateSwarmCmdImpl(getDockerCmdExecFactory().createUpdateSwarmCmdExec(), swarmSpec);
    }

    @Override
    public UpdateSwarmNodeCmd updateSwarmNodeCmd() {
        return new UpdateSwarmNodeCmdImpl(getDockerCmdExecFactory().updateSwarmNodeCmdExec());
    }

    @Override
    public ListSwarmNodesCmd listSwarmNodesCmd() {
        return new ListSwarmNodesCmdImpl(getDockerCmdExecFactory().listSwarmNodeCmdExec());
    }

    @Override
    public ListServicesCmd listServicesCmd() {
        return new ListServicesCmdImpl(getDockerCmdExecFactory().createListServicesCmdExec());
    }

    @Override
    public CreateServiceCmd createServiceCmd(ServiceSpec serviceSpec) {
        return new CreateServiceCmdImpl(getDockerCmdExecFactory().createCreateServiceCmdExec(), serviceSpec);
    }

    @Override
    public InspectServiceCmd inspectServiceCmd(String serviceId) {
        return new InspectServiceCmdImpl(getDockerCmdExecFactory().createInspectServiceCmdExec(), serviceId);
    }

    @Override
    public UpdateServiceCmd updateServiceCmd(String serviceId, ServiceSpec serviceSpec) {
        return new UpdateServiceCmdImpl(getDockerCmdExecFactory().createUpdateServiceCmdExec(), serviceId, serviceSpec);
    }

    @Override
    public RemoveServiceCmd removeServiceCmd(String serviceId) {
        return new RemoveServiceCmdImpl(getDockerCmdExecFactory().createRemoveServiceCmdExec(), serviceId);
    }

    @Override
    public LogSwarmObjectCmd logServiceCmd(String serviceId) {
        return new LogSwarmObjectImpl(getDockerCmdExecFactory().logSwarmObjectExec("services"), serviceId);
    }

    @Override
    public LogSwarmObjectCmd logTaskCmd(String taskId) {
        return new LogSwarmObjectImpl(getDockerCmdExecFactory().logSwarmObjectExec("tasks"), taskId);
    }

    @Override
    public PruneCmd pruneCmd(PruneType pruneType) {
        return new PruneCmdImpl(getDockerCmdExecFactory().pruneCmdExec(), pruneType);
    }

    @Override
    public ListSecretsCmd listSecretsCmd() {
        return new ListSecretsCmdImpl(getDockerCmdExecFactory().createListSecretsCmdExec());
    }

    @Override
    public CreateSecretCmd createSecretCmd(SecretSpec secretSpec) {
        return new CreateSecretCmdImpl(getDockerCmdExecFactory().createCreateSecretCmdExec(), secretSpec);
    }

    @Override
    public RemoveSecretCmd removeSecretCmd(String secretId) {
        return new RemoveSecretCmdImpl(getDockerCmdExecFactory().createRemoveSecretCmdExec(), secretId);
    }

    @Override
    public ListTasksCmd listTasksCmd() {
        return new ListTasksCmdImpl(getDockerCmdExecFactory().listTasksCmdExec());
    }

    @Override
    public void close() throws IOException {
        getDockerCmdExecFactory().close();
    }

}
