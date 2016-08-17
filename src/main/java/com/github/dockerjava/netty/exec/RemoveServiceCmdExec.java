package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.RemoveServiceCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveServiceCmdExec extends AbstrSyncDockerCmdExec<RemoveServiceCmd, Void> implements
        RemoveServiceCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveServiceCmdExec.class);

    public RemoveServiceCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RemoveServiceCmd command) {
        WebTarget webTarget = getBaseResource().path("/services/" + command.getServiceId());

        LOGGER.trace("DELETE: {}", webTarget);
        webTarget.request().accept(MediaType.APPLICATION_JSON).delete();

        return null;
    }

}
