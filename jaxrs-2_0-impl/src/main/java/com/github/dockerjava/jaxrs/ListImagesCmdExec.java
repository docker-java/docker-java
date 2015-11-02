package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Image;
import com.google.common.net.UrlEscapers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class ListImagesCmdExec extends AbstrSyncDockerCmdExec<ListImagesCmd, List<Image>> implements ListImagesCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListImagesCmdExec.class);

    public ListImagesCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<Image> execute(ListImagesCmd command) {
        WebTarget webResource = getBaseResource().path("/images/json").queryParam("all",
                command.hasShowAllEnabled() ? "1" : "0");

        if (command.getFilters() != null)
            webResource = webResource.queryParam("filters", UrlEscapers.urlPathSegmentEscaper().escape(command.getFilters()));

        LOGGER.trace("GET: {}", webResource);

        List<Image> images = webResource.request().accept(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Image>>() {
                });
        LOGGER.trace("Response: {}", images);

        return images;
    }

}
