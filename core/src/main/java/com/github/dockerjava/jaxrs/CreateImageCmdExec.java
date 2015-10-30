package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.CreateImageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

public class CreateImageCmdExec extends AbstrSyncDockerCmdExec<CreateImageCmd, CreateImageResponse> implements
        CreateImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateImageCmdExec.class);

    public CreateImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected CreateImageResponse execute(CreateImageCmd command) {
        WebTarget webResource = getBaseResource().path("/images/create").queryParam("repo", command.getRepository())
                .queryParam("tag", command.getTag()).queryParam("fromSrc", "-");

        LOGGER.trace("POST: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .post(entity(command.getImageStream(), MediaType.APPLICATION_OCTET_STREAM), CreateImageResponse.class);
    }
}
