package com.github.dockerjava.core.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

import java.io.IOException;

public class PingCmdExec extends AbstrSyncDockerCmdExec<PingCmd, Void> implements PingCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingCmdExec.class);

    public PingCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(PingCmd command) {
        WebTarget webResource = getBaseResource().path("/_ping");

        LOGGER.trace("GET: {}", webResource);
        try {
            webResource.request().get().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
