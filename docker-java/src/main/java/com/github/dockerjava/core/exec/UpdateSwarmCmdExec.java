package com.github.dockerjava.core.exec;


import com.github.dockerjava.api.command.UpdateSwarmCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateSwarmCmdExec extends AbstrSyncDockerCmdExec<UpdateSwarmCmd, Void>
        implements UpdateSwarmCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSwarmCmdExec.class);

    public UpdateSwarmCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(UpdateSwarmCmd command) {
        WebTarget webResource = getBaseResource().path("/swarm/update")
                .queryParam("version", command.getVersion());
        webResource = booleanQueryParam(webResource, "rotateManagerToken", command.getRotateManagerToken());
        webResource = booleanQueryParam(webResource, "rotateWorkertoken", command.getRotateWorkerToken());

        LOGGER.trace("POST: {} ", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(command.getSwarmSpec());
        return null;
    }
}
