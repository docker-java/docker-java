package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.WebTarget;

/**
 *
 * @author Marcus Linke
 *
 */
public class InfoCmdExec implements InfoCmd.Exec {

    private WebTarget webResource;

    public InfoCmdExec(WebTarget webResource, DockerClientConfig dockerClientConfig) {
        this.webResource = webResource;
    }

    @Override
    public Info exec(InfoCmd command) {
        return webResource.path("info").request().get(new TypeReference<Info>() {
        });
    }

}
