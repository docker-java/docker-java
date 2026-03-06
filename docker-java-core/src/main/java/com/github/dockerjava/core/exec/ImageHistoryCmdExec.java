package com.github.dockerjava.core.exec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.ImageHistoryCmd;
import com.github.dockerjava.api.model.ImageHistory;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;

public class ImageHistoryCmdExec extends AbstrSyncDockerCmdExec<ImageHistoryCmd, List<ImageHistory>> implements
        ImageHistoryCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageHistoryCmdExec.class);

    public ImageHistoryCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<ImageHistory> execute(ImageHistoryCmd command) {
        WebTarget webResource = getBaseResource().path("/images/{id}/history").resolveTemplate("id",
                command.getImageId());

        LOGGER.trace("GET: {}", webResource);

        return webResource.request().accept(MediaType.APPLICATION_JSON).get(new TypeReference<List<ImageHistory>>() {
        });
    }

}
