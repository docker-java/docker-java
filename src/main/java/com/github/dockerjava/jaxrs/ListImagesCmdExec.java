package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.google.common.net.UrlEscapers.urlPathSegmentEscaper;

public class ListImagesCmdExec extends AbstrSyncDockerCmdExec<ListImagesCmd, List<Image>> implements ListImagesCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListImagesCmdExec.class);

    public ListImagesCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<Image> execute(ListImagesCmd command) {
        WebTarget webTarget = getBaseResource().path("/images/json");

        webTarget = booleanQueryParam(webTarget, "all", command.hasShowAllEnabled());

        if (command.getFilters() != null)
            webTarget = webTarget.queryParam("filters", urlPathSegmentEscaper().escape(command.getFilters()));

        LOGGER.trace("GET: {}", webTarget);

        List<Image> images = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Image>>() {
                });
        LOGGER.trace("Response: {}", images);

        return images;
    }

}
