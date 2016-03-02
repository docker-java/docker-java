package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.UpdateContainerCmd;
import com.github.dockerjava.api.model.UpdateContainerResponse;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

/**
 * Update container settings.
 *
 * @author Kanstantsin Shautsou
 */
public class UpdateContainerCmdExec extends AbstrSyncDockerCmdExec<UpdateContainerCmd, UpdateContainerResponse>
        implements UpdateContainerCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateContainerCmdExec.class);

    public UpdateContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected UpdateContainerResponse execute(UpdateContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/update")
                .resolveTemplate("id", command.getContainerId());

        LOGGER.trace("POST: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(entity(command, MediaType.APPLICATION_JSON), UpdateContainerResponse.class);
    }
}
