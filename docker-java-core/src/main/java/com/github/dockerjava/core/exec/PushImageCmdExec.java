package com.github.dockerjava.core.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.InvocationBuilder;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;

public class PushImageCmdExec extends AbstrAsyncDockerCmdExec<PushImageCmd, PushResponseItem> implements
        PushImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushImageCmdExec.class);

    public PushImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute0(PushImageCmd command, ResultCallback<PushResponseItem> resultCallback) {
        WebTarget webResource = getBaseResource().path("/images/{imageName}/push")
            .resolveTemplate("imageName", command.getName())
            .queryParam("tag", command.getTag());

        LOGGER.trace("POST: {}", webResource);

        InvocationBuilder builder = resourceWithAuthConfig(command.getAuthConfig(), webResource.request())
                .accept(MediaType.APPLICATION_JSON);

        builder.post(null, new TypeReference<PushResponseItem>() {
        }, resultCallback);

        return null;
    }
}
