package com.github.dockerjava.api.command;

import com.github.dockerjava.api.async.StreamCallback;
import com.github.dockerjava.api.model.Statistics;

/**
 * Get stats
 *
 */
public interface StatsCmd extends DockerCmd<Void> {
    public StatsCmd withContainerId(String containerId);

    public String getContainerId();

    public StatsCmd withStatisticsCallback(StatisticsCallback callback);

    public StatisticsCallback getStatisticsCallback();

    public static interface Exec extends DockerCmdExec<StatsCmd, Void> {
    }

    /**
     * {@link Statistics} stream callback
     */
    public interface StatisticsCallback extends StreamCallback<Statistics> {
    }

}
