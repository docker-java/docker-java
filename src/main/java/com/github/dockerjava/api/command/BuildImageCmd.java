package com.github.dockerjava.api.command;

import java.io.File;
import java.io.InputStream;

/**
 * 
 * Build an image from Dockerfile.
 * 
 * TODO: http://docs.docker.com/reference/builder/#dockerignore
 * 
 */
public interface BuildImageCmd extends DockerCmd<InputStream>{

	public BuildImageCmd withTag(String tag);

	public File getDockerFolder();

	public String getTag();

	public boolean hasNoCacheEnabled();

	public boolean hasRemoveEnabled();

	public boolean isQuiet();

	public BuildImageCmd withNoCache();

	public BuildImageCmd withNoCache(boolean noCache);

	public BuildImageCmd withRemove(boolean rm);

	public BuildImageCmd withQuiet(boolean quiet);

}