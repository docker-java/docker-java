package com.github.dockerjava.jaxrs;

import static com.google.common.net.UrlEscapers.urlPathSegmentEscaper;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.ListVolumesCmd;
import com.github.dockerjava.api.command.ListVolumesResponse;
import com.github.dockerjava.core.DockerClientConfig;

public class ListVolumesCmdExec extends AbstrSyncDockerCmdExec<ListVolumesCmd, ListVolumesResponse> implements ListVolumesCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListVolumesCmdExec.class);

    public ListVolumesCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected ListVolumesResponse execute(ListVolumesCmd command) {
        WebTarget webTarget = getBaseResource().path("/volumes");

        if (command.getFilters() != null)
            webTarget = webTarget.queryParam("filters", urlPathSegmentEscaper().escape(command.getFilters()));

        LOGGER.trace("GET: {}", webTarget);

        return webTarget.request().accept(MediaType.APPLICATION_JSON).get(ListVolumesResponse.class);
    }
}
