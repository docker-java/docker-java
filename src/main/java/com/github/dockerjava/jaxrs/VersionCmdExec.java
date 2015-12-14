package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.model.Version;
import com.github.dockerjava.core.DockerClientConfig;

public class VersionCmdExec extends AbstrSyncDockerCmdExec<VersionCmd, Version> implements VersionCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionCmdExec.class);

    public VersionCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Version execute(VersionCmd command) {
        WebTarget webResource = getBaseResource().path("/version");

        LOGGER.trace("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(Version.class);
    }

}
