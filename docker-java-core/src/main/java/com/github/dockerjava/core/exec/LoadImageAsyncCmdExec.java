package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.LoadImageAsyncCmd;
import com.github.dockerjava.api.model.LoadResponseItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadImageAsyncCmdExec extends AbstrAsyncDockerCmdExec<LoadImageAsyncCmd, LoadResponseItem> implements LoadImageAsyncCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadImageAsyncCmdExec.class);

    public LoadImageAsyncCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute0(LoadImageAsyncCmd command, ResultCallback<LoadResponseItem> resultCallback) {
        WebTarget webTarget = getBaseResource().path("/images/load");

        LOGGER.trace("POST: {}", webTarget);

        webTarget.request().post(new TypeReference<LoadResponseItem>() { }, resultCallback, command.getImageStream());

        return null;
    }
}
