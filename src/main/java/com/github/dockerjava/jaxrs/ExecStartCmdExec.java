package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.async.FrameStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.POSTCallbackNotifier;

public class ExecStartCmdExec extends AbstrAsyncDockerCmdExec<ExecStartCmd, Frame> implements ExecStartCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecStartCmdExec.class);

    public ExecStartCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected AbstractCallbackNotifier<Frame> callbackNotifier(ExecStartCmd command,
            ResultCallback<Frame> resultCallback) {
        WebTarget webTarget = getBaseResource().path("/exec/{id}/start").resolveTemplate("id", command.getExecId());

        LOGGER.trace("POST: {}", webTarget);

        return new POSTCallbackNotifier<Frame>(new FrameStreamProcessor(), resultCallback, webTarget.request().accept(
                MediaType.APPLICATION_JSON), entity(command, MediaType.APPLICATION_JSON));
    }
}
