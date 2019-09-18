package com.github.dockerjava.jaxrs;


import com.github.dockerjava.api.command.InitializeSwarmCmd;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

public class InitializeSwarmCmdExec extends AbstrSyncDockerCmdExec<InitializeSwarmCmd, Void>
        implements InitializeSwarmCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializeSwarmCmdExec.class);

    public InitializeSwarmCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(InitializeSwarmCmd command) {
        WebTarget webResource = getBaseResource().path("/swarm/init");

        LOGGER.trace("POST: {} ", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(entity(command, MediaType.APPLICATION_JSON)).close();
        return null;
    }
}
