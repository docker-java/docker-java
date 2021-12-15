package com.github.dockerjava.api.command;

public interface DockerCmdSyncExec<CMD_T extends DockerCmd<RES_T>, RES_T> {

    RES_T exec(CMD_T command);

}
