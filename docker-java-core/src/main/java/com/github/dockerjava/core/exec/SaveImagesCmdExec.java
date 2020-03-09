package com.github.dockerjava.core.exec;

import com.github.dockerjava.api.command.SaveImagesCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

public class SaveImagesCmdExec extends AbstrSyncDockerCmdExec<SaveImagesCmd, InputStream> implements SaveImagesCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveImagesCmdExec.class);

    public SaveImagesCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InputStream execute(SaveImagesCmd command) {

        final List<SaveImagesCmd.TaggedImage> images = command.getImages();
        if (images.isEmpty()) {
            LOGGER.warn("No images specified for " + SaveImagesCmd.class.getName() + ".");
        }
        final ImmutableSet.Builder<String> queryParamSet = ImmutableSet.builder();
        for (SaveImagesCmd.TaggedImage image : images) {
            queryParamSet.add(image.asString());
        }
        final WebTarget webResource = getBaseResource()
            .path("/images/get")
            .queryParamsSet("names", queryParamSet.build());

        LOGGER.trace("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get();
    }
}
