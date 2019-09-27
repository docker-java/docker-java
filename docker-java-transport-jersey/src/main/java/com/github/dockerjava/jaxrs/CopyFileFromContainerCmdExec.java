package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;

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

        Response response = webResource.request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .post(entity(command, MediaType.APPLICATION_JSON));

        return new WrappedResponseInputStream(response);
    }

}
