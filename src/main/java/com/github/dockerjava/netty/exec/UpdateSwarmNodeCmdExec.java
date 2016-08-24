package com.github.dockerjava.netty.exec;


import com.github.dockerjava.api.command.UpdateSwarmNodeCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Update swarmNode spec
 */
public class UpdateSwarmNodeCmdExec extends AbstrSyncDockerCmdExec<UpdateSwarmNodeCmd, Void>
        implements UpdateSwarmNodeCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSwarmNodeCmdExec.class);

    public UpdateSwarmNodeCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(UpdateSwarmNodeCmd command) {
        WebTarget webResource = getBaseResource().path("/nodes/{id}/update")
                .resolveTemplate("id", command.getSwarmNodeId());

        LOGGER.trace("POST: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(command.getSwarmNodeSpec());
        return null;
    }
}
