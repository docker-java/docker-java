package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.CreateSecretCmd;
import com.github.dockerjava.api.command.CreateSecretResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                .post(command.getSecretSpec(), new TypeReference<CreateSecretResponse>() {
                });
    }
}
