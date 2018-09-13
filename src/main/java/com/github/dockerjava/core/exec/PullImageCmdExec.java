package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PullImageCmdExec extends AbstrAsyncDockerCmdExec<PullImageCmd, PullResponseItem> implements
        PullImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(PullImageCmdExec.class);

    public PullImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute0(PullImageCmd command, ResultCallback<PullResponseItem> resultCallback) {

        WebTarget webResource = getBaseResource().path("/images/create").queryParam("tag", command.getTag())
                .queryParam("fromImage", command.getRepository()).queryParam("registry", command.getRegistry());

        if (command.getPlatform() != null) {
            webResource = webResource.queryParam("platform", command.getPlatform());
        }

        LOGGER.trace("POST: {}", webResource);
        resourceWithOptionalAuthConfig(command.getAuthConfig(), webResource.request()).accept(MediaType.APPLICATION_OCTET_STREAM).post(
                null, new TypeReference<PullResponseItem>() {
                }, resultCallback);

        return null;
    }
}
