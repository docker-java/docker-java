package com.github.dockerjava.api.command;


import com.github.dockerjava.core.util.DockerImageName;

/**
 * Tag an image into a repository
 * 
 */
public interface TagImageCmd extends DockerCmd<Void> {

	public DockerImageName getNewImageName();

	public DockerImageName getOriginalImageName();

	public boolean hasForceEnabled();

	public TagImageCmd withNewImageName(DockerImageName newImageName);

	public TagImageCmd withOriginalImageName(DockerImageName originalImageName);

	public TagImageCmd withForce();

	public TagImageCmd withForce(boolean force);
	
	public static interface Exec extends DockerCmdExec<TagImageCmd, Void> {
	}

}