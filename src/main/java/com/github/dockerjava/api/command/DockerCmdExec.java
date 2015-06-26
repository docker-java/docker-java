package com.github.dockerjava.api.command;

public interface DockerCmdExec<CMD_T extends DockerCmd<RES_T>, RES_T> {

    public RES_T exec(CMD_T command);

}
