package com.github.dockerjava.api.command;

import java.io.IOException;
import java.io.InputStream;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PushEventStreamItem;

/**
 * Push the latest image to the repository.
 *
 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
 */
public interface PushImageCmd extends DockerCmd<PushImageCmd.Response>{

	public String getName();

	public String getTag();

	/**
	 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
	 */
	public PushImageCmd withName(String name);
	
    /**
     * @param tag The image's tag. Not null.
     */
    public PushImageCmd withTag(String tag);

    public AuthConfig getAuthConfig();
	
	public PushImageCmd withAuthConfig(AuthConfig authConfig);

	/**
	 * @throws NotFoundException No such image
	 */
	public Response exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<PushImageCmd, Response> {
	}

  	public static abstract class Response extends InputStream {
	  public abstract Iterable<PushEventStreamItem> getItems() throws IOException;
	}

}