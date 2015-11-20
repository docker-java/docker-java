package com.github.dockerjava.netty.exec;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

public class CopyFileFromContainerCmdExec extends AbstrSyncDockerCmdExec<CopyFileFromContainerCmd, InputStream>
        implements CopyFileFromContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopyFileFromContainerCmdExec.class);

    public CopyFileFromContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InputStream execute(CopyFileFromContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/copy").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("POST: " + webResource.toString());

        return webResource.request().accept(MediaType.APPLICATION_OCTET_STREAM)
                .post(command);
    }

}
