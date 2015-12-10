package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.core.DockerClientConfig;

public class RemoveImageCmdExec extends AbstrSyncDockerCmdExec<RemoveImageCmd, Void> implements RemoveImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveImageCmdExec.class);

    public RemoveImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RemoveImageCmd command) {
        WebTarget webTarget = getBaseResource().path("/images/" + command.getImageId());

        webTarget = booleanQueryParam(webTarget, "force", command.hasForceEnabled());
        webTarget = booleanQueryParam(webTarget, "noprune", command.hasNoPruneEnabled());

        LOGGER.trace("DELETE: {}", webTarget);
        webTarget.request().delete().close();

        return null;
    }

}
