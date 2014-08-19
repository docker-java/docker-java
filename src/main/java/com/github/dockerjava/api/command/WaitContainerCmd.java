package com.github.dockerjava.api.command;

/**
 * Wait a container
 * 
 * Block until container stops, then returns its exit code
 */
public interface WaitContainerCmd extends DockerCmd<Integer> {

	public String getContainerId();

	public WaitContainerCmd withContainerId(String containerId);
	
	public static interface Exec extends DockerCmdExec<WaitContainerCmd, Integer> {
	}

}