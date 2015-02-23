package com.github.dockerjava.jaxrs;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.SaveImageCmd;

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
