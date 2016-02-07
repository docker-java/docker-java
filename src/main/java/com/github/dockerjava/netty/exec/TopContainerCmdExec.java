package com.github.dockerjava.netty.exec;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.TopContainerResponse;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

public class TopContainerCmdExec extends AbstrSyncDockerCmdExec<TopContainerCmd, TopContainerResponse> implements
        TopContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopContainerCmdExec.class);

    public TopContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected TopContainerResponse execute(TopContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/top").resolveTemplate("id",
                command.getContainerId());

        if (!StringUtils.isEmpty(command.getPsArgs())) {
            webResource = webResource.queryParam("ps_args", command.getPsArgs());
        }

        LOGGER.trace("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(new TypeReference<TopContainerResponse>() {
        });
    }

}
