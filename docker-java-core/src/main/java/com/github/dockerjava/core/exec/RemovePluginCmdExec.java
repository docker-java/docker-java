package com.github.dockerjava.core.exec;


import com.github.dockerjava.api.command.RemovePluginCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemovePluginCmdExec extends AbstrSyncDockerCmdExec<RemovePluginCmd, Void> implements RemovePluginCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveImageCmdExec.class);

    public RemovePluginCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(RemovePluginCmd command) {
        WebTarget webTarget = getBaseResource().path("/plugins/" + command.getPluginName());

        webTarget = booleanQueryParam(webTarget, "force", command.hasForceEnabled());


        LOGGER.trace("DELETE: {}", webTarget);
        webTarget.request().delete();
        return null;
    }
}
