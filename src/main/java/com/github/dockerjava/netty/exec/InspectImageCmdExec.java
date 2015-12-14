package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

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

        return webResource.request().accept(MediaType.APPLICATION_JSON).get(new TypeReference<InspectImageResponse>() {
        });
    }

}
