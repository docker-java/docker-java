package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.RemoveSecretCmd;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class RemoveSecretCmdExec extends AbstrSyncDockerCmdExec<RemoveSecretCmd, Void> implements
        RemoveSecretCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveSecretCmdExec.class);

    public RemoveSecretCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RemoveSecretCmd command) {
        WebTarget webTarget = getBaseResource().path("/secrets/" + command.getSecretId());

        LOGGER.trace("DELETE: {}", webTarget);
        webTarget.request().accept(MediaType.APPLICATION_JSON).delete().close();

        return null;
    }

}
