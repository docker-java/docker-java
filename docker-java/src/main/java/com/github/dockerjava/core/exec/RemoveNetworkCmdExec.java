package com.github.dockerjava.core.exec;

import com.github.dockerjava.api.command.RemoveNetworkCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveNetworkCmdExec extends AbstrSyncDockerCmdExec<RemoveNetworkCmd, Void> implements
        RemoveNetworkCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveNetworkCmdExec.class);

    public RemoveNetworkCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RemoveNetworkCmd command) {
        WebTarget webTarget = getBaseResource().path("/networks/" + command.getNetworkId());

        LOGGER.trace("DELETE: {}", webTarget);
        webTarget.request().accept(MediaType.APPLICATION_JSON).delete();

        return null;
    }

}
