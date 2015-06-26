package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.InspectExecCmd;
import com.github.dockerjava.api.command.InspectExecResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class InspectExecCmdExec extends AbstrDockerCmdExec<InspectExecCmd, InspectExecResponse> implements
        InspectExecCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(InspectExecCmdExec.class);

    public InspectExecCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected InspectExecResponse execute(InspectExecCmd command) {
        WebTarget webResource = getBaseResource().path("/exec/{id}/json").resolveTemplate("id", command.getExecId());
        LOGGER.debug("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(InspectExecResponse.class);
    }
}
