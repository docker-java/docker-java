package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Info;

public interface InfoCmd extends DockerCmd<Info> {

    public static interface Exec extends DockerCmdExec<InfoCmd, Info> {
    }

}