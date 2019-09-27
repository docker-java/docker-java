package com.github.dockerjava.jaxrs;


import com.github.dockerjava.api.command.JoinSwarmCmd;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

public class JoinSwarmCmdExec extends AbstrSyncDockerCmdExec<JoinSwarmCmd, Void>
        implements JoinSwarmCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializeSwarmCmdExec.class);

    public JoinSwarmCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(JoinSwarmCmd command) {
        WebTarget webResource = getBaseResource().path("/swarm/join");

        LOGGER.trace("POST: {} ", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(entity(command, MediaType.APPLICATION_JSON)).close();
        return null;
    }
}
