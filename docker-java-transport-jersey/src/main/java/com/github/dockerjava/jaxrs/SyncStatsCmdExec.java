package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.SyncStatsCmd;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.DockerClientConfig;


public class SyncStatsCmdExec extends AbstrSyncDockerCmdExec<SyncStatsCmd, Statistics> implements SyncStatsCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncStatsCmdExec.class);

    public SyncStatsCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Statistics execute(SyncStatsCmd command) {
        WebTarget webTarget = getBaseResource().path("/containers/{id}/stats").queryParam("stream", "false").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("GET: {}", webTarget);
        return webTarget.request().accept(MediaType.APPLICATION_JSON).get(new GenericType<Statistics>() {
        });
    }
}
