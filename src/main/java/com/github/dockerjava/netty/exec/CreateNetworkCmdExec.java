package com.github.dockerjava.netty.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateNetworkCmdExec extends AbstrSyncDockerCmdExec<CreateNetworkCmd, CreateNetworkResponse> implements
        CreateNetworkCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNetworkCmdExec.class);

    public CreateNetworkCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected CreateNetworkResponse execute(CreateNetworkCmd command) {
        WebTarget webResource = getBaseResource().path("/networks/create");

        LOGGER.trace("POST: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(command, new TypeReference<CreateNetworkResponse>() {
                });
    }
}
