package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InspectExecCmd;
import com.github.dockerjava.api.command.InspectExecResponse;
import com.github.dockerjava.core.DockerClientConfig;

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
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(InspectExecResponse.class);
    }
}
