package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.github.dockerjava.api.command.RenameContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
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
