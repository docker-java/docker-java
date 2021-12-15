package com.github.dockerjava.core.exec;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.LogSwarmObjectCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.WebTarget;

import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogSwarmObjectExec extends AbstrAsyncDockerCmdExec<LogSwarmObjectCmd, Frame> implements
        LogSwarmObjectCmd.Exec {
    private String endpoint = "";
    private static final Logger LOGGER = LoggerFactory.getLogger(LogSwarmObjectExec.class);

    public LogSwarmObjectExec(com.github.dockerjava.core.WebTarget baseResource, DockerClientConfig dockerClientConfig, String endpoint) {
        super(baseResource, dockerClientConfig);
        this.endpoint = endpoint;
    }

    @Override
    protected Void execute0(LogSwarmObjectCmd command, ResultCallback<Frame> resultCallback) {

        WebTarget webTarget = getBaseResource().path("/" + endpoint + "/{id}/logs").resolveTemplate("id", command.getId());

        if (command.getTail() != null) {
            webTarget = webTarget.queryParam("tail", command.getTail());
        } else {
            webTarget = webTarget.queryParam("tail", "all");
        }

        if (command.getSince() != null) {
            webTarget = webTarget.queryParam("since", command.getSince());
        }

        webTarget = booleanQueryParam(webTarget, "timestamps", command.getTimestamps());
        webTarget = booleanQueryParam(webTarget, "stdout", command.getStdout());
        webTarget = booleanQueryParam(webTarget, "stderr", command.getStderr());
        webTarget = booleanQueryParam(webTarget, "follow", command.getFollow());

        LOGGER.trace("GET: {}", webTarget);

        webTarget.request().get(resultCallback);

        return null;
    }
}
