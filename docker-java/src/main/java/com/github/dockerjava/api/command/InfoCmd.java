package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Info;

public interface InfoCmd extends SyncDockerCmd<Info> {

    interface Exec extends DockerCmdSyncExec<InfoCmd, Info> {
    }

}
