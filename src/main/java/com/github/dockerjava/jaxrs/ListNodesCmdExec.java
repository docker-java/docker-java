package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.ListNodesCmd;
import com.github.dockerjava.api.model.SwarmNode;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.FiltersEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.google.common.net.UrlEscapers.urlPathSegmentEscaper;

public class ListNodesCmdExec extends AbstrSyncDockerCmdExec<ListNodesCmd, List<SwarmNode>> implements
        ListNodesCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListNodesCmdExec.class);

    public ListNodesCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<SwarmNode> execute(ListNodesCmd command) {
        WebTarget webTarget = getBaseResource().path("/nodes");

        if (command.getFilters() != null && !command.getFilters().isEmpty()) {
            webTarget = webTarget
                    .queryParam("filters", urlPathSegmentEscaper().escape(FiltersEncoder.jsonEncode(command.getFilters())));
        }

        LOGGER.trace("GET: {}", webTarget);
        List<SwarmNode> nodes = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<SwarmNode>>() {
                });
        LOGGER.trace("Response: {}", nodes);

        return nodes;
    }
}
