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
import com.github.dockerjava.api.command.CreateConfigCmd;
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
import com.github.dockerjava.api.command.InspectConfigCmd;
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
import com.github.dockerjava.api.command.ListConfigsCmd;
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
import com.github.dockerjava.api.command.RemoveConfigCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.api.command.RemoveNetworkCmd;
import com.github.dockerjava.api.command.RemoveSecretCmd;
import com.github.dockerjava.api.command.RemoveServiceCmd;
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
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Identifier;
import com.github.dockerjava.api.model.PruneType;
import com.github.dockerjava.api.model.SecretSpec;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.SwarmSpec;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @apiNote implementations MUST override {{@link #getDockerClient()}}
 * @implNote We're not using an abstract class here because we want
 * Java compiler to force us to implement every {@link DockerClient}'s method,
 * especially when new methods are added
 */
@SuppressWarnings("unused")
public class DockerClientDelegate implements DockerClient {

    protected DockerClient getDockerClient() {
        throw new IllegalStateException("Implement me!");
    }

    @Override
    public AuthConfig authConfig() throws DockerException {
        return getDockerClient().authConfig();
    }

    @Override
    public AuthCmd authCmd() {
        return getDockerClient().authCmd();
    }

    @Override
    public InfoCmd infoCmd() {
        return getDockerClient().infoCmd();
    }

    @Override
    public PingCmd pingCmd() {
        return getDockerClient().pingCmd();
    }

    @Override
    public VersionCmd versionCmd() {
        return getDockerClient().versionCmd();
    }

    @Override
    public PullImageCmd pullImageCmd(@Nonnull String repository) {
        return getDockerClient().pullImageCmd(repository);
    }

    @Override
    public PushImageCmd pushImageCmd(@Nonnull String name) {
        return getDockerClient().pushImageCmd(name);
    }

    @Override
    public PushImageCmd pushImageCmd(@Nonnull Identifier identifier) {
        return getDockerClient().pushImageCmd(identifier);
    }

    @Override
    public CreateImageCmd createImageCmd(@Nonnull String repository, @Nonnull InputStream imageStream) {
        return getDockerClient().createImageCmd(repository, imageStream);
    }

    @Override
    public LoadImageCmd loadImageCmd(@Nonnull InputStream imageStream) {
        return getDockerClient().loadImageCmd(imageStream);
    }

    @Override
    public SearchImagesCmd searchImagesCmd(@Nonnull String term) {
        return getDockerClient().searchImagesCmd(term);
    }

    @Override
    public RemoveImageCmd removeImageCmd(@Nonnull String imageId) {
        return getDockerClient().removeImageCmd(imageId);
    }

    @Override
    public ListImagesCmd listImagesCmd() {
        return getDockerClient().listImagesCmd();
    }

    @Override
    public InspectImageCmd inspectImageCmd(@Nonnull String imageId) {
        return getDockerClient().inspectImageCmd(imageId);
    }

    @Override
    public SaveImageCmd saveImageCmd(@Nonnull String name) {
        return getDockerClient().saveImageCmd(name);
    }

    @Override
    public SaveImagesCmd saveImagesCmd() {
        return getDockerClient().saveImagesCmd();
    }

    @Override
    public ListContainersCmd listContainersCmd() {
        return getDockerClient().listContainersCmd();
    }

    @Override
    public CreateContainerCmd createContainerCmd(@Nonnull String image) {
        return getDockerClient().createContainerCmd(image);
    }

    @Override
    public StartContainerCmd startContainerCmd(@Nonnull String containerId) {
        return getDockerClient().startContainerCmd(containerId);
    }

    @Override
    public ExecCreateCmd execCreateCmd(@Nonnull String containerId) {
        return getDockerClient().execCreateCmd(containerId);
    }

    @Override
    public ResizeExecCmd resizeExecCmd(@Nonnull String execId) {
        return getDockerClient().resizeExecCmd(execId);
    }

    @Override
    public InspectContainerCmd inspectContainerCmd(@Nonnull String containerId) {
        return getDockerClient().inspectContainerCmd(containerId);
    }

    @Override
    public RemoveContainerCmd removeContainerCmd(@Nonnull String containerId) {
        return getDockerClient().removeContainerCmd(containerId);
    }

    @Override
    public WaitContainerCmd waitContainerCmd(@Nonnull String containerId) {
        return getDockerClient().waitContainerCmd(containerId);
    }

    @Override
    public AttachContainerCmd attachContainerCmd(@Nonnull String containerId) {
        return getDockerClient().attachContainerCmd(containerId);
    }

    @Override
    public ExecStartCmd execStartCmd(@Nonnull String execId) {
        return getDockerClient().execStartCmd(execId);
    }

    @Override
    public InspectExecCmd inspectExecCmd(@Nonnull String execId) {
        return getDockerClient().inspectExecCmd(execId);
    }

    @Override
    public LogContainerCmd logContainerCmd(@Nonnull String containerId) {
        return getDockerClient().logContainerCmd(containerId);
    }

    @Override
    public CopyArchiveFromContainerCmd copyArchiveFromContainerCmd(@Nonnull String containerId, @Nonnull String resource) {
        return getDockerClient().copyArchiveFromContainerCmd(containerId, resource);
    }

    @Override
    @Deprecated
    public CopyFileFromContainerCmd copyFileFromContainerCmd(@Nonnull String containerId, @Nonnull String resource) {
        return getDockerClient().copyFileFromContainerCmd(containerId, resource);
    }

    @Override
    public CopyArchiveToContainerCmd copyArchiveToContainerCmd(@Nonnull String containerId) {
        return getDockerClient().copyArchiveToContainerCmd(containerId);
    }

    @Override
    public ContainerDiffCmd containerDiffCmd(@Nonnull String containerId) {
        return getDockerClient().containerDiffCmd(containerId);
    }

    @Override
    public StopContainerCmd stopContainerCmd(@Nonnull String containerId) {
        return getDockerClient().stopContainerCmd(containerId);
    }

    @Override
    public KillContainerCmd killContainerCmd(@Nonnull String containerId) {
        return getDockerClient().killContainerCmd(containerId);
    }

    @Override
    public UpdateContainerCmd updateContainerCmd(@Nonnull String containerId) {
        return getDockerClient().updateContainerCmd(containerId);
    }

    @Override
    public RenameContainerCmd renameContainerCmd(@Nonnull String containerId) {
        return getDockerClient().renameContainerCmd(containerId);
    }

    @Override
    public RestartContainerCmd restartContainerCmd(@Nonnull String containerId) {
        return getDockerClient().restartContainerCmd(containerId);
    }

    @Override
    public ResizeContainerCmd resizeContainerCmd(@Nonnull String containerId) {
        return getDockerClient().resizeContainerCmd(containerId);
    }

    @Override
    public CommitCmd commitCmd(@Nonnull String containerId) {
        return getDockerClient().commitCmd(containerId);
    }

    @Override
    public BuildImageCmd buildImageCmd() {
        return getDockerClient().buildImageCmd();
    }

    @Override
    public BuildImageCmd buildImageCmd(File dockerFileOrFolder) {
        return getDockerClient().buildImageCmd(dockerFileOrFolder);
    }

    @Override
    public BuildImageCmd buildImageCmd(InputStream tarInputStream) {
        return getDockerClient().buildImageCmd(tarInputStream);
    }

    @Override
    public TopContainerCmd topContainerCmd(String containerId) {
        return getDockerClient().topContainerCmd(containerId);
    }

    @Override
    public TagImageCmd tagImageCmd(String imageId, String imageNameWithRepository, String tag) {
        return getDockerClient().tagImageCmd(imageId, imageNameWithRepository, tag);
    }

    @Override
    public PauseContainerCmd pauseContainerCmd(String containerId) {
        return getDockerClient().pauseContainerCmd(containerId);
    }

    @Override
    public UnpauseContainerCmd unpauseContainerCmd(String containerId) {
        return getDockerClient().unpauseContainerCmd(containerId);
    }

    @Override
    public EventsCmd eventsCmd() {
        return getDockerClient().eventsCmd();
    }

    @Override
    public StatsCmd statsCmd(String containerId) {
        return getDockerClient().statsCmd(containerId);
    }

    @Override
    public CreateVolumeCmd createVolumeCmd() {
        return getDockerClient().createVolumeCmd();
    }

    @Override
    public InspectVolumeCmd inspectVolumeCmd(String name) {
        return getDockerClient().inspectVolumeCmd(name);
    }

    @Override
    public RemoveVolumeCmd removeVolumeCmd(String name) {
        return getDockerClient().removeVolumeCmd(name);
    }

    @Override
    public ListVolumesCmd listVolumesCmd() {
        return getDockerClient().listVolumesCmd();
    }

    @Override
    public ListNetworksCmd listNetworksCmd() {
        return getDockerClient().listNetworksCmd();
    }

    @Override
    public InspectNetworkCmd inspectNetworkCmd() {
        return getDockerClient().inspectNetworkCmd();
    }

    @Override
    public CreateNetworkCmd createNetworkCmd() {
        return getDockerClient().createNetworkCmd();
    }

    @Override
    public RemoveNetworkCmd removeNetworkCmd(@Nonnull String networkId) {
        return getDockerClient().removeNetworkCmd(networkId);
    }

    @Override
    public ConnectToNetworkCmd connectToNetworkCmd() {
        return getDockerClient().connectToNetworkCmd();
    }

    @Override
    public DisconnectFromNetworkCmd disconnectFromNetworkCmd() {
        return getDockerClient().disconnectFromNetworkCmd();
    }

    @Override
    public InitializeSwarmCmd initializeSwarmCmd(SwarmSpec swarmSpec) {
        return getDockerClient().initializeSwarmCmd(swarmSpec);
    }

    @Override
    public InspectSwarmCmd inspectSwarmCmd() {
        return getDockerClient().inspectSwarmCmd();
    }

    @Override
    public JoinSwarmCmd joinSwarmCmd() {
        return getDockerClient().joinSwarmCmd();
    }

    @Override
    public LeaveSwarmCmd leaveSwarmCmd() {
        return getDockerClient().leaveSwarmCmd();
    }

    @Override
    public UpdateSwarmCmd updateSwarmCmd(SwarmSpec swarmSpec) {
        return getDockerClient().updateSwarmCmd(swarmSpec);
    }

    @Override
    public UpdateSwarmNodeCmd updateSwarmNodeCmd() {
        return getDockerClient().updateSwarmNodeCmd();
    }

    @Override
    public ListSwarmNodesCmd listSwarmNodesCmd() {
        return getDockerClient().listSwarmNodesCmd();
    }

    @Override
    public ListServicesCmd listServicesCmd() {
        return getDockerClient().listServicesCmd();
    }

    @Override
    public CreateServiceCmd createServiceCmd(ServiceSpec serviceSpec) {
        return getDockerClient().createServiceCmd(serviceSpec);
    }

    @Override
    public InspectServiceCmd inspectServiceCmd(String serviceId) {
        return getDockerClient().inspectServiceCmd(serviceId);
    }

    @Override
    public UpdateServiceCmd updateServiceCmd(String serviceId, ServiceSpec serviceSpec) {
        return getDockerClient().updateServiceCmd(serviceId, serviceSpec);
    }

    @Override
    public RemoveServiceCmd removeServiceCmd(String serviceId) {
        return getDockerClient().removeServiceCmd(serviceId);
    }

    @Override
    public ListTasksCmd listTasksCmd() {
        return getDockerClient().listTasksCmd();
    }

    @Override
    public LogSwarmObjectCmd logServiceCmd(String serviceId) {
        return getDockerClient().logServiceCmd(serviceId);
    }

    @Override
    public LogSwarmObjectCmd logTaskCmd(String taskId) {
        return getDockerClient().logTaskCmd(taskId);
    }

    @Override
    public PruneCmd pruneCmd(PruneType pruneType) {
        return getDockerClient().pruneCmd(pruneType);
    }

    @Override
    public ListSecretsCmd listSecretsCmd() {
        return getDockerClient().listSecretsCmd();
    }

    @Override
    public CreateSecretCmd createSecretCmd(SecretSpec secretSpec) {
        return getDockerClient().createSecretCmd(secretSpec);
    }

    @Override
    public RemoveSecretCmd removeSecretCmd(String secretId) {
        return getDockerClient().removeSecretCmd(secretId);
    }

    @Override
    public ListConfigsCmd listConfigsCmd() {
        return getDockerClient().listConfigsCmd();
    }

    @Override
    public CreateConfigCmd createConfigCmd() {
        return getDockerClient().createConfigCmd();
    }

    @Override
    public InspectConfigCmd inspectConfigCmd(String configId) {
        return getDockerClient().inspectConfigCmd(configId);
    }

    @Override
    public RemoveConfigCmd removeConfigCmd(String configId) {
        return getDockerClient().removeConfigCmd(configId);
    }

    @Override
    public void close() throws IOException {
        getDockerClient().close();
    }
}
