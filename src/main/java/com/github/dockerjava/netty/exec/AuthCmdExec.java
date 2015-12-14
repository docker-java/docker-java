package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.model.AuthResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

public class AuthCmdExec extends AbstrSyncDockerCmdExec<AuthCmd, AuthResponse> implements AuthCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthCmdExec.class);

    public AuthCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected AuthResponse execute(AuthCmd command) {
        WebTarget webResource = getBaseResource().path("/auth");
        LOGGER.trace("POST: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(command.getAuthConfig(), new TypeReference<AuthResponse>() {
                });
    }

}
