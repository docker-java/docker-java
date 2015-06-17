package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.Statistics;

/**
 * Container stats
 */
public class StatsCmdImpl extends AbstrDockerCmd<StatsCmd, Void> implements StatsCmd {

    private String containerId;

    private ResultCallback<Statistics> resultCallback;

    public StatsCmdImpl(StatsCmd.Exec exec, ResultCallback<Statistics> resultCallback) {
        super(exec);
        withResultCallback(resultCallback);
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
    public StatsCmd withResultCallback(ResultCallback<Statistics> resultCallback) {
        this.resultCallback = resultCallback;
        return this;
    }

    @Override
    public ResultCallback<Statistics> getResultCallback() {
        return resultCallback;
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
