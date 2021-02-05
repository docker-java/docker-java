package com.github.dockerjava.core.exec;

import com.github.dockerjava.api.command.RemoveConfigCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveConfigCmdExec extends AbstrSyncDockerCmdExec<RemoveConfigCmd, Void> implements RemoveConfigCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveConfigCmdExec.class);

    public RemoveConfigCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RemoveConfigCmd command) {
        WebTarget webTarget = getBaseResource().path("/configs/" + command.getConfigId());

        LOGGER.trace("DELETE: {}", webTarget);
        webTarget.request().accept(MediaType.APPLICATION_JSON).delete();

        return null;
    }

}
