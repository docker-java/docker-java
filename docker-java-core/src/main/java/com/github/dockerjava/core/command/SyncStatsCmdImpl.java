package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.SyncStatsCmd;
import com.github.dockerjava.api.model.Statistics;

/**
 * Container stats
 */
public class SyncStatsCmdImpl extends AbstrDockerCmd<SyncStatsCmd, Statistics> implements SyncStatsCmd {

    private String containerId;

    public SyncStatsCmdImpl(SyncStatsCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public SyncStatsCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public String getContainerId() {
        return containerId;
    }
}
