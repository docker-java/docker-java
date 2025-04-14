package com.github.dockerjava.core.exec;

import java.io.InputStream;

import com.github.dockerjava.api.command.ExportImageCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;

/**
 * Executor for ExportImageCmd.
 */
public class ExportImageCmdExec extends AbstrSyncDockerCmdExec<ExportImageCmd, InputStream> implements ExportImageCmd.Exec {

    public ExportImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected InputStream execute(ExportImageCmd command) {
        WebTarget webTarget = getBaseResource().path("/images/" + command.getImageId() + "/get");
        return webTarget.request().accept(MediaType.APPLICATION_OCTET_STREAM).get();
    }
}