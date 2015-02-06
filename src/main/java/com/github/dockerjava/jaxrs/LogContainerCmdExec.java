package com.github.dockerjava.jaxrs;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.LogContainerCmd;

public class LogContainerCmdExec extends AbstrDockerCmdExec<LogContainerCmd, InputStream> implements LogContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogContainerCmdExec.class);

	public LogContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(LogContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/logs")
				.resolveTemplate("id", command.getContainerId())
				.queryParam("timestamps", command.hasTimestampsEnabled() ? "1" : "0")
				.queryParam("stdout", command.hasStdoutEnabled() ? "1" : "0")
				.queryParam("stderr", command.hasStderrEnabled() ? "1" : "0")
				.queryParam("follow", command.hasFollowStreamEnabled() ? "1" : "0")
				.queryParam("tail", command.getTail() < 0 ? "all" : "" + command.getTail());

		LOGGER.trace("GET: {}", webResource);
		return webResource.request().get().readEntity(InputStream.class);
	}

}
