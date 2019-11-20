package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.PullResponseItem;
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

public class PullImageCmdExec extends AbstrAsyncDockerCmdExec<PullImageCmd, PullResponseItem> implements
        PullImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(PullImageCmdExec.class);

    public PullImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected AbstractCallbackNotifier<PullResponseItem> callbackNotifier(PullImageCmd command,
            ResultCallback<PullResponseItem> resultCallback) {

        WebTarget webResource = getBaseResource().path("/images/create").queryParam("tag", command.getTag())
                .queryParam("fromImage", command.getRepository()).queryParam("registry", command.getRegistry());

        if (command.getPlatform() != null) {
            webResource = webResource.queryParam("platform", command.getPlatform());
        }

        LOGGER.trace("POST: {}", webResource);
        Builder builder = resourceWithOptionalAuthConfig(command.getAuthConfig(), webResource.request()).accept(
                MediaType.APPLICATION_OCTET_STREAM_TYPE);

        return new POSTCallbackNotifier<>(
                new JsonStreamProcessor<>(objectMapper, PullResponseItem.class),
                resultCallback, builder, entity(null, MediaType.APPLICATION_JSON));
    }
}
