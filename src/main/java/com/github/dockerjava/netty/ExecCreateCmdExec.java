package com.github.dockerjava.netty;

import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.AbstrSyncDockerCmdExec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecCreateCmdExec extends AbstrSyncDockerCmdExec<ExecCreateCmd, ExecCreateCmdResponse>
		implements ExecCreateCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecCreateCmdExec.class);

	public ExecCreateCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
		super(baseResource, dockerClientConfig);
	}

	@Override
	protected ExecCreateCmdResponse execute(ExecCreateCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/exec").resolveTemplate("id",
				command.getContainerId());

		LOGGER.trace("POST: {}", webResource);

		return webResource.request().accept(MediaType.APPLICATION_JSON)
				.post(command, ExecCreateCmdResponse.class);
	}
}
