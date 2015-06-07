package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.core.util.DockerImageName;


/**
 * Tag an image into a repository
 * 
 */
public class TagImageCmdImpl extends AbstrDockerCmd<TagImageCmd, Void> implements TagImageCmd  {

	private DockerImageName originalImageName, newImageName;

	private boolean force;

	public TagImageCmdImpl(TagImageCmd.Exec exec, DockerImageName originalImageName, DockerImageName newImageName) {
		super(exec);
		withNewImageName(newImageName);
		withOriginalImageName(originalImageName);
	}

    @Override
	public DockerImageName getOriginalImageName() {
        return originalImageName;
    }

    @Override
	public DockerImageName getNewImageName() {
        return newImageName;
    }

    @Override
	public boolean hasForceEnabled() {
        return force;
    }

    @Override
	public TagImageCmd withNewImageName(DockerImageName imageName) {
		checkNotNull(imageName, "newImageName was not specified");
		this.newImageName = imageName;
		return this;
	}

	@Override
	public TagImageCmd withOriginalImageName(DockerImageName imageName) {
		checkNotNull(imageName, "originalImageName was not specified");
		this.originalImageName = imageName;
		return this;
	}

	@Override
	public TagImageCmd withForce() {
		return withForce(true);
	}

	@Override
	public TagImageCmd withForce(boolean force) {
		this.force = force;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("tag ")
            .append(force ? "--force=true " : "")
            .append(originalImageName)
            .append(" ")
            .append(newImageName)
            .toString();
    }
}
