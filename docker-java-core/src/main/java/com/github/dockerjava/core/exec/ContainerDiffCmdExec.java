package com.github.dockerjava.core.exec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.ContainerDiffCmd;
import com.github.dockerjava.api.model.ChangeLog;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;

public class ContainerDiffCmdExec extends AbstrSyncDockerCmdExec<ContainerDiffCmd, List<ChangeLog>> implements
        ContainerDiffCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerDiffCmdExec.class);

    public ContainerDiffCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<ChangeLog> execute(ContainerDiffCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/changes").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(new TypeReference<List<ChangeLog>>() {
        });
    }

}
