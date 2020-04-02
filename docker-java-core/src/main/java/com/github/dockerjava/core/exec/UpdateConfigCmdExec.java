package com.github.dockerjava.core.exec;

import com.github.dockerjava.api.command.UpdateConfigCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateConfigCmdExec extends AbstrSyncDockerCmdExec<UpdateConfigCmd, Void>
        implements UpdateConfigCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateConfigCmdExec.class);

    public UpdateConfigCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(UpdateConfigCmd command) {
        WebTarget webResource = getBaseResource().path("/configs/{id}/update")
                .resolveTemplate("id", command.getConfigId());

        LOGGER.trace("POST: {} ", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON).post(command.getConfigSpec());
        return null;
    }
}
