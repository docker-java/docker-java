package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.UpdateServiceCmd;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

/**
 * Update service settings.
 */
public class UpdateServiceCmdExec extends AbstrSyncDockerCmdExec<UpdateServiceCmd, Void>
        implements UpdateServiceCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateServiceCmdExec.class);

    public UpdateServiceCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(UpdateServiceCmd command) {
        WebTarget webResource = getBaseResource().path("/services/{id}/update")
                .resolveTemplate("id", command.getServiceId())
                .queryParam("version", command.getVersion());

        LOGGER.trace("POST: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(entity(command.getServiceSpec(), MediaType.APPLICATION_JSON)).close();

        return null;
    }
}
