package com.github.dockerjava.jaxrs2;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.TopContainerResponse;

public class TopContainerCmdExec extends AbstrDockerCmdExec<TopContainerCmd, TopContainerResponse> implements TopContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopContainerCmdExec.class);

	public TopContainerCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected TopContainerResponse execute(TopContainerCmd command) {
		WebTarget webResource = getBaseResource().path("/containers/{id}/top")
				.resolveTemplate("id", command.getContainerId());

		if(!StringUtils.isEmpty(command.getPsArgs()))
			webResource = webResource.queryParam("ps_args", command.getPsArgs());
		
		LOGGER.trace("GET: {}", webResource);
		return webResource.request().accept(MediaType.APPLICATION_JSON).get(TopContainerResponse.class);
	}

}
