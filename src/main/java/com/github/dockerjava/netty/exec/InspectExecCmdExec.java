package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.InspectExecCmd;
import com.github.dockerjava.api.command.InspectExecResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

public class InspectExecCmdExec extends AbstrSyncDockerCmdExec<InspectExecCmd, InspectExecResponse> implements
        InspectExecCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(InspectExecCmdExec.class);

    public InspectExecCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InspectExecResponse execute(InspectExecCmd command) {
        WebTarget webResource = getBaseResource().path("/exec/{id}/json").resolveTemplate("id", command.getExecId());

        LOGGER.debug("GET: {}", webResource);

        return webResource.request().accept(MediaType.APPLICATION_JSON).get(new TypeReference<InspectExecResponse>() {
        });
    }
}
