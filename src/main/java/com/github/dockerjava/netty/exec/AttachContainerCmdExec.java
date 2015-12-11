package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.WebTarget;

public class AttachContainerCmdExec extends AbstrAsyncDockerCmdExec<AttachContainerCmd, Frame> implements
        AttachContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachContainerCmdExec.class);

    public AttachContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute0(AttachContainerCmd command, ResultCallback<Frame> resultCallback) {

        WebTarget webTarget = getBaseResource().path("/containers/{id}/attach").resolveTemplate("id",
                command.getContainerId());

        webTarget = booleanQueryParam(webTarget, "logs", command.hasLogsEnabled());
        webTarget = booleanQueryParam(webTarget, "stdout", command.hasStdoutEnabled());
        webTarget = booleanQueryParam(webTarget, "stderr", command.hasStderrEnabled());
        webTarget = booleanQueryParam(webTarget, "stdin", command.getStdin() != null);
        webTarget = booleanQueryParam(webTarget, "stream", command.hasFollowStreamEnabled());

        LOGGER.trace("POST: {}", webTarget);

        webTarget.request().post(null, command.getStdin(), resultCallback);

        return null;
    }
}
