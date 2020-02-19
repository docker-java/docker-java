package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.CreateServiceCmd;
import com.github.dockerjava.api.command.CreateServiceResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.InvocationBuilder;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        InvocationBuilder builder = resourceWithOptionalAuthConfig(command.getAuthConfig(), webResource.request())
                .accept(MediaType.APPLICATION_JSON);

        return builder.post(command.getServiceSpec(), new TypeReference<CreateServiceResponse>() {
        });
    }
}
