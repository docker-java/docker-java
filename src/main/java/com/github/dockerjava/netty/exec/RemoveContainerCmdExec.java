package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        return webTarget.request().accept(MediaType.APPLICATION_JSON).delete().awaitResult();
    }

}
