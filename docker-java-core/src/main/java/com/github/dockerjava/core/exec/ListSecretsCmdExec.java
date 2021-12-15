package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.ListSecretsCmd;
import com.github.dockerjava.api.model.Secret;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import com.github.dockerjava.core.util.FiltersEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ListSecretsCmdExec extends AbstrSyncDockerCmdExec<ListSecretsCmd, List<Secret>> implements
        ListSecretsCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListSecretsCmdExec.class);

    public ListSecretsCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<Secret> execute(ListSecretsCmd command) {
        WebTarget webTarget = getBaseResource().path("/secrets");

        if (command.getFilters() != null && !command.getFilters().isEmpty()) {
            webTarget = webTarget
                    .queryParam("filters", FiltersEncoder.jsonEncode(command.getFilters()));
        }

        LOGGER.trace("GET: {}", webTarget);

        List<Secret> secrets = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new TypeReference<List<Secret>>() {
                });

        LOGGER.trace("Response: {}", secrets);

        return secrets;
    }

}
