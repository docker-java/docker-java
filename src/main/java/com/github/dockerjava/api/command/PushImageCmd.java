package com.github.dockerjava.api.command;

import java.io.InputStream;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;

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
	
	public AuthConfig getAuthConfig();
	
	public PushImageCmd withAuthConfig(AuthConfig authConfig);

	/**
	 * @throws NotFoundException No such image
	 */
	public InputStream exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<PushImageCmd, InputStream> {
	}

}