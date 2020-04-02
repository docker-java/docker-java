package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.ListConfigsCmd;
import com.github.dockerjava.api.model.Config;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import com.github.dockerjava.core.util.FiltersEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ListConfigsCmdExec extends AbstrSyncDockerCmdExec<ListConfigsCmd, List<Config>> implements
        ListConfigsCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListConfigsCmdExec.class);

    public ListConfigsCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<Config> execute(ListConfigsCmd command) {
        WebTarget webTarget = getBaseResource().path("/configs");

        if (command.getFilters() != null && !command.getFilters().isEmpty()) {
            webTarget = webTarget
                    .queryParam("filters", FiltersEncoder.jsonEncode(command.getFilters()));
        }

        LOGGER.trace("GET: {}", webTarget);

        List<Config> secrets = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new TypeReference<List<Config>>() {
                });

        LOGGER.trace("Response: {}", secrets);

        return secrets;
    }

}
