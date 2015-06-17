package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.StatsCmd;

/**
 * Stream docker stats
 */
public class StatsCmdImpl extends AbstrDockerCmd<StatsCmd, Void> implements StatsCmd {

    private String containerId;

    private StatisticsCallback statisticsCallback;

    public StatsCmdImpl(StatsCmd.Exec exec, StatisticsCallback statisticsCallback) {
        super(exec);
        withStatisticsCallback(statisticsCallback);
    }

    @Override
    public StatsCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public StatsCmd withStatisticsCallback(StatisticsCallback statisticsCallback) {
        this.statisticsCallback = statisticsCallback;
        return this;
    }

    @Override
    public StatisticsCallback getStatisticsCallback() {
        return statisticsCallback;
    }

    @Override
    public Void exec() {
        return super.exec();
    }

    @Override
    public String toString() {
        return new StringBuilder("stats").append(containerId != null ? " --id=" + containerId : "").toString();
    }
}
