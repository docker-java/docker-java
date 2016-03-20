package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.RenameContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenameContainerCmdExec extends AbstrSyncDockerCmdExec<RenameContainerCmd, Void>
        implements RenameContainerCmd.Exec {
    private static final Logger LOG = LoggerFactory.getLogger(RenameContainerCmdExec.class);

    public RenameContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RenameContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/rename")
                .resolveTemplate("id", command.getContainerId())
                .queryParam("name", command.getName());

        LOG.trace("POST: {}", webResource);
        webResource.request().accept(MediaType.APPLICATION_JSON).post(null);

        return null;
    }
}
