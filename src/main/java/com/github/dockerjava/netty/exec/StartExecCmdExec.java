package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.StartExecCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

public class StartExecCmdExec extends AbstrAsyncDockerCmdExec<StartExecCmd, Frame> implements StartExecCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartExecCmdExec.class);

    public StartExecCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute0(StartExecCmd command, ResultCallback<Frame> resultCallback) {
        WebTarget webTarget = getBaseResource().path("/exec/{id}/start").resolveTemplate("id", command.getExecId());

        webTarget.request().accept(MediaType.APPLICATION_JSON).post(command, command.getStdin(), resultCallback);

        return null;
    }
}
