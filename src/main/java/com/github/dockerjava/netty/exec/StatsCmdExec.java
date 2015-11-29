package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.WebTarget;

public class StatsCmdExec extends AbstrAsyncDockerCmdExec<StatsCmd, Statistics> implements StatsCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatsCmdExec.class);

    public StatsCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute0(StatsCmd command, ResultCallback<Statistics> resultCallback) {

        WebTarget webTarget = getBaseResource().path("/containers/{id}/stats").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("GET: {}", webTarget);

        webTarget.request().get(new TypeReference<Statistics>() {
        }, resultCallback);

        return null;
    }
}
