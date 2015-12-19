package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.CreateVolumeCmd;
import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

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
                .post(command, new TypeReference<CreateVolumeResponse>() {
                });
    }
}
