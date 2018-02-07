package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.UpdateSwarmNodeCmd;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

/**
 * Update swarmNode spec
 */
public class UpdateSwarmNodeCmdExec extends AbstrSyncDockerCmdExec<UpdateSwarmNodeCmd, Void>
        implements UpdateSwarmNodeCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSwarmNodeCmdExec.class);

    public UpdateSwarmNodeCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(UpdateSwarmNodeCmd command) {
        WebTarget webResource = getBaseResource().path("/nodes/{id}/update")
                .resolveTemplate("id", command.getSwarmNodeId())
                .queryParam("version", command.getVersion());

        LOGGER.trace("POST: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(entity(command.getSwarmNodeSpec(), MediaType.APPLICATION_JSON)).close();
        return null;
    }
}
