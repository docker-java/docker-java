package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.ListSwarmNodesCmd;
import com.github.dockerjava.api.model.SwarmNode;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.FiltersEncoder;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ListSwarmNodesCmdExec extends AbstrSyncDockerCmdExec<ListSwarmNodesCmd, List<SwarmNode>> implements
        ListSwarmNodesCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListSwarmNodesCmdExec.class);

    public ListSwarmNodesCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }


    @Override
    protected List<SwarmNode> execute(ListSwarmNodesCmd command) {
        WebTarget webTarget = getBaseResource().path("/nodes");

        if (command.getFilters() != null && !command.getFilters().isEmpty()) {
            webTarget = webTarget
                    .queryParam("filters", FiltersEncoder.jsonEncode(command.getFilters()));
        }

        LOGGER.trace("GET: {}", webTarget);

        List<SwarmNode> nodes = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new TypeReference<List<SwarmNode>>() {
                });

        LOGGER.trace("Response: {}", nodes);

        return nodes;
    }
}
