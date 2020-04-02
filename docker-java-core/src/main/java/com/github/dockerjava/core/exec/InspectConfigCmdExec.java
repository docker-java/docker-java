package com.github.dockerjava.core.exec;


import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.InspectConfigCmd;
import com.github.dockerjava.api.model.Config;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InspectConfigCmdExec extends AbstrSyncDockerCmdExec<InspectConfigCmd, Config>
        implements InspectConfigCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InspectConfigCmdExec.class);

    public InspectConfigCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Config execute(InspectConfigCmd command) {
        WebTarget webResource = getBaseResource().path("/configs/{id}")
                .resolveTemplate("id", command.getConfigId());

        LOGGER.debug("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON)
                .get(new TypeReference<Config>() {
                });
    }
}
