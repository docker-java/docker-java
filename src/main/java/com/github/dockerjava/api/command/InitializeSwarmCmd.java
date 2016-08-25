package com.github.dockerjava.api.command;


import com.github.dockerjava.api.model.SwarmSpec;

import javax.annotation.CheckForNull;

public interface InitializeSwarmCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getListenAddr();

    InitializeSwarmCmd withListenAddr(String listenAddr);

    @CheckForNull
    String getAdvertiseAddr();

    InitializeSwarmCmd withAdvertiseAddr(String advertiseAddr);

    @CheckForNull
    Boolean isForceNewCluster();

    InitializeSwarmCmd withForceNewCluster(Boolean forceNewCluster);

    @CheckForNull
    SwarmSpec getSwarmSpec();

    InitializeSwarmCmd withSwarmSpec(SwarmSpec swarmSpec);

    @Override
    Void exec();

    interface Exec extends DockerCmdSyncExec<InitializeSwarmCmd, Void> {

    }
}
