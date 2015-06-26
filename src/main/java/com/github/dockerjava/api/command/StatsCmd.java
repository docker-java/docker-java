package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Statistics;

/**
 * Get container stats. The result of {@link Statistics} is handled asynchronously because the docker remote API will
 * block when a container is stopped until the container is up again.
 */
public interface StatsCmd extends AsyncDockerCmd<StatsCmd, Statistics, Void> {
    public StatsCmd withContainerId(String containerId);

    public String getContainerId();

    public static interface Exec extends DockerCmdExec<StatsCmd, Void> {
    }
}
