package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class RemoveContainerCmdExec extends AbstrSyncDockerCmdExec<RemoveContainerCmd, Void> implements
        RemoveContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveContainerCmdExec.class);

    public RemoveContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RemoveContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/" + command.getContainerId())
                .queryParam("v", command.hasRemoveVolumesEnabled() ? "1" : "0")
                .queryParam("force", command.hasForceEnabled() ? "1" : "0");

        LOGGER.trace("DELETE: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON).delete().close();

        return null;
    }

}
