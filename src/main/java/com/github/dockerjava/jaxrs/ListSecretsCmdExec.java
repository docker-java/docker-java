package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.ListSecretsCmd;
import com.github.dockerjava.api.model.Secret;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.FiltersEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.google.common.net.UrlEscapers.urlPathSegmentEscaper;

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
                    .queryParam("filters", urlPathSegmentEscaper().escape(FiltersEncoder.jsonEncode(command.getFilters())));
        }

        LOGGER.trace("GET: {}", webTarget);
        List<Secret> secrets = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Secret>>() {
                });
        LOGGER.trace("Response: {}", secrets);

        return secrets;
    }
}

