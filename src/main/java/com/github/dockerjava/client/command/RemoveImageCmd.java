package com.github.dockerjava.client.command;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
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

	protected Void impl() throws DockerException {
		Preconditions.checkState(!StringUtils.isEmpty(imageId), "Image ID can't be empty");

		try {
			WebResource webResource = baseResource.path("/images/" + imageId)
					.queryParam("force", force ? "1" : "0").queryParam("noprune", noPrune ? "1" : "0");

			LOGGER.trace("DELETE: {}", webResource);
			webResource.delete(ClientResponse.class);


		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully removed image " + imageId);
			} else if (exception.getResponse().getStatus() == 404) {
				LOGGER.warn("{} no such image", imageId);
			} else if (exception.getResponse().getStatus() == 409) {
				throw new DockerException("Conflict");
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error.", exception);
			} else {
				throw new DockerException(exception);
			}
		}

		return null;
	}
}
