package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.EventStreamItem;
import com.github.dockerjava.api.model.PushEventStreamItem;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * Build an image from Dockerfile.
 * 
 * TODO: http://docs.docker.com/reference/builder/#dockerignore
 * 
 */
public interface BuildImageCmd extends DockerCmd<BuildImageCmd.Response>{

	public BuildImageCmd withTag(String tag);

	public InputStream getTarInputStream();

	public String getTag();

	public boolean hasNoCacheEnabled();

	public boolean hasRemoveEnabled();

	public boolean isQuiet();
	
	public BuildImageCmd withTarInputStream(InputStream tarInputStream);

	public BuildImageCmd withNoCache();

	public BuildImageCmd withNoCache(boolean noCache);
	
	public BuildImageCmd withRemove();

	public BuildImageCmd withRemove(boolean rm);
	
	public BuildImageCmd withQuiet();

	public BuildImageCmd withQuiet(boolean quiet);
	
	public static interface Exec extends DockerCmdExec<BuildImageCmd, BuildImageCmd.Response> {
	}

  public static abstract class Response extends InputStream {
    public abstract Iterable<EventStreamItem> getItems() throws IOException;
  }
}