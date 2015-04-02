package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Statistics;

/**
 * Stats callback
 */
public interface StatsCallback {
    public void onStats(Statistics stats);
    public void onException(Throwable throwable);
    public void onCompletion(int numStats);
    public boolean isReceiving();
}
