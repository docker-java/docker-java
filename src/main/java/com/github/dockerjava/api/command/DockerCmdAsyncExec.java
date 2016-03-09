package com.github.dockerjava.api.command;

import com.github.dockerjava.api.async.ResultCallback;

public interface DockerCmdAsyncExec<CMD_T extends DockerCmd<Void>, A_RES_T> {

    Void exec(CMD_T command, ResultCallback<A_RES_T> resultCallback);

}
