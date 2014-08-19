package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.google.common.base.Preconditions;

/**
 *
 * Remove an image, deleting any tags it might have.
 *
 */
public class RemoveImageCmdImpl extends AbstrDockerCmd<RemoveImageCmd, Void> implements RemoveImageCmd {

	private String imageId;

	private boolean force, noPrune;

	public RemoveImageCmdImpl(DockerCmdExec<RemoveImageCmd, Void> exec, String imageId) {
		super(exec);
		withImageId(imageId);
	}

    @Override
	public String getImageId() {
        return imageId;
    }

    @Override
	public boolean hasForceEnabled() {
        return force;
    }

    @Override
	public boolean hasNoPruneEnabled() {
        return noPrune;
    }

    @Override
	public RemoveImageCmd withImageId(String imageId) {
		Preconditions.checkNotNull(imageId, "imageId was not specified");
		this.imageId = imageId;
		return this;
	}

	@Override
	public RemoveImageCmd withForce() {
		return withForce(true);
	}

	@Override
	public RemoveImageCmd withForce(boolean force) {
		this.force = force;
		return this;
	}

	@Override
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

//	protected Void impl() throws DockerException {
//		Preconditions.checkState(!StringUtils.isEmpty(imageId), "Image ID can't be empty");
//		
//		WebTarget webResource = baseResource.path("/images/" + imageId)
//				.queryParam("force", force ? "1" : "0")
//				.queryParam("noprune", noPrune ? "1" : "0");
//
//		LOGGER.trace("DELETE: {}", webResource);
//		webResource.request().delete(Response.class);
//		
//		return null;
//	}
}
