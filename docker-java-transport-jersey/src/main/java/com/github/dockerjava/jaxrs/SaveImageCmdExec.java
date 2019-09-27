package com.github.dockerjava.jaxrs;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.SaveImageCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;
import com.google.common.base.Strings;

public class SaveImageCmdExec extends AbstrSyncDockerCmdExec<SaveImageCmd, InputStream> implements SaveImageCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveImageCmdExec.class);

    public SaveImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InputStream execute(SaveImageCmd command) {
        // If tag is present, only tar the specific image
        // else tar all the images with the same name
        String name = command.getName();
        if (!Strings.isNullOrEmpty(command.getTag())) {
            name += ":" + command.getTag();
        }
        WebTarget webResource = getBaseResource().path("/images/" + name + "/get");

        LOGGER.trace("GET: {}", webResource);
        Response response = webResource.request().accept(MediaType.APPLICATION_JSON).get();

        return new WrappedResponseInputStream(response);
    }
}
