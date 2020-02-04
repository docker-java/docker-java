package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.model.Statistics;

/**
 * Get container stats. The result of {@link Statistics} is handled asynchronously because the docker remote API will block when a container
 * is stopped until the container is up again.
 */
public interface StatsCmd extends AsyncDockerCmd<StatsCmd, Statistics> {

    @CheckForNull
    String getContainerId();

    StatsCmd withContainerId(@Nonnull String containerId);

    @CheckForNull
    Boolean hasNoStream();

    StatsCmd withNoStream(boolean noStream);

    interface Exec extends DockerCmdAsyncExec<StatsCmd, Statistics> {
    }
}
