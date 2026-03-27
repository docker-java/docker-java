package com.github.dockerjava.core.exec;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.ExportContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;

public class ExportContainerCmdExec extends AbstrSyncDockerCmdExec<ExportContainerCmd, InputStream>
        implements ExportContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportContainerCmdExec.class);

    public ExportContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InputStream execute(ExportContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/export").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("GET: {}", webResource);

        return webResource.request().accept(MediaType.APPLICATION_X_TAR).get();
    }
}
