package com.github.dockerjava.core;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.command.*;

import javax.annotation.Nonnull;
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

    private DockerCmdExecFactory dockerCmdExecFactory;

    private DockerClientImpl() {
        this(DefaultDockerClientConfig.createDefaultConfigBuilder().build());
    }

    private DockerClientImpl(String serverUrl) {
        this(configWithServerUrl(serverUrl));
    }

    private DockerClientImpl(DockerClientConfig dockerClientConfig) {
        checkNotNull(dockerClientConfig, "config was not specified");
        this.dockerClientConfig = dockerClientConfig;
    }

    private static DockerClientConfig configWithServerUrl(String serverUrl) {
        return DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(serverUrl).build();
    }

    public static DockerClientImpl getInstance() {
        return new DockerClientImpl();
    }

    public static DockerClientImpl getInstance(DockerClientConfig dockerClientConfig) {
        return new DockerClientImpl(dockerClientConfig);
    }

    public static DockerClientImpl getInstance(String serverUrl) {
        return new DockerClientImpl(serverUrl);
    }

    public DockerClientImpl withDockerCmdExecFactory(DockerCmdExecFactory dockerCmdExecFactory) {
        checkNotNull(dockerCmdExecFactory, "dockerCmdExecFactory was not specified");
        this.dockerCmdExecFactory = dockerCmdExecFactory;
        this.dockerCmdExecFactory.init(dockerClientConfig);
        return this;
    }

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
    public PruneCmd pruneContainersCmd(PruneType pruneType) {
        return new PruneCmdImpl(getDockerCmdExecFactory().pruneCmdExec(), pruneType);
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
