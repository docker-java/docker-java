package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

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

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("repo", repository);
		params.add("tag", tag);
		params.add("force", force ? "1" : "0");

		WebResource webResource = baseResource
				.path("/images/" + imageId + "/tag")
				.queryParams(params);
		
		LOGGER.trace("POST: {}", webResource);
		webResource.post(ClientResponse.class);
		return null;
	}
}
