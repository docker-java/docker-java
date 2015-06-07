package com.github.dockerjava.api.command;

import com.github.dockerjava.core.util.DockerImageName;

import java.io.InputStream;

public interface CreateImageCmd extends DockerCmd<CreateImageResponse> {

	public DockerImageName getImageName();
	
	public InputStream getImageStream();

	/**
	 * @param imageName        the repository to import to
	 */
	public CreateImageCmd withImageName(DockerImageName imageName);

	/**
	 * @param imageStream       the InputStream of the tar file
	 */
	public CreateImageCmd withImageStream(InputStream imageStream);
	
	public static interface Exec extends DockerCmdExec<CreateImageCmd, CreateImageResponse> {
	}


}