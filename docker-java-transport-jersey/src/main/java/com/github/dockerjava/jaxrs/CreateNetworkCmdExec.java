package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

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
                .post(entity(command, MediaType.APPLICATION_JSON), CreateNetworkResponse.class);
    }
}
