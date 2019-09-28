package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CreateVolumeCmd;
import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.core.DockerClientConfig;

public class CreateVolumeCmdExec extends AbstrSyncDockerCmdExec<CreateVolumeCmd, CreateVolumeResponse> implements
        CreateVolumeCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateVolumeCmdExec.class);

    public CreateVolumeCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected CreateVolumeResponse execute(CreateVolumeCmd command) {
        WebTarget webResource = getBaseResource().path("/volumes/create");

        LOGGER.trace("POST: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(entity(command, MediaType.APPLICATION_JSON), CreateVolumeResponse.class);
    }
}
