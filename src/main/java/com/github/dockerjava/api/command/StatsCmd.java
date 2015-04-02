package com.github.dockerjava.api.command;

import java.util.concurrent.ExecutorService;


/**
 * Get stats
 *
 */
public interface StatsCmd extends DockerCmd<ExecutorService> {
    public StatsCmd withContainerId(String containerId);
    
    public String getContainerId();
    
    public StatsCmd withStatsCallback(StatsCallback statsCallback);
    
    public StatsCallback getStatsCallback();

    public static interface Exec extends DockerCmdExec<StatsCmd, ExecutorService> {
    }

}
