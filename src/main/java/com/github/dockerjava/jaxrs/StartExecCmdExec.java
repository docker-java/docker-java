package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.StartExecCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.async.FrameStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.POSTCallbackNotifier;

public class StartExecCmdExec extends AbstrAsyncDockerCmdExec<StartExecCmd, Frame> implements StartExecCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartExecCmdExec.class);

    public StartExecCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected AbstractCallbackNotifier<Frame> callbackNotifier(StartExecCmd command,
            ResultCallback<Frame> resultCallback) {
        WebTarget webTarget = getBaseResource().path("/exec/{id}/start").resolveTemplate("id", command.getExecId());

        LOGGER.trace("POST: {}", webTarget);

        return new POSTCallbackNotifier<Frame>(new FrameStreamProcessor(), resultCallback, webTarget.request().accept(
                MediaType.APPLICATION_JSON), null);
    }
}
