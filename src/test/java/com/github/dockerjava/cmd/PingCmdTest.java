package com.github.dockerjava.cmd;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;
import org.junit.Test;

public class PingCmdTest extends AbstractNettyDockerClientTest {

    @Test
    public void ping() throws DockerException {
        dockerClient.pingCmd().exec();
    }

}
