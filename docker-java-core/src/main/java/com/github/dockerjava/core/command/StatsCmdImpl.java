package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.Statistics;

/**
 * Container stats
 */
public class StatsCmdImpl extends AbstrAsyncDockerCmd<StatsCmd, Statistics> implements StatsCmd {

    private String containerId;

    private Boolean noStream;

    public StatsCmdImpl(StatsCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
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
    public Boolean hasNoStream() {
        return noStream;
    }

    @Override
    public StatsCmd withNoStream(boolean noStream) {
        this.noStream = noStream;
        return this;
    }
}
