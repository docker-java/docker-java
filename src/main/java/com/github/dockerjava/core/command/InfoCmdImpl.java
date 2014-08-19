package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.model.Info;

/**
 * Return Docker server info
 */
public class InfoCmdImpl extends AbstrDockerCmd<InfoCmd, Info> implements InfoCmd  {

	public InfoCmdImpl(DockerCmdExec<InfoCmd, Info> exec) {
		super(exec);
	}
	
	@Override
    public String toString() {
        return "info";
    }
    
//	protected Info impl() throws DockerException {
//		WebTarget webResource = baseResource.path("/info");
//
//		LOGGER.trace("GET: {}", webResource);
//		return webResource.request().accept(MediaType.APPLICATION_JSON).get(Info.class);
//	}
}
