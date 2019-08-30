package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.PruneCmd;
import com.github.dockerjava.api.model.PruneResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import com.github.dockerjava.core.util.FiltersEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                .post(null, new TypeReference<PruneResponse>() { });

        LOGGER.trace("Response: {}", response);

        return response;
    }

}
