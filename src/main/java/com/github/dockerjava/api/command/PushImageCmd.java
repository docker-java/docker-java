package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PushEventStreamItem;
import com.github.dockerjava.core.command.EventStreamReader;
import com.github.dockerjava.core.util.DockerImageName;

import java.io.IOException;
import java.io.InputStream;

/**
 * Push the latest image to the repository.
 *
 * param imageName The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
 */
public interface PushImageCmd extends DockerCmd<PushImageCmd.Response>{

	public DockerImageName getImageName();

	/**
	 * @param imageName The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
	 */
	public PushImageCmd withImageName(DockerImageName imageName);

    public AuthConfig getAuthConfig();
	
	public PushImageCmd withAuthConfig(AuthConfig authConfig);

	/**
	 * @throws NotFoundException No such image
	 */
	public Response exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<PushImageCmd, Response> {
	}

	/**
	 * @see {@link EventStreamReader}
	 */
  	public static abstract class Response extends InputStream {
	  public abstract Iterable<PushEventStreamItem> getItems() throws IOException;
	}

}