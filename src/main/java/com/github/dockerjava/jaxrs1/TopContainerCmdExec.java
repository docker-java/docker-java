package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.TopContainerResponse;
import com.sun.jersey.api.client.WebResource;

public class TopContainerCmdExec extends AbstrDockerCmdExec<TopContainerCmd, TopContainerResponse> implements TopContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopContainerCmdExec.class);

	public TopContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected TopContainerResponse execute(TopContainerCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/containers/{id}/top", command.getContainerId())
		        .build();

		if(!StringUtils.isEmpty(command.getPsArgs()))
			webResource = webResource.queryParam("ps_args", command.getPsArgs());
		
		LOGGER.trace("GET: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON).get(TopContainerResponse.class);
	}

}
