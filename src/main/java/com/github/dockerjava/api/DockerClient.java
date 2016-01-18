package com.github.dockerjava.api;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

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
import com.github.dockerjava.api.command.CreateVolumeCmd;
import com.github.dockerjava.api.command.DisconnectFromNetworkCmd;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectExecCmd;
import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectNetworkCmd;
import com.github.dockerjava.api.command.InspectVolumeCmd;
import com.github.dockerjava.api.command.KillContainerCmd;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.ListNetworksCmd;
import com.github.dockerjava.api.command.ListVolumesCmd;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.api.command.RemoveNetworkCmd;
import com.github.dockerjava.api.command.RemoveVolumeCmd;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.command.SaveImageCmd;
import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Identifier;
import com.github.dockerjava.core.RemoteApiVersion;

// https://godoc.org/github.com/fsouza/go-dockerclient
public interface DockerClient extends Closeable {

    public AuthConfig authConfig() throws DockerException;

    /**
     * Authenticate with the server, useful for checking authentication.
     */
    public AuthCmd authCmd();

    public InfoCmd infoCmd();

    public PingCmd pingCmd();

    public VersionCmd versionCmd();

    /**
     * * IMAGE API *
     */

    public PullImageCmd pullImageCmd(@Nonnull String repository);

    public PushImageCmd pushImageCmd(@Nonnull String name);

    public PushImageCmd pushImageCmd(@Nonnull Identifier identifier);

    public CreateImageCmd createImageCmd(@Nonnull String repository, @Nonnull InputStream imageStream);

    public SearchImagesCmd searchImagesCmd(@Nonnull String term);

    public RemoveImageCmd removeImageCmd(@Nonnull String imageId);

    public ListImagesCmd listImagesCmd();

    public InspectImageCmd inspectImageCmd(@Nonnull String imageId);

    public SaveImageCmd saveImageCmd(@Nonnull String name);

    /**
     * * CONTAINER API *
     */

    public ListContainersCmd listContainersCmd();

    public CreateContainerCmd createContainerCmd(@Nonnull String image);

    /**
     * Creates a new {@link StartContainerCmd} for the container with the given ID. The command can then be further
     * customized by using builder methods on it like {@link StartContainerCmd#withDns(String...)}.
     * <p>
     * <b>If you customize the command, any existing configuration of the target container will get reset to its default
     * before applying the new configuration. To preserve the existing configuration, use an unconfigured
     * {@link StartContainerCmd}.</b>
     * <p>
     * This command corresponds to the <code>/containers/{id}/start</code> endpoint of the Docker Remote API.
     */
    public StartContainerCmd startContainerCmd(@Nonnull String containerId);

    public ExecCreateCmd execCreateCmd(@Nonnull String containerId);

    public InspectContainerCmd inspectContainerCmd(@Nonnull String containerId);

    public RemoveContainerCmd removeContainerCmd(@Nonnull String containerId);

    public WaitContainerCmd waitContainerCmd(@Nonnull String containerId);

    public AttachContainerCmd attachContainerCmd(@Nonnull String containerId);

    public ExecStartCmd execStartCmd(@Nonnull String containerId);

    public InspectExecCmd inspectExecCmd(@Nonnull String execId);

    public LogContainerCmd logContainerCmd(@Nonnull String containerId);

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
    public CopyArchiveFromContainerCmd copyArchiveFromContainerCmd(@Nonnull String containerId, @Nonnull String resource);

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
     */
    @Deprecated
    public CopyFileFromContainerCmd copyFileFromContainerCmd(@Nonnull String containerId, @Nonnull String resource);

    /**
     * Copy archive from local machine to remote container
     *
     * @param containerId
     *            id of the container
     * @return created command
     * @since {@link RemoteApiVersion#VERSION_1_20}
     */
    public CopyArchiveToContainerCmd copyArchiveToContainerCmd(@Nonnull String containerId);

    public ContainerDiffCmd containerDiffCmd(@Nonnull String containerId);

    public StopContainerCmd stopContainerCmd(@Nonnull String containerId);

    public KillContainerCmd killContainerCmd(@Nonnull String containerId);

    public RestartContainerCmd restartContainerCmd(@Nonnull String containerId);

    public CommitCmd commitCmd(@Nonnull String containerId);

    public BuildImageCmd buildImageCmd();

    public BuildImageCmd buildImageCmd(File dockerFileOrFolder);

    public BuildImageCmd buildImageCmd(InputStream tarInputStream);

    public TopContainerCmd topContainerCmd(String containerId);

    public TagImageCmd tagImageCmd(String imageId, String repository, String tag);

    public PauseContainerCmd pauseContainerCmd(String containerId);

    public UnpauseContainerCmd unpauseContainerCmd(String containerId);

    public EventsCmd eventsCmd();

    public StatsCmd statsCmd(String containerId);

    public CreateVolumeCmd createVolumeCmd();

    public InspectVolumeCmd inspectVolumeCmd(String name);

    public RemoveVolumeCmd removeVolumeCmd(String name);

    public ListVolumesCmd listVolumesCmd();

    public ListNetworksCmd listNetworksCmd();

    public InspectNetworkCmd inspectNetworkCmd();

    public CreateNetworkCmd createNetworkCmd();

    public RemoveNetworkCmd removeNetworkCmd(@Nonnull String networkId);

    public ConnectToNetworkCmd connectToNetworkCmd();

    public DisconnectFromNetworkCmd disconnectFromNetworkCmd();

    @Override
    public void close() throws IOException;

}
