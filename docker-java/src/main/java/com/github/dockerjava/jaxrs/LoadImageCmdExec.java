package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.core.DockerClientConfig;

public class LoadImageCmdExec extends AbstrSyncDockerCmdExec<LoadImageCmd, Void> implements LoadImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadImageCmdExec.class);

    public LoadImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(LoadImageCmd command) {
        WebTarget webTarget = getBaseResource().path("/images/load");

        LOGGER.trace("POST: {}", webTarget);
        webTarget.request().post(entity(command.getImageStream(), MediaType.APPLICATION_OCTET_STREAM));

        return null;
    }
}
