package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.core.util.DockerImageName;

/**
 * Inspect the details of an image.
 */
public interface InspectImageCmd extends DockerCmd<InspectImageResponse>{

	public DockerImageName getImageName();

	public InspectImageCmd withImageName(DockerImageName imageName);

	/**
	 * @throws NotFoundException No such image
	 */
	@Override
	public InspectImageResponse exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<InspectImageCmd, InspectImageResponse> {
	}

}