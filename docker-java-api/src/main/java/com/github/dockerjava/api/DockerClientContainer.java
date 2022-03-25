package com.github.dockerjava.api;
import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.api.command.ContainerDiffCmd;
import com.github.dockerjava.api.command.CopyArchiveFromContainerCmd;
import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectExecCmd;
import com.github.dockerjava.api.command.KillContainerCmd;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RenameContainerCmd;
import com.github.dockerjava.api.command.ResizeContainerCmd;
import com.github.dockerjava.api.command.ResizeExecCmd;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.UpdateContainerCmd;
import com.github.dockerjava.api.command.WaitContainerCmd;

import javax.annotation.Nonnull;

public interface DockerClientContainer {
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

    ResizeExecCmd resizeExecCmd(@Nonnull String execId);

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
     * @param containerId id of the container
     * @param resource    path to container's resource
     * @return created command
     * @since {@link RemoteApiVersion#VERSION_1_20}
     */
    CopyArchiveFromContainerCmd copyArchiveFromContainerCmd(@Nonnull String containerId, @Nonnull String resource);

    /**
     * Copy resource from container to local machine.
     *
     * @param containerId id of the container
     * @param resource    path to container's resource
     * @return created command
     * @see #copyArchiveFromContainerCmd(String, String)
     * @deprecated since docker API version 1.20, replaced by {@link #copyArchiveFromContainerCmd(String, String)}
     * since 1.24 fails.
     */
    @Deprecated
    CopyFileFromContainerCmd copyFileFromContainerCmd(@Nonnull String containerId, @Nonnull String resource);

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

    ResizeContainerCmd resizeContainerCmd(@Nonnull String containerId);

    CommitCmd commitCmd(@Nonnull String containerId);
}
