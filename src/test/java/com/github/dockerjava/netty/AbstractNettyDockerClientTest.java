package com.github.dockerjava.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.TestDockerCmdExecFactory;

public abstract class AbstractNettyDockerClientTest extends AbstractDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractNettyDockerClientTest.class);

    @Override
    protected TestDockerCmdExecFactory initTestDockerCmdExecFactory() {
        return new TestDockerCmdExecFactory(new NettyDockerCmdExecFactory());
    }
}
