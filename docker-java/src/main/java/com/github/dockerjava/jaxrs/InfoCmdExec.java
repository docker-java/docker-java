package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DockerClientConfig;

public class InfoCmdExec extends AbstrSyncDockerCmdExec<InfoCmd, Info> implements InfoCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfoCmdExec.class);

    public InfoCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Info execute(InfoCmd command) {
        WebTarget webResource = getBaseResource().path("/info");

        LOGGER.trace("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(Info.class);
    }

}
