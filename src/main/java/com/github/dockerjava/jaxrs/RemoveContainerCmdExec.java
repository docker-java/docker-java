package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;

public class RemoveContainerCmdExec extends AbstrSyncDockerCmdExec<RemoveContainerCmd, Void> implements
        RemoveContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveContainerCmdExec.class);

    public RemoveContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RemoveContainerCmd command) {
        WebTarget webTarget = getBaseResource().path("/containers/" + command.getContainerId());

        webTarget = booleanQueryParam(webTarget, "v", command.hasRemoveVolumesEnabled());
        webTarget = booleanQueryParam(webTarget, "force", command.hasForceEnabled());

        LOGGER.trace("DELETE: {}", webTarget);
        webTarget.request().accept(MediaType.APPLICATION_JSON).delete().close();

        return null;
    }

}
