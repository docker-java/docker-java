package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

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
        ObjectNode objectNode = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .post(command, new TypeReference<ObjectNode>() {
                });

        return objectNode.get("Id").asText();
    }

}
