package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

/**
 * Wait a container
 * 
 * Block until container stops, then returns its exit code
 */
public interface WaitContainerCmd extends DockerCmd<Integer> {

    public String getContainerId();

    public WaitContainerCmd withContainerId(String containerId);

    /**
     * @throws NotFoundException
     *             container not found
     */
    @Override
    public Integer exec() throws NotFoundException;

    public static interface Exec extends DockerCmdExec<WaitContainerCmd, Integer> {
    }

}