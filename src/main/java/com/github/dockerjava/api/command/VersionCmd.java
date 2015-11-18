package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Version;

/**
 * Returns the Docker version info.
 */
public interface VersionCmd extends SyncDockerCmd<Version> {

    public static interface Exec extends DockerCmdSyncExec<VersionCmd, Version> {
    }

}