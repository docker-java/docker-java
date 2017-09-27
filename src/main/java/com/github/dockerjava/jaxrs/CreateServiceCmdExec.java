package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.CreateServiceCmd;
import com.github.dockerjava.api.command.CreateServiceResponse;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

public class CreateServiceCmdExec extends AbstrSyncDockerCmdExec<CreateServiceCmd, CreateServiceResponse>
        implements CreateServiceCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateServiceCmdExec.class);

    public CreateServiceCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected CreateServiceResponse execute(CreateServiceCmd command) {
        WebTarget webResource = getBaseResource().path("/services/create");

        LOGGER.trace("POST: {} ", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(entity(command.getServiceSpec(), MediaType.APPLICATION_JSON), CreateServiceResponse.class);
    }
}
