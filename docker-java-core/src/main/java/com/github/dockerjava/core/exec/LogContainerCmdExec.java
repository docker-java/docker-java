package com.github.dockerjava.core.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

public class LogContainerCmdExec extends AbstrAsyncDockerCmdExec<LogContainerCmd, Frame> implements
        LogContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogContainerCmdExec.class);

    public LogContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute0(LogContainerCmd command, ResultCallback<Frame> resultCallback) {

        WebTarget webTarget = getBaseResource().path("/containers/{id}/logs").resolveTemplate("id",
                command.getContainerId());

        if (command.getTail() != null) {
            webTarget = webTarget.queryParam("tail", command.getTail());
        }

        if (command.getSince() != null) {
            webTarget = webTarget.queryParam("since", command.getSince());
        }

        webTarget = booleanQueryParam(webTarget, "timestamps", command.hasTimestampsEnabled());
        webTarget = booleanQueryParam(webTarget, "stdout", command.hasStdoutEnabled());
        webTarget = booleanQueryParam(webTarget, "stderr", command.hasStderrEnabled());
        webTarget = booleanQueryParam(webTarget, "follow", command.hasFollowStreamEnabled());

        LOGGER.trace("GET: {}", webTarget);

        webTarget.request().get(resultCallback);

        return null;
    }
}
