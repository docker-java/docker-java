package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.google.common.base.Preconditions;

/**
 * Inspect the details of an image.
 */
public class InspectImageCmdImpl extends AbstrDockerCmd<InspectImageCmd, InspectImageResponse> implements InspectImageCmd  {

	private String imageId;

	public InspectImageCmdImpl(DockerCmdExec<InspectImageCmd, InspectImageResponse> exec,String imageId) {
		super(exec);
		withImageId(imageId);
	}

    @Override
	public String getImageId() {
        return imageId;
    }

    @Override
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

//	protected InspectImageResponse impl() {
//		WebTarget webResource = baseResource.path("/images/{id}/json").resolveTemplate("id", imageId);
//
//		LOGGER.trace("GET: {}", webResource);
//		return webResource.request().accept(MediaType.APPLICATION_JSON).get(InspectImageResponse.class);
//	}
}
