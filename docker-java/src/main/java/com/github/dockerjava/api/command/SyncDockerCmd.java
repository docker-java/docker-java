package com.github.dockerjava.api.command;

public interface SyncDockerCmd<RES_T> extends DockerCmd<RES_T> {

    RES_T exec();

}
