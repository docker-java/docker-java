package com.github.dockerjava.client.command;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 *
 * Remove an image, deleting any tags it might have.
 *
 */
public class RemoveImageCmd extends AbstrDockerCmd<RemoveImageCmd, Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoveImageCmd.class);

	private String imageId;

	private boolean force, noPrune;

	public RemoveImageCmd(String imageId) {
		withImageId(imageId);
	}

    public String getImageId() {
        return imageId;
    }

    public boolean hasForceEnabled() {
        return force;
    }

    public boolean hasNoPruneEnabled() {
        return noPrune;
    }

    public RemoveImageCmd withImageId(String imageId) {
		Preconditions.checkNotNull(imageId, "imageId was not specified");
		this.imageId = imageId;
		return this;
	}

	public RemoveImageCmd withForce() {
		return withForce(true);
	}

	public RemoveImageCmd withForce(boolean force) {
		this.force = force;
		return this;
	}

	public RemoveImageCmd withNoPrune(boolean noPrune) {
		this.noPrune = noPrune;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("rmi ")
            .append(noPrune ? "--no-prune=true" : "")
            .append(force ? "--force=true" : "")
            .append(imageId)
            .toString();
    }
    
    /**
     * @throws NotFoundException No such image
     */
    @Override
    public Void exec() throws NotFoundException {
    	return super.exec();
    }

	protected Void impl() throws DockerException {
		Preconditions.checkState(!StringUtils.isEmpty(imageId), "Image ID can't be empty");
		
		WebResource webResource = baseResource.path("/images/" + imageId)
				.queryParam("force", force ? "1" : "0")
				.queryParam("noprune", noPrune ? "1" : "0");

		LOGGER.trace("DELETE: {}", webResource);
		webResource.delete(ClientResponse.class);

		return null;
	}
}
