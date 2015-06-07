package com.github.dockerjava.jaxrs;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.SaveImageCmd;
import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;

public class SaveImageCmdExec extends AbstrDockerCmdExec<SaveImageCmd, InputStream> implements SaveImageCmd.Exec {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SaveImageCmdExec.class);

    public SaveImageCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected InputStream execute(SaveImageCmd command) {
        WebTarget webResource = getBaseResource().path("/images/" + command.getImageName() + "/get")
                //.queryParam("tag", command.getTag());
        ;

        LOGGER.trace("GET: {}", webResource);
        Response response =  webResource
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        return new WrappedResponseInputStream(response);
    }
}
