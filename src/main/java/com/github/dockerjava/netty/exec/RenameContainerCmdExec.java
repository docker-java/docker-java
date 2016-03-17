package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.RenameContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rename a container.
 * Created by lujian on 16/3/17.
 */
public class RenameContainerCmdExec extends AbstrSyncDockerCmdExec<RenameContainerCmd, Void> implements RenameContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RenameContainerCmdExec.class);

    public RenameContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RenameContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/rename").resolveTemplate("id", command.getContainerId())
                .queryParam("name", command.getName());

        LOGGER.trace("POST: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON).post(null);

        return null;
    }
}
