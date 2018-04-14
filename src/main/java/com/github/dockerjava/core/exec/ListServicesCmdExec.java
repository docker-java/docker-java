package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.ListServicesCmd;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.FiltersEncoder;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ListServicesCmdExec extends AbstrSyncDockerCmdExec<ListServicesCmd, List<Service>> implements
        ListServicesCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListServicesCmdExec.class);

    public ListServicesCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<Service> execute(ListServicesCmd command) {
        WebTarget webTarget = getBaseResource().path("/services");

        if (command.getFilters() != null && !command.getFilters().isEmpty()) {
            webTarget = webTarget
                    .queryParam("filters", FiltersEncoder.jsonEncode(command.getFilters()));
        }

        LOGGER.trace("GET: {}", webTarget);

        List<Service> services = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new TypeReference<List<Service>>() {
                });

        LOGGER.trace("Response: {}", services);

        return services;
    }

}
