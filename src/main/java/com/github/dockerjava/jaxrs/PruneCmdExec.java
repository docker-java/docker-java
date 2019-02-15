package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.PruneCmd;
import com.github.dockerjava.api.model.PruneResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.FiltersEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

public class PruneCmdExec extends AbstrSyncDockerCmdExec<PruneCmd, PruneResponse> implements PruneCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(PruneCmdExec.class);

    public PruneCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected PruneResponse execute(PruneCmd command) {
        WebTarget webTarget = getBaseResource().path(command.getApiPath());

        if (command.getFilters() != null && !command.getFilters().isEmpty()) {
            webTarget = webTarget.queryParam("filters", FiltersEncoder.jsonEncode(command.getFilters()));
        }

        LOGGER.trace("POST: {}", webTarget);

        PruneResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .post(null, new GenericType<PruneResponse>() {});
        LOGGER.trace("Response: {}", response);

        return response;
    }

}
