package com.github.dockerjava.core.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;

public class ExecStartCmdExec extends AbstrAsyncDockerCmdExec<ExecStartCmd, Frame> implements ExecStartCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecStartCmdExec.class);

    public ExecStartCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute0(ExecStartCmd command, ResultCallback<Frame> resultCallback) {
        WebTarget webTarget = getBaseResource().path("/exec/{id}/start").resolveTemplate("id", command.getExecId());

        webTarget.request().accept(MediaType.APPLICATION_JSON).post(command, command.getStdin(), resultCallback);

        return null;
    }
}
