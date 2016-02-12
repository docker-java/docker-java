package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.async.FrameStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.POSTCallbackNotifier;

public class AttachContainerCmdExec extends AbstrAsyncDockerCmdExec<AttachContainerCmd, Frame> implements
        AttachContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachContainerCmdExec.class);

    public AttachContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected AbstractCallbackNotifier<Frame> callbackNotifier(AttachContainerCmd command,
            ResultCallback<Frame> resultCallback) {

        if (command.getStdin() != null) {
            throw new UnsupportedOperationException(
                    "Passing stdin to the container is currently not supported. Try experimental netty engine!");
        }

        WebTarget webTarget = getBaseResource().path("/containers/{id}/attach").resolveTemplate("id",
                command.getContainerId());

        webTarget = booleanQueryParam(webTarget, "logs", command.hasLogsEnabled());
        webTarget = booleanQueryParam(webTarget, "stdout", command.hasStdoutEnabled());
        webTarget = booleanQueryParam(webTarget, "stderr", command.hasStderrEnabled());
        webTarget = booleanQueryParam(webTarget, "stream", command.hasFollowStreamEnabled());

        LOGGER.trace("POST: {}", webTarget);

        return new POSTCallbackNotifier<Frame>(new FrameStreamProcessor(), resultCallback, webTarget.request(), null);
    }
}
