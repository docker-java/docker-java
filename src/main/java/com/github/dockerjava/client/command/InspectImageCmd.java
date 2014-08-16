package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;


/**
 * Inspect the details of an image.
 */
public class InspectImageCmd extends AbstrDockerCmd<InspectImageCmd, InspectImageResponse>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(InspectImageCmd.class);

	private String imageId;

	public InspectImageCmd(String imageId) {
		withImageId(imageId);
	}

    public String getImageId() {
        return imageId;
    }

    public InspectImageCmd withImageId(String imageId) {
		Preconditions.checkNotNull(imageId, "imageId was not specified");
		this.imageId = imageId;
		return this;
	}

    @Override
    public String toString() {
        return "inspect " + imageId;
    }

	protected InspectImageResponse impl() {
		WebTarget webResource = baseResource.path("/images/{id}/json").resolveTemplate("id", imageId);

		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.request().accept(MediaType.APPLICATION_JSON).get(InspectImageResponse.class);
		} catch (ClientErrorException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such image %s", imageId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}
}
