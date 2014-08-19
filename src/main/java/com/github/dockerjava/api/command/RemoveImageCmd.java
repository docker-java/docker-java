package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

/**
*
* Remove an image, deleting any tags it might have.
*
*/
public interface RemoveImageCmd extends DockerCmd<Void>{

	public String getImageId();

	public boolean hasForceEnabled();

	public boolean hasNoPruneEnabled();

	public RemoveImageCmd withImageId(String imageId);

	public RemoveImageCmd withForce();

	public RemoveImageCmd withForce(boolean force);

	public RemoveImageCmd withNoPrune(boolean noPrune);

	/**
	 * @throws NotFoundException No such image
	 */
	public Void exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<RemoveImageCmd, Void> {
	}

}