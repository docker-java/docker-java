package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class InspectContainerCmdExec extends AbstrSyncDockerCmdExec<InspectContainerCmd, InspectContainerResponse>
        implements InspectContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InspectContainerCmdExec.class);

    public InspectContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InspectContainerResponse execute(InspectContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/json").resolveTemplate("id",
                command.getContainerId());

        LOGGER.debug("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(InspectContainerResponse.class);
    }

}
