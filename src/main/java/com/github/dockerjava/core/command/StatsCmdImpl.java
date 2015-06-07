package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutorService;

import com.github.dockerjava.api.command.EventCallback;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.command.StatsCallback;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.command.TopContainerCmd;

/**
 * Stream docker stats
 */
public class StatsCmdImpl extends AbstrDockerCmd<StatsCmd, ExecutorService> implements StatsCmd {

    private String containerId;
    private StatsCallback statsCallback;

    public StatsCmdImpl(StatsCmd.Exec exec, StatsCallback statsCallback) {
        super(exec);
        withStatsCallback(statsCallback);
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
    public StatsCmd withStatsCallback(StatsCallback statsCallback) {
        this.statsCallback = statsCallback;
        return this;
    }

  
    @Override
    public StatsCallback getStatsCallback() {
        return statsCallback;
    }

    @Override
    public ExecutorService exec() {
        return super.exec();
    }

    @Override
    public String toString() {
        return new StringBuilder("stats")
                .append(containerId != null ? " --id=" + containerId : "")
                .toString();
    }
}
