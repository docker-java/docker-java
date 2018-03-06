package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.LogSwarmObjectCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.async.FrameStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.GETCallbackNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;

public class LogSwarmObjectExec extends AbstrAsyncDockerCmdExec<LogSwarmObjectCmd, Frame> implements
        LogSwarmObjectCmd.Exec {

    public LogSwarmObjectExec(WebTarget baseResource, DockerClientConfig dockerClientConfig, String endpoint) {
        super(baseResource, dockerClientConfig);
        this.endpoint = endpoint;
    }

    private String endpoint = "";
    private static final Logger LOGGER = LoggerFactory.getLogger(LogSwarmObjectExec.class);

    @Override
    protected AbstractCallbackNotifier<Frame> callbackNotifier(LogSwarmObjectCmd command, ResultCallback<Frame> resultCallback) {

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
        webTarget = booleanQueryParam(webTarget, "details", command.getDetails());
        LOGGER.trace("GET: {}", webTarget);

        return new GETCallbackNotifier<Frame>(new FrameStreamProcessor(), resultCallback, webTarget.request());
    }
}
