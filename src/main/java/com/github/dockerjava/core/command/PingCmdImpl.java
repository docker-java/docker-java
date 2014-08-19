package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.PingCmd;

/**
 * Ping the Docker server
 * 
 */
public class PingCmdImpl extends AbstrDockerCmd<PingCmd, Void> implements PingCmd {

	public PingCmdImpl(DockerCmdExec<PingCmd, Void> exec) {
		super(exec);
	}
	
//	protected Void impl() {
//		WebTarget webResource = baseResource.path("/_ping");
//       
//        LOGGER.trace("GET: {}", webResource);
//        webResource.request().get(Response.class);
//        
//        return null;
//	}
}
