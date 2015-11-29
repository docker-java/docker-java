package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.InvocationBuilder;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

public class PushImageCmdExec extends AbstrAsyncDockerCmdExec<PushImageCmd, PushResponseItem> implements
        PushImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushImageCmdExec.class);

    public PushImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    private String name(PushImageCmd command) {
        String name = command.getName();
        AuthConfig authConfig = command.getAuthConfig();
        return (name.contains("/") || authConfig == null) ? name : authConfig.getUsername();
    }

    @Override
    protected Void execute0(PushImageCmd command, ResultCallback<PushResponseItem> resultCallback) {

        WebTarget webResource = getBaseResource().path("/images/" + name(command) + "/push").queryParam("tag",
                command.getTag());

        final String registryAuth = registryAuth(command.getAuthConfig());
        LOGGER.trace("POST: {}", webResource);

        InvocationBuilder builder = webResource.request().header("X-Registry-Auth", registryAuth)
                .accept(MediaType.APPLICATION_JSON);

        builder.post(null, new TypeReference<PushResponseItem>() {
        }, resultCallback);

        return null;
    }
}
