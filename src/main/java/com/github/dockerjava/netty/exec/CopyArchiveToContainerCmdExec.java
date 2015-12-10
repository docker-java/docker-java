package com.github.dockerjava.netty.exec;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CopyArchiveToContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

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
                .put(streamToUpload, MediaType.APPLICATION_X_TAR);

        return null;
    }
}
