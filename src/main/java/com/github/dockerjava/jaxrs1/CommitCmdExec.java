package com.github.dockerjava.jaxrs1;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.command.CommitCmd;
import com.sun.jersey.api.client.WebResource;

public class CommitCmdExec extends AbstrDockerCmdExec<CommitCmd, String> implements CommitCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CommitCmdExec.class);
	
	public CommitCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected String execute(CommitCmd command) {
	    WebResource webResource = getBaseResource()
	            .path("/commit")
                .queryParam("container", command.getContainerId())
                .queryParam("repo", command.getRepository())
                .queryParam("tag", command.getTag())
                .queryParam("m", command.getMessage())
                .queryParam("author", command.getAuthor())
                .queryParam("pause",  command.hasPauseEnabled() ? "1" : "0")
                .build();
		
		LOGGER.trace("POST: {}", webResource);
		ObjectNode objectNode = webResource
		        .accept("application/vnd.docker.raw-stream")
		        .entity(command, MediaType.APPLICATION_JSON)
		        .post(ObjectNode.class);
        return objectNode.get("Id").asText();
	}

}
