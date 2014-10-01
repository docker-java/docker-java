package com.github.dockerjava.jaxrs1;

import java.io.ByteArrayInputStream;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.BadRequestException;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.sun.jersey.api.client.WebResource;

public class StartContainerCmdExec extends AbstrDockerCmdExec<StartContainerCmd, Void> implements StartContainerCmd.Exec {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartContainerCmdExec.class);

	public StartContainerCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(StartContainerCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/containers/{id}/start", command.getContainerId())
		        .build();

		// Workaround for Docker issue 6231.  This avoids the use of chunked encoding.
		ObjectMapper mapper = new ObjectMapper();
		ByteArrayInputStream bais;
        try {
            bais = new ByteArrayInputStream(mapper.writeValueAsBytes(command));
        } catch (JsonProcessingException jpe) {
            throw new BadRequestException("Unable to serialize start command", jpe);
        }
		LOGGER.trace("POST: {}", webResource);
		webResource.accept(MediaType.APPLICATION_JSON)
		    .entity(bais, MediaType.APPLICATION_JSON)
		    .post();

		return null;
	}

}
