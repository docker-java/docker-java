package com.github.dockerjava.core.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;

public class CreateContainerCmdExec extends AbstrSyncDockerCmdExec<CreateContainerCmd, CreateContainerResponse>
        implements CreateContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateContainerCmdExec.class);

    public CreateContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected CreateContainerResponse execute(CreateContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/create");

        if (command.getName() != null) {
            webResource = webResource.queryParam("name", command.getName());
        }

        LOGGER.trace("POST: {} ", webResource);
        return resourceWithOptionalAuthConfig(command.getAuthConfig(), webResource.request())
                .accept(MediaType.APPLICATION_JSON)
                .post(command, new TypeReference<CreateContainerResponse>() { });
    }
}
