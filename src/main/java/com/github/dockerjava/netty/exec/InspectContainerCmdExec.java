package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

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

        webResource = booleanQueryParam(webResource, "size", command.getSize());

        LOGGER.debug("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .get(new TypeReference<InspectContainerResponse>() {
                });
    }

}
