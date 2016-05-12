package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.core.DockerClientConfig;

public class InspectContainerCmdExec extends AbstrSyncDockerCmdExec<InspectContainerCmd, InspectContainerResponse>
        implements InspectContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InspectContainerCmdExec.class);

    public InspectContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InspectContainerResponse execute(InspectContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/json")
                                                 .resolveTemplate("id", command.getContainerId());

        webResource = booleanQueryParam(webResource, "size", command.getSize());

        LOGGER.debug("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(InspectContainerResponse.class);
    }

}
