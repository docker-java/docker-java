package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.FiltersEncoder;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ListContainersCmdExec extends AbstrSyncDockerCmdExec<ListContainersCmd, List<Container>> implements
        ListContainersCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListContainersCmdExec.class);

    public ListContainersCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<Container> execute(ListContainersCmd command) {
        WebTarget webTarget = getBaseResource().path("/containers/json").queryParam("since", command.getSinceId())
                .queryParam("before", command.getBeforeId());

        webTarget = booleanQueryParam(webTarget, "all", command.hasShowAllEnabled());
        webTarget = booleanQueryParam(webTarget, "size", command.hasShowSizeEnabled());

        if (command.getLimit() != null && command.getLimit() >= 0) {
            webTarget = webTarget.queryParam("limit", String.valueOf(command.getLimit()));
        }

        if (command.getFilters() != null && !command.getFilters().isEmpty()) {
            webTarget = webTarget
                    .queryParam("filters", FiltersEncoder.jsonEncode(command.getFilters()));
        }

        LOGGER.trace("GET: {}", webTarget);

        List<Container> containers = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new TypeReference<List<Container>>() {
                });

        LOGGER.trace("Response: {}", containers);

        return containers;
    }

}
