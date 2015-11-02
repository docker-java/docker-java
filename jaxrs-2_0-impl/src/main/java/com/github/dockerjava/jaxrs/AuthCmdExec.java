package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.UnauthorizedException;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.model.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AuthCmdExec extends AbstrSyncDockerCmdExec<AuthCmd, AuthResponse> implements AuthCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthCmdExec.class);

    public AuthCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected AuthResponse execute(AuthCmd command) {
        WebTarget webResource = getBaseResource().path("/auth");
        LOGGER.trace("POST: {}", webResource);
        Response response = webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(command.getAuthConfig(), MediaType.APPLICATION_JSON));

        if (response.getStatus() == 401) {
            throw new UnauthorizedException("Unauthorized");
        }

        return response.readEntity(AuthResponse.class);
    }

}
