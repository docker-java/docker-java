package com.github.dockerjava.netty.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.InspectNetworkCmd;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InspectNetworkCmdExec extends AbstrSyncDockerCmdExec<InspectNetworkCmd, Network> implements
        InspectNetworkCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InspectNetworkCmdExec.class);

    public InspectNetworkCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Network execute(InspectNetworkCmd command) {
        WebTarget webResource = getBaseResource().path("/networks/{id}").resolveTemplate("id", command.getNetworkId());

        LOGGER.trace("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(new TypeReference<Network>() {
        });
    }
}
