package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.UpdateContainerCmd;
import com.github.dockerjava.api.model.UpdateContainerResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
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
                .post(command, new TypeReference<UpdateContainerResponse>() {
                });
    }
}
