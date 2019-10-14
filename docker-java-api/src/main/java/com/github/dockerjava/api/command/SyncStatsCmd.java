package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.model.Statistics;

/**
 * Get container stats.
 */
public interface SyncStatsCmd extends SyncDockerCmd<Statistics> {

    @CheckForNull
    String getContainerId();

    SyncStatsCmd withContainerId(@Nonnull String containerId);

    interface Exec extends DockerCmdSyncExec<SyncStatsCmd, Statistics> {
    }
}
