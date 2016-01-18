package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.ListNetworksCmd;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.FiltersEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import java.util.List;

import static com.google.common.net.UrlEscapers.urlPathSegmentEscaper;

public class ListNetworksCmdExec extends AbstrSyncDockerCmdExec<ListNetworksCmd, List<Network>> implements
        ListNetworksCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListNetworksCmdExec.class);

    public ListNetworksCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<Network> execute(ListNetworksCmd command) {
        WebTarget webTarget = getBaseResource().path("/networks");

        if (command.getFilters() != null && !command.getFilters().isEmpty()) {
            webTarget = webTarget
                    .queryParam("filters", urlPathSegmentEscaper().escape(FiltersEncoder.jsonEncode(command.getFilters())));
        }

        LOGGER.trace("GET: {}", webTarget);
        List<Network> networks = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Network>>() {
                });
        LOGGER.trace("Response: {}", networks);

        return networks;
    }

}
