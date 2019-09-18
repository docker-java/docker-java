package com.github.dockerjava.jaxrs;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;

import com.github.dockerjava.api.command.DockerCmd;
import com.github.dockerjava.api.command.DockerCmdSyncExec;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.core.DockerClientConfig;

public abstract class AbstrSyncDockerCmdExec<CMD_T extends DockerCmd<RES_T>, RES_T> extends AbstrDockerCmdExec
        implements DockerCmdSyncExec<CMD_T, RES_T> {

    public AbstrSyncDockerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    public RES_T exec(CMD_T command) {
        // this hack works because of ResponseStatusExceptionFilter
        try (CMD_T cmd = command) {
            try {
                return execute(cmd);
            } catch (ProcessingException e) {
                if (e.getCause() instanceof DockerException) {
                    throw (DockerException) e.getCause();
                } else {
                    throw e;
                }
            }
        }
    }

    protected abstract RES_T execute(CMD_T command);
}
