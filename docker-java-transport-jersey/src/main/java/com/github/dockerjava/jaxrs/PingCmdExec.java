package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.core.DockerClientConfig;

public class PingCmdExec extends AbstrSyncDockerCmdExec<PingCmd, Void> implements PingCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingCmdExec.class);

    public PingCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(PingCmd command) {
        WebTarget webResource = getBaseResource().path("/_ping");

        LOGGER.trace("GET: {}", webResource);
        webResource.request().get().close();

        return null;
    }

}
