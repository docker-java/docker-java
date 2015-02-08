package com.github.dockerjava.jaxrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.command.SaveImageCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PushEventStreamItem;
import jersey.repackaged.com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import static javax.ws.rs.client.Entity.entity;

public class SaveImageCmdExec extends AbstrDockerCmdExec<SaveImageCmd, InputStream> implements SaveImageCmd.Exec {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(PushImageCmdExec.class);

    public SaveImageCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected InputStream execute(SaveImageCmd command) {
        WebTarget webResource = getBaseResource().path("/images/" + command.getName() + "/get")
                .queryParam("tag", command.getTag());

        final String registryAuth = registryAuth(command.getAuthConfig());
        LOGGER.trace("GET: {}", webResource);
        InputStream is =  webResource
                .request()
                .header("X-Registry-Auth", registryAuth)
                .accept(MediaType.APPLICATION_JSON)
                .get().readEntity(InputStream.class);

        return is;
    }
}
