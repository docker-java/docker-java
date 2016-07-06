package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.WebTarget;

public class LoadImageCmdExec extends AbstrSyncDockerCmdExec<LoadImageCmd, Void> implements
        LoadImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadImageCmdExec.class);

    public LoadImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(LoadImageCmd command) {
        WebTarget webResource = getBaseResource().path("/images/load");

        LOGGER.trace("POST: {}", webResource);
        return webResource.request()
                .post(new TypeReference<Void>() {
                }, command.getImageStream());
    }
}
