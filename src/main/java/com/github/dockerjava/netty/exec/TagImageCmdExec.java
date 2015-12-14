package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.WebTarget;

public class TagImageCmdExec extends AbstrSyncDockerCmdExec<TagImageCmd, Void> implements TagImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagImageCmdExec.class);

    public TagImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(TagImageCmd command) {
        WebTarget webTarget = getBaseResource().path("/images/" + command.getImageId() + "/tag")
                .queryParam("repo", command.getRepository()).queryParam("tag", command.getTag());

        webTarget = booleanQueryParam(webTarget, "force", command.hasForceEnabled());

        LOGGER.trace("POST: {}", webTarget);
        webTarget.request().post(null);
        return null;
    }

}
