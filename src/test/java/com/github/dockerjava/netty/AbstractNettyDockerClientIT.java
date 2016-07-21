package com.github.dockerjava.netty;

import com.github.dockerjava.client.AbstractDockerClientIT;
import com.github.dockerjava.core.TestDockerCmdExecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNettyDockerClientIT extends AbstractDockerClientIT {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractNettyDockerClientIT.class);

    @Override
    protected TestDockerCmdExecFactory initTestDockerCmdExecFactory() {
        return new TestDockerCmdExecFactory(new DockerCmdExecFactoryImpl());
    }
}
