package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;


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
    
    /**
     * @throws NotFoundException No such image
     */
    @Override
    public InspectImageResponse exec() throws NotFoundException {
    	return super.exec();
    }

	protected InspectImageResponse impl() {
		WebResource webResource = baseResource.path(String.format("/images/%s/json", imageId));

		LOGGER.trace("GET: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON).get(InspectImageResponse.class);
	}
}
