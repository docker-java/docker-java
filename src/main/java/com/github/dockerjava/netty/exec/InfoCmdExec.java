package com.github.dockerjava.netty.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.WebTarget;

/**
 * http://stackoverflow.com/questions/33296749/netty-connect-to-unix-domain- socket-failed http://netty.io/wiki/native-transports.html
 * https://github.com/netty/netty/blob/master/example/src/main/java/io/netty/ example/http/snoop/HttpSnoopClient.java
 *
 * @author Marcus Linke
 *
 */
public class InfoCmdExec implements InfoCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfoCmdExec.class);

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
