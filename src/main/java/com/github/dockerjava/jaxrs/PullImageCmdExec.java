package com.github.dockerjava.jaxrs;

import java.io.InputStream;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;

public class PullImageCmdExec extends AbstrDockerCmdExec<PullImageCmd, InputStream> implements PullImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(PullImageCmdExec.class);

    public PullImageCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected InputStream execute(PullImageCmd command) {
        WebTarget webResource = getBaseResource().path("/images/create").queryParam("tag", command.getTag())
                .queryParam("fromImage", command.getRepository()).queryParam("registry", command.getRegistry());

        LOGGER.trace("POST: {}", webResource);
        Response response = resourceWithOptionalAuthConfig(command, webResource.request()).accept(
                MediaType.APPLICATION_OCTET_STREAM_TYPE).post(null);

        return new WrappedResponseInputStream(response);
    }

    private Invocation.Builder resourceWithOptionalAuthConfig(PullImageCmd command, Invocation.Builder request) {
        AuthConfig authConfig = command.getAuthConfig();
        if (authConfig != null) {
            request = request.header("X-Registry-Auth", registryAuth(authConfig));
        }
        return request;
    }

}
