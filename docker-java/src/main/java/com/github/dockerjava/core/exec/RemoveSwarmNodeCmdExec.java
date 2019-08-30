package com.github.dockerjava.core.exec;

import com.github.dockerjava.api.command.RemoveSwarmNodeCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RemoveSwarmNodeCmdExec extends AbstrSyncDockerCmdExec<RemoveSwarmNodeCmd, Void> implements
        RemoveSwarmNodeCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.github.dockerjava.jaxrs.RemoveSwarmNodeCmdExec.class);

    public RemoveSwarmNodeCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RemoveSwarmNodeCmd command) {
        WebTarget webTarget = getBaseResource().path("/nodes/" + command.getSwarmNodeId());

        webTarget = booleanQueryParam(webTarget, "force", command.hasForceEnabled());

        LOGGER.trace("DELETE: {}", webTarget);
        webTarget.request().accept(MediaType.APPLICATION_JSON).delete();

        return null;
    }
}
