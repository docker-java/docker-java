package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.InspectNetworkCmd;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class InspectNetworkCmdExec extends AbstrSyncDockerCmdExec<InspectNetworkCmd, Network> implements InspectNetworkCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListNetworksCmdExec.class);

    public InspectNetworkCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Network execute(InspectNetworkCmd command) {

        WebTarget webResource = getBaseResource().path("/networks/{id}").resolveTemplate("id",
                command.getNetworkId());

        LOGGER.debug("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(Network.class);
    }
}
