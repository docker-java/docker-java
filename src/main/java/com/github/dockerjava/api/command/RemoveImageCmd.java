package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.core.util.DockerImageName;

/**
*
* Remove an image, deleting any tags it might have.
*
*/
public interface RemoveImageCmd extends DockerCmd<Void>{

	public DockerImageName getImageName();

	public boolean hasForceEnabled();

	public boolean hasNoPruneEnabled();

	public RemoveImageCmd withImageName(DockerImageName imageName);
	
	/**
	 * force delete of an image, even if it's tagged in multiple repositories
	 */
	public RemoveImageCmd withForce();

	/**
	 * force parameter to force delete of an image, even if it's tagged in multiple repositories
	 */
	public RemoveImageCmd withForce(boolean force);

	/**
	 * prevent the deletion of parent images
	 */
	public RemoveImageCmd withNoPrune();
	
	/**
	 * noprune parameter to prevent the deletion of parent images
	 * 
	 */
	public RemoveImageCmd withNoPrune(boolean noPrune);

	/**
	 * @throws NotFoundException No such image
	 */
	@Override
	public Void exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<RemoveImageCmd, Void> {
	}

}