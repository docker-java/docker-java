package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.CreateImageResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

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
        return webResource.request().accept(MediaType.APPLICATION_OCTET_STREAM)
                .post(new TypeReference<CreateImageResponse>() {
                }, command.getImageStream());
    }
}
