package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.api.model.WaitResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.async.JsonStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.POSTCallbackNotifier;

public class WaitContainerCmdExec extends AbstrAsyncDockerCmdExec<WaitContainerCmd, WaitResponse> implements
        WaitContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitContainerCmdExec.class);

    public WaitContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected AbstractCallbackNotifier<WaitResponse> callbackNotifier(WaitContainerCmd command,
            ResultCallback<WaitResponse> resultCallback) {

        WebTarget webResource = getBaseResource().path("/containers/{id}/wait").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("POST: {}", webResource);

        return new POSTCallbackNotifier<>(new JsonStreamProcessor<>(objectMapper, WaitResponse.class),
                resultCallback, webResource.request().accept(MediaType.APPLICATION_JSON), entity(null,
                MediaType.APPLICATION_JSON));
    }

}
