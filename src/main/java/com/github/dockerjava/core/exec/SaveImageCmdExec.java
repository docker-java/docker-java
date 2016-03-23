package com.github.dockerjava.core.exec;

import com.github.dockerjava.api.command.SaveImageCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class SaveImageCmdExec extends AbstrSyncDockerCmdExec<SaveImageCmd, InputStream> implements SaveImageCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveImageCmdExec.class);

    public SaveImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InputStream execute(SaveImageCmd command) {

        String name = command.getName();
        if (!Strings.isNullOrEmpty(command.getTag())) {
            name += ":" + command.getTag();
        }

        WebTarget webResource = getBaseResource().
                path("/images/" + name + "/get");

        LOGGER.trace("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get();
    }
}
