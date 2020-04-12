package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.CreatePluginCmd;
import com.github.dockerjava.api.command.CreatePluginResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatePluginCmdExec extends AbstrSyncDockerCmdExec<CreatePluginCmd, CreatePluginResponse>
    implements CreatePluginCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePluginCmdExec.class);

    public CreatePluginCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected CreatePluginResponse execute(CreatePluginCmd command) {
        WebTarget webResource = getBaseResource().path("/plugins/create");

        LOGGER.trace("POST: {} ", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON)
            .post(command, new TypeReference<CreatePluginResponse>() {
            });
    }
}
