package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.ListServicesCmd;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.FiltersEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.google.common.net.UrlEscapers.urlPathSegmentEscaper;

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
                    .queryParam("filters", urlPathSegmentEscaper().escape(FiltersEncoder.jsonEncode(command.getFilters())));
        }

        LOGGER.trace("GET: {}", webTarget);
        List<Service> services = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Service>>() {
                });
        LOGGER.trace("Response: {}", services);

        return services;
    }

}
