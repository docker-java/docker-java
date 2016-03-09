package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

/**
 * List processes running inside a container
 */
public interface TopContainerCmd extends SyncDockerCmd<TopContainerResponse> {

    @CheckForNull
    String getContainerId();

    @CheckForNull
    String getPsArgs();

    TopContainerCmd withContainerId(@Nonnull String containerId);

    TopContainerCmd withPsArgs(String psArgs);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    TopContainerResponse exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<TopContainerCmd, TopContainerResponse> {
    }

}
