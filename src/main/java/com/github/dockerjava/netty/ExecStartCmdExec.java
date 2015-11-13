package com.github.dockerjava.netty;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.core.DockerClientConfig;

public class ExecStartCmdExec extends AbstrSyncDockerCmdExec<ExecStartCmd, InputStream> implements ExecStartCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecStartCmdExec.class);

	public ExecStartCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
		super(baseResource, dockerClientConfig);
	}

	@Override
	protected InputStream execute(ExecStartCmd command) {
		WebTarget webResource = getBaseResource().path("/exec/{id}/start").resolveTemplate("id", command.getExecId());

		LOGGER.trace("POST: {}", webResource);

		webResource.request().accept(MediaType.APPLICATION_JSON).post(command, System.out, null, System.in);

		return null;
	}
}
