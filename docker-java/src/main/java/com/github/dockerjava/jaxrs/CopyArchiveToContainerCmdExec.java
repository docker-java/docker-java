package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CopyArchiveToContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;

public class CopyArchiveToContainerCmdExec extends AbstrSyncDockerCmdExec<CopyArchiveToContainerCmd, Void> implements
        CopyArchiveToContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopyArchiveFromContainerCmdExec.class);

    public CopyArchiveToContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(CopyArchiveToContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/archive").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("PUT: " + webResource.toString());
        InputStream streamToUpload = command.getTarInputStream();
        webResource.queryParam("path", command.getRemotePath())
                .queryParam("noOverwriteDirNonDir", command.isNoOverwriteDirNonDir()).request()
                .put(entity(streamToUpload, "application/x-tar")).close();
        return null;

    }
}
