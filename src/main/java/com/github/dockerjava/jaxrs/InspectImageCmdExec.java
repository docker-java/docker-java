package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.core.DockerClientConfig;

public class InspectImageCmdExec extends AbstrSyncDockerCmdExec<InspectImageCmd, InspectImageResponse> implements
        InspectImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InspectImageCmdExec.class);

    public InspectImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InspectImageResponse execute(InspectImageCmd command) {
        WebTarget webResource = getBaseResource().path("/images/{id}/json").resolveTemplate("id", command.getImageId());

        LOGGER.trace("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(InspectImageResponse.class);
    }

}
