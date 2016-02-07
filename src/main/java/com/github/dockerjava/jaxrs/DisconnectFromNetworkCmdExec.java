package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.DisconnectFromNetworkCmd;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

public class DisconnectFromNetworkCmdExec extends AbstrSyncDockerCmdExec<DisconnectFromNetworkCmd, Void>
        implements DisconnectFromNetworkCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisconnectFromNetworkCmdExec.class);

    public DisconnectFromNetworkCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(DisconnectFromNetworkCmd command) {

        WebTarget webTarget = getBaseResource().path("/networks/" + command.getNetworkId() + "/disconnect");

        LOGGER.trace("POST: {}", webTarget);
        webTarget.request().post(entity(command, MediaType.APPLICATION_JSON));

        return null;
    }
}
