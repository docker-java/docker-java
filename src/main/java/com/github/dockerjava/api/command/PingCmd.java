package com.github.dockerjava.api.command;

/**
 * Ping the Docker server
 *
 */
public interface PingCmd extends SyncDockerCmd<Void> {

    interface Exec extends DockerCmdSyncExec<PingCmd, Void> {
    }

}
