package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.LoadImageResponseItem;
import com.github.dockerjava.core.async.JsonStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.POSTCallbackNotifier;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.core.DockerClientConfig;

public class LoadImageCmdExec extends AbstrAsyncDockerCmdExec<LoadImageCmd, LoadImageResponseItem> implements LoadImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadImageCmdExec.class);

    public LoadImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected AbstractCallbackNotifier<LoadImageResponseItem> callbackNotifier(LoadImageCmd command, ResultCallback<LoadImageResponseItem> resultCallback) {
        WebTarget webTarget = getBaseResource().path("/images/load");

        if (command.getQuiet() != null) {
            webTarget = webTarget.queryParam("quiet", command.getQuiet());
        }

        webTarget.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.CHUNKED);
        webTarget.property(ClientProperties.CHUNKED_ENCODING_SIZE, 1024 * 1024);

        LOGGER.trace("POST: {}", webTarget);
//        webTarget.request()
//                .post(entity(command.getImageStream(), MediaType.APPLICATION_OCTET_STREAM));

        return new POSTCallbackNotifier<>(new JsonStreamProcessor<>(LoadImageResponseItem.class),
                resultCallback,
                webTarget.request().accept(MediaType.TEXT_PLAIN),
                entity(command.getImageStream(), "application/tar")
        );
    }
}
