package com.github.dockerjava.core.exec;


import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.InspectSwarmCmd;
import com.github.dockerjava.api.model.Swarm;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InspectSwarmCmdExec extends AbstrSyncDockerCmdExec<InspectSwarmCmd, Swarm>
        implements InspectSwarmCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InspectSwarmCmdExec.class);

    public InspectSwarmCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Swarm execute(InspectSwarmCmd command) {
        WebTarget webResource = getBaseResource().path("/swarm");

        LOGGER.debug("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .get(new TypeReference<Swarm>() {
                });
    }
}
