package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.entity;


/**
 * Tag an image into a repository
 *
 * @param image
 *            the local image to tag (either a name or an id)
 * @param repository
 *            the repository to tag in
 * @param force
 *            (not documented)
 * @return the HTTP status code (201 for success)
 */
public class TagImageCmd extends AbstrDockerCmd<TagImageCmd, Integer>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(TagImageCmd.class);

	private String imageId, repository, tag;

	private boolean force;

	public TagImageCmd(String imageId, String repository, String tag) {
		withImageId(imageId);
		withRepository(repository);
		withTag(tag);
	}

    public String getImageId() {
        return imageId;
    }

    public String getRepository() {
        return repository;
    }

    public String getTag() {
        return tag;
    }

    public boolean hasForceEnabled() {
        return force;
    }

    public TagImageCmd withImageId(String imageId) {
		Preconditions.checkNotNull(imageId, "imageId was not specified");
		this.imageId = imageId;
		return this;
	}

	public TagImageCmd withRepository(String repository) {
		Preconditions.checkNotNull(repository, "repository was not specified");
		this.repository = repository;
		return this;
	}

	public TagImageCmd withTag(String tag) {
		Preconditions.checkNotNull(tag, "tag was not specified");
		this.tag = tag;
		return this;
	}

	public TagImageCmd withForce() {
		return withForce(true);
	}

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

	protected Integer impl() {
		WebTarget webResource = baseResource.path("/images/" + imageId + "/tag")
                .queryParam("repo", repository)
                .queryParam("tag", tag)
                .queryParam("force", force ? "1" : "0");

		try {
			LOGGER.trace("POST: {}", webResource);
			Response resp = webResource.request().post(entity(null, MediaType.APPLICATION_JSON), Response.class);
			return resp.getStatus();
		} catch (ClientErrorException exception) {
			throw new DockerException(exception);
		}
	}
}
