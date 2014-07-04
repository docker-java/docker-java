package com.github.dockerjava.client.command;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


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

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("repo", repository);
		params.add("tag", tag);
		params.add("force", force ? "1" : "0");

		WebResource webResource = baseResource.path("/images/" + imageId + "/tag").queryParams(
				params);

		try {
			LOGGER.trace("POST: {}", webResource);
			ClientResponse resp = webResource.post(ClientResponse.class);
			return resp.getStatus();
		} catch (UniformInterfaceException exception) {
			throw new DockerException(exception);
		}
	}
}
