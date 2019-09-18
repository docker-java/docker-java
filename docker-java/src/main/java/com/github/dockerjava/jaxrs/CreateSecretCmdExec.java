package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.CreateSecretCmd;
import com.github.dockerjava.api.command.CreateSecretResponse;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

public class CreateSecretCmdExec extends AbstrSyncDockerCmdExec<CreateSecretCmd, CreateSecretResponse>
        implements CreateSecretCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateSecretCmdExec.class);

    public CreateSecretCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected CreateSecretResponse execute(CreateSecretCmd command) {
        WebTarget webResource = getBaseResource().path("/secrets/create");


        LOGGER.trace("POST: {} ", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(entity(command.getSecretSpec(), MediaType.APPLICATION_JSON), CreateSecretResponse.class);
    }
}
