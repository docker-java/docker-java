package com.github.dockerjava.jaxrs;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CopyArchiveFromContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;

public class CopyArchiveFromContainerCmdExec extends AbstrSyncDockerCmdExec<CopyArchiveFromContainerCmd, InputStream>
        implements CopyArchiveFromContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopyArchiveFromContainerCmdExec.class);

    public CopyArchiveFromContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InputStream execute(CopyArchiveFromContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/archive").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("Get: " + webResource.toString());

        Response response = webResource.queryParam("path", command.getResource()).request().accept("application/x-tar")
                .get();

        return new WrappedResponseInputStream(response);
    }

}
