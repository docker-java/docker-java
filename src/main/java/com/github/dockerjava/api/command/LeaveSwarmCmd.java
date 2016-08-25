package com.github.dockerjava.api.command;


import javax.annotation.CheckForNull;

public interface LeaveSwarmCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    Boolean hasForceEnabled();

    LeaveSwarmCmd withForceEnabled(Boolean force);

    @Override
    Void exec();

    interface Exec extends DockerCmdSyncExec<LeaveSwarmCmd, Void> {
    }
}
