package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.LoadImageResponseItem;
import com.github.dockerjava.netty.InvocationBuilder;
import com.github.dockerjava.netty.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.WebTarget;

public class LoadImageCmdExec extends AbstrAsyncDockerCmdExec<LoadImageCmd, LoadImageResponseItem> implements
        LoadImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadImageCmdExec.class);

    public LoadImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute0(LoadImageCmd command, ResultCallback<LoadImageResponseItem> resultCallback) {
        WebTarget webResource = getBaseResource().path("/images/load");

        if (command.getQuiet() != null) {
            webResource = webResource.queryParam("quiet", command.getQuiet());
        }

        LOGGER.trace("POST: {}", webResource);
        final InvocationBuilder builder = webResource.request()
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/tar")
                .header("encoding", "gzip");

        builder.post(new TypeReference<LoadImageResponseItem>() {
        }, resultCallback, command.getImageStream());

        return null;
    }
}
