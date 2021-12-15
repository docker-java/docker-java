package com.github.dockerjava.core.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.api.model.WaitResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;

public class WaitContainerCmdExec extends AbstrAsyncDockerCmdExec<WaitContainerCmd, WaitResponse> implements
        WaitContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitContainerCmdExec.class);

    public WaitContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute0(WaitContainerCmd command, ResultCallback<WaitResponse> resultCallback) {
        WebTarget webTarget = getBaseResource().path("/containers/{id}/wait").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("POST: {}", webTarget);

        webTarget.request().accept(MediaType.APPLICATION_JSON).post((Object) null, new TypeReference<WaitResponse>() {
        }, resultCallback);

        return null;
    }

}
