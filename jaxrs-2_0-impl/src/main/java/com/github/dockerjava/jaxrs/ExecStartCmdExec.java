package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public class ExecStartCmdExec extends AbstrSyncDockerCmdExec<ExecStartCmd, InputStream> implements ExecStartCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecStartCmdExec.class);

    public ExecStartCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InputStream execute(ExecStartCmd command) {
        WebTarget webResource = getBaseResource().path("/exec/{id}/start").resolveTemplate("id", command.getExecId());

        LOGGER.trace("POST: {}", webResource);

        Response response = webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(command, MediaType.APPLICATION_JSON), Response.class);

        return new WrappedResponseInputStream(response);
    }
}
