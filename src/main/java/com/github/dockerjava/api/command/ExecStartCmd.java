package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

import java.io.InputStream;

public interface ExecStartCmd extends DockerCmd<InputStream>{

    public String getContainerId();

    public boolean isDetach();

    public boolean isTty();

    /**
     * @throws com.github.dockerjava.api.NotFoundException No such container
     */
    @Override
    public InputStream exec() throws NotFoundException;

    public static interface Exec extends DockerCmdExec<ExecStartCmd, InputStream> {}
}
