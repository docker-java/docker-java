package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.ConnectToNetworkCmd;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

public class ConnectToNetworkCmdExec extends AbstrSyncDockerCmdExec<ConnectToNetworkCmd, Void>
        implements ConnectToNetworkCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectToNetworkCmdExec.class);

    public ConnectToNetworkCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(ConnectToNetworkCmd command) {

        WebTarget webTarget = getBaseResource().path("/networks/" + command.getNetworkId() + "/connect");

        LOGGER.trace("POST: {}", webTarget);
        webTarget.request().post(entity(command, MediaType.APPLICATION_JSON));

        return null;
    }
}
