package com.github.dockerjava.api.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.EventStreamItem;

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

    public String getPathToDockerfile();

	public AuthConfigurations getBuildAuthConfigs();

    public BuildImageCmd withBaseDirectory(File baseDirectory);

    public BuildImageCmd withDockerfile(File dockerfile);
	
	public BuildImageCmd withTarInputStream(InputStream tarInputStream);

	public BuildImageCmd withNoCache();

	public BuildImageCmd withNoCache(boolean noCache);
	
	public BuildImageCmd withRemove();

	public BuildImageCmd withRemove(boolean rm);
	
	public BuildImageCmd withQuiet();

	public BuildImageCmd withQuiet(boolean quiet);
	
  	public BuildImageCmd withBuildAuthConfigs(AuthConfigurations authConfig);
	
	public static interface Exec extends DockerCmdExec<BuildImageCmd, BuildImageCmd.Response> {
	}

  public static abstract class Response extends InputStream {
    public abstract Iterable<EventStreamItem> getItems() throws IOException;
  }

}