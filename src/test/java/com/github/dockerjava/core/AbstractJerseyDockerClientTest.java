package com.github.dockerjava.core;

import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;

/**
 * @author Kanstantsin Shautsou
 */
public class AbstractJerseyDockerClientTest extends AbstractDockerClientTest {
    @Override
    protected TestDockerCmdExecFactory initTestDockerCmdExecFactory() {
        return new TestDockerCmdExecFactory(new JerseyDockerCmdExecFactory());
    }
}
