package com.github.dockerjava.netty;

import com.github.dockerjava.core.TestDockerCmdExecFactory;
import com.github.dockerjava.core.command.AbstractSwarmDockerClientTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNettySwarmDockerClientTest extends AbstractSwarmDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractNettySwarmDockerClientTest.class);

    @Override
    protected TestDockerCmdExecFactory initTestDockerCmdExecFactory() {
        return new TestDockerCmdExecFactory(new NettyDockerCmdExecFactory());
    }
}
