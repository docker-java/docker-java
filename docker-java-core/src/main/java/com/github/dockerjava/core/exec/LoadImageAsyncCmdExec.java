package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.LoadImageAsyncCmd;
import com.github.dockerjava.api.model.LoadImageAsyncResponseItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author luwt
 * @date 2021/1/7.
 */
public class LoadImageAsyncCmdExec extends AbstrAsyncDockerCmdExec<LoadImageAsyncCmd, LoadImageAsyncResponseItem> implements
    LoadImageAsyncCmd.Exec{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadImageAsyncCmdExec.class);


    public LoadImageAsyncCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }


    @Override
    protected Void execute0(LoadImageAsyncCmd command, ResultCallback<LoadImageAsyncResponseItem> resultCallback) {
        WebTarget webResource = getBaseResource().path("/images/load");

        LOGGER.trace("POST: {}", webResource);
        webResource.request().post(new TypeReference<LoadImageAsyncResponseItem>() {
        }, resultCallback, command.getImageStream());
        return null;
    }
}
