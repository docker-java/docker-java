package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.core.util.DockerImageName;

/**
 *
 * Remove an image, deleting any tags it might have.
 *
 */
public class RemoveImageCmdImpl extends AbstrDockerCmd<RemoveImageCmd, Void> implements RemoveImageCmd {

	private DockerImageName imageName;

	private boolean force, noPrune;

	public RemoveImageCmdImpl(RemoveImageCmd.Exec exec, DockerImageName imageName) {
		super(exec);
		withImageName(imageName);
	}

    @Override
	public DockerImageName getImageName() {
        return imageName;
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
	public RemoveImageCmd withImageName(DockerImageName imageName) {
		checkNotNull(imageName, "imageName was not specified");
		this.imageName = imageName;
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
	public RemoveImageCmd withNoPrune() {
		return withNoPrune(true);
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
            .append(imageName)
            .toString();
    }
    
    /**
     * @throws NotFoundException No such image
     */
	@Override
    public Void exec() throws NotFoundException {
    	return super.exec();
    }
}
