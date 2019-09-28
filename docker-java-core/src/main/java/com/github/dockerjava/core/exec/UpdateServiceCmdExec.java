package com.github.dockerjava.core.exec;

import com.github.dockerjava.api.command.UpdateServiceCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Update service settings.
 */
public class UpdateServiceCmdExec extends AbstrSyncDockerCmdExec<UpdateServiceCmd, Void>
        implements UpdateServiceCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateServiceCmdExec.class);

    public UpdateServiceCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(UpdateServiceCmd command) {
        WebTarget webResource = getBaseResource().path("/services/{id}/update")
                .resolveTemplate("id", command.getServiceId())
                .queryParam("version", command.getVersion());

        LOGGER.trace("POST: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON)
                .post(command.getServiceSpec());
        return null;
    }
}
