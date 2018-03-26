package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.SaveImagesCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

//CHECKSTYLE:OFF

public class SaveImagesCmdExec extends AbstrSyncDockerCmdExec<SaveImagesCmd, InputStream> implements SaveImagesCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveImagesCmdExec.class);

    public SaveImagesCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    @SuppressFBWarnings("NP")
    protected InputStream execute(SaveImagesCmd command) {
        checkNotNull(command.getImages(), "images was not specified");
        WebTarget webResource = getBaseResource().path("/images/get");
        for (String image: command.getImages()) {
            webResource = webResource.queryParam("names", image);
        }

        LOGGER.trace("GET: {}", webResource);
        Response response = webResource.request().accept(MediaType.APPLICATION_JSON).get();

        return new WrappedResponseInputStream(response);
    }
}
