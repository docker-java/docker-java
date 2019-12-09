package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.async.JsonStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.GETCallbackNotifier;

import static org.apache.commons.lang.BooleanUtils.isTrue;

public class StatsCmdExec extends AbstrAsyncDockerCmdExec<StatsCmd, Statistics> implements StatsCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatsCmdExec.class);

    public StatsCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected AbstractCallbackNotifier<Statistics> callbackNotifier(StatsCmd command,
            ResultCallback<Statistics> resultCallback) {

        WebTarget webTarget = getBaseResource().path("/containers/{id}/stats").resolveTemplate("id",
                command.getContainerId());

        if (isTrue(command.getNoStream())) {
            webTarget = webTarget.queryParam("stream", "0");
        }

        LOGGER.trace("GET: {}", webTarget);

        return new GETCallbackNotifier<>(new JsonStreamProcessor<>(objectMapper, Statistics.class),
                resultCallback, webTarget.request());
    }
}
