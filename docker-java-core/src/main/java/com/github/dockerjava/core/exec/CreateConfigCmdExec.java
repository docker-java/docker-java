package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.CreateConfigCmd;
import com.github.dockerjava.api.command.CreateConfigResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateConfigCmdExec extends AbstrSyncDockerCmdExec<CreateConfigCmd, CreateConfigResponse>
        implements CreateConfigCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateConfigCmdExec.class);

    public CreateConfigCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected CreateConfigResponse execute(CreateConfigCmd command) {
        WebTarget webResource = getBaseResource().path("/configs/create");

        LOGGER.trace("POST: {} ", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(command, new TypeReference<CreateConfigResponse>() {
                });
    }
}
