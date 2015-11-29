package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.InvocationBuilder;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

public class PullImageCmdExec extends AbstrAsyncDockerCmdExec<PullImageCmd, PullResponseItem> implements
        PullImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(PullImageCmdExec.class);

    public PullImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    private InvocationBuilder resourceWithOptionalAuthConfig(PullImageCmd command, InvocationBuilder request) {
        AuthConfig authConfig = command.getAuthConfig();
        if (authConfig != null) {
            request = request.header("X-Registry-Auth", registryAuth(authConfig));
        }
        return request;
    }

    @Override
    protected Void execute0(PullImageCmd command, ResultCallback<PullResponseItem> resultCallback) {

        WebTarget webResource = getBaseResource().path("/images/create").queryParam("tag", command.getTag())
                .queryParam("fromImage", command.getRepository()).queryParam("registry", command.getRegistry());

        LOGGER.trace("POST: {}", webResource);
        resourceWithOptionalAuthConfig(command, webResource.request()).accept(MediaType.APPLICATION_OCTET_STREAM).post(
                null, new TypeReference<PullResponseItem>() {
                }, resultCallback);

        return null;
    }
}
