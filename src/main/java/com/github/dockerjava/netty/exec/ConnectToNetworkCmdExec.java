package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.ConnectToNetworkCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectToNetworkCmdExec extends AbstrSyncDockerCmdExec<ConnectToNetworkCmd, Void>
        implements ConnectToNetworkCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectToNetworkCmdExec.class);

    public ConnectToNetworkCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(ConnectToNetworkCmd command) {

        WebTarget webTarget = getBaseResource().path("/networks/" + command.getNetworkId() + "/connect");

        LOGGER.trace("POST: {}", webTarget);
        webTarget.request().post(command);

        return null;
    }
}
