package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.core.DockerClientConfig;

public class CommitCmdExec extends AbstrSyncDockerCmdExec<CommitCmd, String> implements CommitCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommitCmdExec.class);

    public CommitCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected String execute(CommitCmd command) {
        WebTarget webTarget = getBaseResource().path("/commit").queryParam("container", command.getContainerId())
                .queryParam("repo", command.getRepository()).queryParam("tag", command.getTag())
                .queryParam("m", command.getMessage()).queryParam("author", command.getAuthor());

        webTarget = booleanQueryParam(webTarget, "pause", command.hasPauseEnabled());

        LOGGER.trace("POST: {}", webTarget);
        ObjectNode objectNode = webTarget.request().accept("application/vnd.docker.raw-stream")
                .post(entity(command, MediaType.APPLICATION_JSON), ObjectNode.class);
        return objectNode.get("Id").asText();
    }

}
