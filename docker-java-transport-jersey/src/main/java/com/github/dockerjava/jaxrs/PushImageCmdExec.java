package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.async.JsonStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.POSTCallbackNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.client.Entity.entity;

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
    protected AbstractCallbackNotifier<PushResponseItem> callbackNotifier(PushImageCmd command,
            ResultCallback<PushResponseItem> resultCallback) {

        WebTarget webResource = getBaseResource().path("/images/" + name(command) + "/push").queryParam("tag",
                command.getTag());

        LOGGER.trace("POST: {}", webResource);

        Builder builder = resourceWithAuthConfig(command.getAuthConfig(), webResource.request())
                .accept(MediaType.APPLICATION_JSON);

        return new POSTCallbackNotifier<>(
                new JsonStreamProcessor<>(objectMapper, PushResponseItem.class),
                resultCallback, builder, entity(null, MediaType.APPLICATION_JSON));
    }
}
