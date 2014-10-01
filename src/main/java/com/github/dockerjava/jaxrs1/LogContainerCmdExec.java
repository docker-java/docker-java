package com.github.dockerjava.jaxrs1;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.LogContainerCmd;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class LogContainerCmdExec extends AbstrDockerCmdExec<LogContainerCmd, InputStream> implements LogContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogContainerCmdExec.class);

	public LogContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected InputStream execute(LogContainerCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/containers/{id}/logs", command.getContainerId())
				.queryParam("timestamps", command.hasTimestampsEnabled() ? "1" : "0")
				.queryParam("stdout", command.hasStdoutEnabled() ? "1" : "0")
				.queryParam("stderr", command.hasStderrEnabled() ? "1" : "0")
				.queryParam("follow", command.hasFollowStreamEnabled() ? "1" : "0")
				.queryParam("tail", command.getTail() < 0 ? "all" : "" + command.getTail())
				.build();

		LOGGER.trace("GET: {}", webResource);
		return webResource.get(ClientResponse.class).getEntityInputStream();
	}

}
