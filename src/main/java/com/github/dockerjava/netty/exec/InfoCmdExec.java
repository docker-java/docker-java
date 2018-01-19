package com.github.dockerjava.netty.exec;

import com.github.dockerjava.core.DockerClientConfig;

import javax.ws.rs.client.WebTarget;

/**
 * http://stackoverflow.com/questions/33296749/netty-connect-to-unix-domain- socket-failed http://netty.io/wiki/native-transports.html
 * https://github.com/netty/netty/blob/master/example/src/main/java/io/netty/ example/http/snoop/HttpSnoopClient.java
 *
 * @author Marcus Linke
 */
public class InfoCmdExec extends com.github.dockerjava.jaxrs.InfoCmdExec {

    public InfoCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }
}