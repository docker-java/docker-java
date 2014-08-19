package com.github.dockerjava.api.command;

import java.io.InputStream;

/**
*
* Pull image from repository.
*
*/
public interface PullImageCmd extends DockerCmd<InputStream>{

	public String getRepository();

	public String getTag();

	public String getRegistry();

	public PullImageCmd withRepository(String repository);

	public PullImageCmd withTag(String tag);

	public PullImageCmd withRegistry(String registry);
	
	public static interface Exec extends DockerCmdExec<PullImageCmd, InputStream> {
	}

}