package com.github.dockerjava.core.exec;

import com.github.dockerjava.api.command.UpdateServiceCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
        try {
            webResource.request().accept(MediaType.APPLICATION_JSON).post(command.getServiceSpec()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
