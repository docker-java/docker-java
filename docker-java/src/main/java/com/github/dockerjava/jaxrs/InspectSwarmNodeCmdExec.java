package com.github.dockerjava.jaxrs;


import com.github.dockerjava.api.command.InspectSwarmNodeCmd;
import com.github.dockerjava.api.model.SwarmNode;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class InspectSwarmNodeCmdExec extends AbstrSyncDockerCmdExec<InspectSwarmNodeCmd, SwarmNode>
        implements InspectSwarmNodeCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InspectSwarmNodeCmdExec.class);

    public InspectSwarmNodeCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected SwarmNode execute(InspectSwarmNodeCmd command) {
        WebTarget webResource = getBaseResource().path("/nodes/{id}")
                .resolveTemplate("id", command.getSwarmNodeId());

        LOGGER.debug("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(SwarmNode.class);
    }
}
