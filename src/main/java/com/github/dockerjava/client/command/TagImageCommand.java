package com.github.dockerjava.client.command;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.TagImageCmd;
import com.google.common.base.Preconditions;

/**
 * Tag an image into a repository
 *
 * @param image			The local image to tag (either a name or an id)
 * @param repository 	The repository to tag in
 * @param force         (not documented)
 * 
 */
public class TagImageCommand extends AbstrDockerCmd<TagImageCommand, Void> implements TagImageCmd  {

	private static final Logger LOGGER = LoggerFactory.getLogger(TagImageCommand.class);

	private String imageId, repository, tag;

	private boolean force;

	public TagImageCommand(String imageId, String repository, String tag) {
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
		Preconditions.checkNotNull(imageId, "imageId was not specified");
		this.imageId = imageId;
		return this;
	}

	@Override
	public TagImageCmd withRepository(String repository) {
		Preconditions.checkNotNull(repository, "repository was not specified");
		this.repository = repository;
		return this;
	}

	@Override
	public TagImageCmd withTag(String tag) {
		Preconditions.checkNotNull(tag, "tag was not specified");
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


	protected Void impl() {
		WebTarget webResource = baseResource.path("/images/" + imageId + "/tag")
                .queryParam("repo", repository)
                .queryParam("tag", tag)
                .queryParam("force", force ? "1" : "0");

		LOGGER.trace("POST: {}", webResource);
		webResource.request().post(entity(null, MediaType.APPLICATION_JSON), Response.class);
		return null;
	}
}
