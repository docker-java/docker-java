package com.github.dockerjava.jaxrs.jersey.jaxrs;

import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.command.RemoveImageCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;

public class RemoveImageCmdExec extends AbstrSyncDockerCmdExec<RemoveImageCmd, Void> implements RemoveImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveImageCmdExec.class);

    public RemoveImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RemoveImageCmd command) {
        WebTarget webResource = getBaseResource().path("/images/" + command.getImageId())
                .queryParam("force", command.hasForceEnabled() ? "1" : "0")
                .queryParam("noprune", command.hasNoPruneEnabled() ? "1" : "0");

        LOGGER.trace("DELETE: {}", webResource);
        webResource.request().delete().close();

        return null;
    }

}
