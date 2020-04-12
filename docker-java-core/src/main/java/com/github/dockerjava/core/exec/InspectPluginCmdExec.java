package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.InspectPluginCmd;
import com.github.dockerjava.api.command.InspectPluginResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InspectPluginCmdExec extends AbstrSyncDockerCmdExec<InspectPluginCmd, InspectPluginResponse> implements
    InspectPluginCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InspectPluginCmdExec.class);

    public InspectPluginCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InspectPluginResponse execute(InspectPluginCmd command) {
        WebTarget webResource = getBaseResource().path("/plugins/{name}/json").resolveTemplate("id", command.getName());
        LOGGER.trace("GET: {}", webResource);

        return webResource.request().accept(MediaType.APPLICATION_JSON).get(new TypeReference<InspectPluginResponse>() {
        });
    }
}
