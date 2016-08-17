package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.RemoveSwarmNodeCmd;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class RemoveSwarmNodeCmdExec extends AbstrSyncDockerCmdExec<RemoveSwarmNodeCmd, Void> implements
        RemoveSwarmNodeCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveSwarmNodeCmdExec.class);

    public RemoveSwarmNodeCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RemoveSwarmNodeCmd command) {
        WebTarget webTarget = getBaseResource().path("/nodes/" + command.getSwarmNodeId());

        webTarget = booleanQueryParam(webTarget, "force", command.hasForceEnabled());

        LOGGER.trace("DELETE: {}", webTarget);
        webTarget.request().accept(MediaType.APPLICATION_JSON).delete().close();

        return null;
    }
}
