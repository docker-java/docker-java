package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.RemoveVolumeCmd;
import com.github.dockerjava.core.DockerClientConfig;

public class RemoveVolumeCmdExec extends AbstrSyncDockerCmdExec<RemoveVolumeCmd, Void> implements
        RemoveVolumeCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveVolumeCmdExec.class);

    public RemoveVolumeCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RemoveVolumeCmd command) {
        WebTarget webTarget = getBaseResource().path("/volumes/" + command.getName());

        LOGGER.trace("DELETE: {}", webTarget);
        webTarget.request().accept(MediaType.APPLICATION_JSON).delete().close();

        return null;
    }
}
