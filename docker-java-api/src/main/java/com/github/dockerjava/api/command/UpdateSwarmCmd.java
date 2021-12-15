package com.github.dockerjava.api.command;


import com.github.dockerjava.api.model.SwarmSpec;

import javax.annotation.CheckForNull;

public interface UpdateSwarmCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    Long getVersion();

    UpdateSwarmCmd withVersion(Long version);

    @CheckForNull
    Boolean getRotateWorkerToken();

    UpdateSwarmCmd withRotateWorkerToken(Boolean rotateWorkerToken);

    @CheckForNull
    Boolean getRotateManagerToken();

    UpdateSwarmCmd withRotateManagerToken(Boolean rotateManagerToken);

    @CheckForNull
    SwarmSpec getSwarmSpec();

    UpdateSwarmCmd withSwarmSpec(SwarmSpec swarmSpec);

    @Override
    Void exec();

    interface Exec extends DockerCmdSyncExec<UpdateSwarmCmd, Void> {
    }
}
