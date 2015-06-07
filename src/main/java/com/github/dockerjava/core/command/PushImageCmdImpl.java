package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.core.util.DockerImageName;

/**
 * Push the latest image to the repository.
 *
 * param imageName The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
 */
public class PushImageCmdImpl extends AbstrAuthCfgDockerCmd<PushImageCmd, PushImageCmd.Response> implements PushImageCmd  {

    private DockerImageName imageName;

    public PushImageCmdImpl(PushImageCmd.Exec exec, DockerImageName imageName) {
    	super(exec);
    	withImageName(imageName);
    }

    @Override
	public DockerImageName getImageName() {
        return imageName;
    }

    /**
	 * @param imageName The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
	 */
	@Override
	public PushImageCmd withImageName(DockerImageName imageName) {
		checkNotNull(imageName, "imageName was not specified");
		this.imageName = imageName;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("push ")
            .append(imageName.toString())
            .toString();
    }
   
    /**
     * @throws NotFoundException No such image
     */
    @Override
    public Response exec() throws NotFoundException {
    	return super.exec();
    }
}
