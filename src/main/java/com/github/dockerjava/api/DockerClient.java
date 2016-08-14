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
import com.github.dockerjava.api.command.LoadImageCmd;
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
import com.github.dockerjava.api.command.UpdateContainerCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.api.command.RenameContainerCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Identifier;
import com.github.dockerjava.core.RemoteApiVersion;

// https://godoc.org/github.com/fsouza/go-dockerclient
public interface DockerClient extends Closeable {

    AuthConfig authConfig() throws DockerException;

    /**
     * Authenticate with the server, useful for checking authentication.
     */
    AuthCmd authCmd();

    InfoCmd infoCmd();

    PingCmd pingCmd();

    VersionCmd versionCmd();

    /**
     * * IMAGE API *
     */

    PullImageCmd pullImageCmd(@Nonnull String repository);

    PushImageCmd pushImageCmd(@Nonnull String name);

    PushImageCmd pushImageCmd(@Nonnull Identifier identifier);

    CreateImageCmd createImageCmd(@Nonnull String repository, @Nonnull InputStream imageStream);

    /**
     * Loads a tarball with a set of images and tags into a Docker repository.
     *
     * Corresponds to POST /images/load API endpoint.
     *
     * @param imageStream
     *            stream of the tarball file
     * @return created command
     * @since {@link RemoteApiVersion#VERSION_1_7}
     */
    LoadImageCmd loadImageCmd(@Nonnull InputStream imageStream);

    SearchImagesCmd searchImagesCmd(@Nonnull String term);

    RemoveImageCmd removeImageCmd(@Nonnull String imageId);

    ListImagesCmd listImagesCmd();

    InspectImageCmd inspectImageCmd(@Nonnull String imageId);

    SaveImageCmd saveImageCmd(@Nonnull String name);

    /**
     * * CONTAINER API *
     */

    ListContainersCmd listContainersCmd();

    CreateContainerCmd createContainerCmd(@Nonnull String image);

    /**
     * Creates a new {@link StartContainerCmd} for the container with the given ID. The command can then be further customized by using
     * builder methods on it like {@link StartContainerCmd#withDns(String...)}.
     * <p>
     * <b>If you customize the command, any existing configuration of the target container will get reset to its default before applying the
     * new configuration. To preserve the existing configuration, use an unconfigured {@link StartContainerCmd}.</b>
     * <p>
     * This command corresponds to the <code>/containers/{id}/start</code> endpoint of the Docker Remote API.
     */
    StartContainerCmd startContainerCmd(@Nonnull String containerId);

    ExecCreateCmd execCreateCmd(@Nonnull String containerId);

    InspectContainerCmd inspectContainerCmd(@Nonnull String containerId);

    RemoveContainerCmd removeContainerCmd(@Nonnull String containerId);

    WaitContainerCmd waitContainerCmd(@Nonnull String containerId);

    AttachContainerCmd attachContainerCmd(@Nonnull String containerId);

    ExecStartCmd execStartCmd(@Nonnull String execId);

    InspectExecCmd inspectExecCmd(@Nonnull String execId);

    LogContainerCmd logContainerCmd(@Nonnull String containerId);

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
    CopyArchiveFromContainerCmd copyArchiveFromContainerCmd(@Nonnull String containerId, @Nonnull String resource);

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
     * since 1.24 fails.
     */
    @Deprecated
    CopyFileFromContainerCmd copyFileFromContainerCmd(@Nonnull String containerId, @Nonnull String resource);

    /**
     * Copy archive from local machine to remote container
     *
     * @param containerId
     *            id of the container
     * @return created command
     * @since {@link RemoteApiVersion#VERSION_1_20}
     */
    CopyArchiveToContainerCmd copyArchiveToContainerCmd(@Nonnull String containerId);

    ContainerDiffCmd containerDiffCmd(@Nonnull String containerId);

    StopContainerCmd stopContainerCmd(@Nonnull String containerId);

    KillContainerCmd killContainerCmd(@Nonnull String containerId);

    /**
     * Update container settings
     *
     * @param containerId id of the container
     * @return command
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    UpdateContainerCmd updateContainerCmd(@Nonnull String containerId);

    /**
     * Rename container.
     *
     * @param containerId id of the container
     * @return command
     * @since {@link RemoteApiVersion#VERSION_1_17}
     */
    RenameContainerCmd renameContainerCmd(@Nonnull String containerId);

    RestartContainerCmd restartContainerCmd(@Nonnull String containerId);

    CommitCmd commitCmd(@Nonnull String containerId);

    BuildImageCmd buildImageCmd();

    BuildImageCmd buildImageCmd(File dockerFileOrFolder);

    BuildImageCmd buildImageCmd(InputStream tarInputStream);

    TopContainerCmd topContainerCmd(String containerId);

    TagImageCmd tagImageCmd(String imageId, String repository, String tag);

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

    @Override
    void close() throws IOException;

}
