package com.github.dockerjava.jaxrs.jersey.jaxrs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.command.CommitCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class CommitCmdExec extends AbstrSyncDockerCmdExec<CommitCmd, String> implements CommitCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommitCmdExec.class);

    public CommitCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected String execute(CommitCmd command) {
        WebTarget webResource = getBaseResource().path("/commit").queryParam("container", command.getContainerId())
                .queryParam("repo", command.getRepository()).queryParam("tag", command.getTag())
                .queryParam("m", command.getMessage()).queryParam("author", command.getAuthor())
                .queryParam("pause", command.hasPauseEnabled() ? "1" : "0");

        LOGGER.trace("POST: {}", webResource);
        ObjectNode objectNode = webResource.request().accept("application/vnd.docker.raw-stream")
                .post(Entity.entity(command, MediaType.APPLICATION_JSON), ObjectNode.class);
        return objectNode.get("Id").asText();
    }

}
