package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.LeaveSwarmCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeaveSwarmCmdExec extends AbstrSyncDockerCmdExec<LeaveSwarmCmd, Void> implements LeaveSwarmCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveSwarmCmdExec.class);

    public LeaveSwarmCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(LeaveSwarmCmd command) {
        WebTarget webTarget = getBaseResource().path("/swarm/leave");

        webTarget = booleanQueryParam(webTarget, "force", command.hasForceEnabled());

        LOGGER.trace("POST: {}", webTarget);
        webTarget.request().accept(MediaType.APPLICATION_JSON)
                .post(null);

        return null;
    }
}
