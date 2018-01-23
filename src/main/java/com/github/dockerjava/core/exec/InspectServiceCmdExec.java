package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.InspectServiceCmd;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InspectServiceCmdExec extends AbstrSyncDockerCmdExec<InspectServiceCmd, Service>
        implements InspectServiceCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InspectServiceCmdExec.class);

    public InspectServiceCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Service execute(InspectServiceCmd command) {
        WebTarget webResource = getBaseResource().path("/services/{id}")
                                                 .resolveTemplate("id", command.getServiceId());

        LOGGER.debug("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(new TypeReference<Service>() { });
    }

}
