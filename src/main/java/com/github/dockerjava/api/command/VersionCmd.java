package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Version;

/**
 * Returns the Docker version info.
 */
public interface VersionCmd extends DockerCmd<Version> {

    public static interface Exec extends DockerCmdExec<VersionCmd, Version> {
    }

}