package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

/**
 * Inspect the details of an image.
 */
public interface InspectImageCmd extends DockerCmd<InspectImageResponse>{

	public String getImageId();

	public InspectImageCmd withImageId(String imageId);

	/**
	 * @throws NotFoundException No such image
	 */
	@Override
	public InspectImageResponse exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<InspectImageCmd, InspectImageResponse> {
	}

}