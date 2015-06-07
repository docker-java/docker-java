package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.core.util.DockerImageName;


/**
 * Inspect the details of an image.
 */
public class InspectImageCmdImpl extends AbstrDockerCmd<InspectImageCmd, InspectImageResponse> implements InspectImageCmd  {

	private DockerImageName imageName;

	public InspectImageCmdImpl(InspectImageCmd.Exec exec, DockerImageName imageName) {
		super(exec);
		withImageName(imageName);
	}

    @Override
	public DockerImageName getImageName() {
        return imageName;
    }

    @Override
	public InspectImageCmd withImageName(DockerImageName imageName) {
		checkNotNull(imageName, "imageName was not specified");
		this.imageName = imageName;
		return this;
	}

    @Override
    public String toString() {
        return "inspect " + imageName;
    }
    
    /**
     * @throws NotFoundException No such image
     */
	@Override
    public InspectImageResponse exec() throws NotFoundException {
    	return super.exec();
    }
}
