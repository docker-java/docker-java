package com.github.dockerjava.api.command;

import java.io.InputStream;

import com.github.dockerjava.api.NotFoundException;

/**
 * Push the latest image to the repository.
 *
 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
 */
public interface PushImageCmd extends DockerCmd<InputStream>{

	public String getName();

	/**
	 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
	 */
	public PushImageCmd withName(String name);

	/**
	 * @throws NotFoundException No such image
	 */
	public InputStream exec() throws NotFoundException;

}