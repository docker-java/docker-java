package com.github.dockerjava.jaxrs;

import java.io.IOException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.DockerCmd;
import com.github.dockerjava.api.command.DockerCmdSyncExec;

public abstract class AbstrSyncDockerCmdExec<CMD_T extends DockerCmd<RES_T>, RES_T> extends AbstrDockerCmdExec
        implements DockerCmdSyncExec<CMD_T, RES_T> {

    public AbstrSyncDockerCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    public RES_T exec(CMD_T command) {
        // this hack works because of ResponseStatusExceptionFilter
        RES_T result;
        try {
            result = execute(command);

        } catch (ProcessingException e) {
            if (e.getCause() instanceof DockerException) {
                throw (DockerException) e.getCause();
            } else {
                throw e;
            }
        } finally {
            try {
                command.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    protected abstract RES_T execute(CMD_T command);
}
