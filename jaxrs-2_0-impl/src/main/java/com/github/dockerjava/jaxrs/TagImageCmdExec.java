package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.command.TagImageCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;

public class TagImageCmdExec extends AbstrSyncDockerCmdExec<TagImageCmd, Void> implements TagImageCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagImageCmdExec.class);

    public TagImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected Void execute(TagImageCmd command) {
        WebTarget webResource = getBaseResource().path("/images/" + command.getImageId() + "/tag")
                .queryParam("repo", command.getRepository()).queryParam("tag", command.getTag())
                .queryParam("force", command.hasForceEnabled() ? "1" : "0");

        LOGGER.trace("POST: {}", webResource);
        webResource.request().post(null).close();
        return null;
    }

}
