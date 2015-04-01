package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.ExecStartCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

import static javax.ws.rs.client.Entity.entity;

public class ExecStartCmdExec extends AbstrDockerCmdExec<ExecStartCmd, InputStream> implements ExecStartCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecStartCmdExec.class);

    public ExecStartCmdExec(WebTarget baseResource) {
        super(baseResource);
    }


    @Override
    protected InputStream execute(ExecStartCmd command) {
        WebTarget webResource = getBaseResource().path("/exec/{id}/start").resolveTemplate("id", command.getExecId());

        LOGGER.trace("POST: {}", webResource);

		return webResource
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(entity(command, MediaType.APPLICATION_JSON), Response.class).readEntity(InputStream.class);
    }
}
