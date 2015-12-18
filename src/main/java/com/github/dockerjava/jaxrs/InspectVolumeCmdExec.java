package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InspectVolumeCmd;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.core.DockerClientConfig;

public class InspectVolumeCmdExec extends AbstrSyncDockerCmdExec<InspectVolumeCmd, InspectVolumeResponse> implements
        InspectVolumeCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InspectVolumeCmdExec.class);

    public InspectVolumeCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InspectVolumeResponse execute(InspectVolumeCmd command) {
        WebTarget webResource = getBaseResource().path("/volumes/{name}").resolveTemplate("name", command.getName());

        LOGGER.trace("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(InspectVolumeResponse.class);
    }
}
