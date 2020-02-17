package com.github.dockerjava.core.exec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.FiltersEncoder;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;

public class ListImagesCmdExec extends AbstrSyncDockerCmdExec<ListImagesCmd, List<Image>> implements ListImagesCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListImagesCmdExec.class);

    public ListImagesCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<Image> execute(ListImagesCmd command) {
        WebTarget webTarget = getBaseResource().path("/images/json");

        webTarget = booleanQueryParam(webTarget, "all", command.hasShowAllEnabled());

        if (command.getFilters() != null && !command.getFilters().isEmpty()) {
            webTarget = webTarget.queryParam("filters", FiltersEncoder.jsonEncode(command.getFilters()));
        }

       if (command.getImageNameFilter() != null) {
            webTarget = webTarget.queryParam("filter", command.getImageNameFilter());
       }

        LOGGER.trace("GET: {}", webTarget);

        List<Image> images = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new TypeReference<List<Image>>() {
                });

        LOGGER.trace("Response: {}", images);

        return images;
    }
}
