package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.TagImageCmd;


/**
 * Tag an image into a repository
 *
 * @param image			The local image to tag (either a name or an id)
 * @param repository 	The repository to tag in
 * @param force         (not documented)
 * 
 */
public class TagImageCmdImpl extends AbstrDockerCmd<TagImageCmd, Void> implements TagImageCmd  {

	private String imageId, repository, tag;

	private boolean force;

	public TagImageCmdImpl(TagImageCmd.Exec exec, String imageId, String repository, String tag) {
		super(exec);
		withImageId(imageId);
		withRepository(repository);
		withTag(tag);
	}

    @Override
	public String getImageId() {
        return imageId;
    }

    @Override
	public String getRepository() {
        return repository;
    }

    @Override
	public String getTag() {
        return tag;
    }

    @Override
	public boolean hasForceEnabled() {
        return force;
    }

    @Override
	public TagImageCmd withImageId(String imageId) {
		checkNotNull(imageId, "imageId was not specified");
		this.imageId = imageId;
		return this;
	}

	@Override
	public TagImageCmd withRepository(String repository) {
		checkNotNull(repository, "repository was not specified");
		this.repository = repository;
		return this;
	}

	@Override
	public TagImageCmd withTag(String tag) {
		checkNotNull(tag, "tag was not specified");
		this.tag = tag;
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
            .append(repository != null ? repository + "/" : "")
            .append(imageId)
            .append(tag != null ? ":" + tag : "")
            .toString();
    }
}
