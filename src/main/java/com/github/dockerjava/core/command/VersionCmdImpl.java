package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.model.Version;


/**
 * Returns the Docker version info.
 */
public class VersionCmdImpl extends AbstrDockerCmd<VersionCmd, Version> implements VersionCmd  {

	 @Override
    public String toString() {
        return "version";
    }   
    
    public VersionCmdImpl(DockerCmdExec<VersionCmd, Version> exec) {
    	super(exec);
	}

//	protected Version impl() throws DockerException {
//		WebTarget webResource = baseResource.path("/version");
//		
//		LOGGER.trace("GET: {}", webResource);
//		return webResource.request().accept(MediaType.APPLICATION_JSON).get(Version.class);
//	}
}
