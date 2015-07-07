package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.async.FrameStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.POSTCallbackNotifier;

public class AttachContainerCmdExec extends AbstrDockerCmdExec<AttachContainerCmd, Void> implements
        AttachContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachContainerCmdExec.class);

    public AttachContainerCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected Void execute(AttachContainerCmd command) {
        WebTarget webTarget = getBaseResource().path("/containers/{id}/attach")
                .resolveTemplate("id", command.getContainerId())
                .queryParam("logs", command.hasLogsEnabled() ? "1" : "0")
                // .queryParam("stdin", command.hasStdinEnabled() ? "1" : "0")
                .queryParam("stdout", command.hasStdoutEnabled() ? "1" : "0")
                .queryParam("stderr", command.hasStderrEnabled() ? "1" : "0")
                .queryParam("stream", command.hasFollowStreamEnabled() ? "1" : "0");

        LOGGER.trace("POST: {}", webTarget);

        POSTCallbackNotifier<Frame> callbackNotifier = new POSTCallbackNotifier<Frame>(new FrameStreamProcessor(),
                command.getResultCallback(), webTarget);

        AbstractCallbackNotifier.startAsyncProcessing(callbackNotifier);

        return null;
    }
}
