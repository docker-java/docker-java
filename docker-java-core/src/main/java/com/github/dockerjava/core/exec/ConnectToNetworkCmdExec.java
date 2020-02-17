package com.github.dockerjava.core.exec;

import com.github.dockerjava.api.command.ConnectToNetworkCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
        try {
            webTarget.request().post(command).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
