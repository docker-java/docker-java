package com.github.dockerjava.netty;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;

import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.TestDockerCmdExecFactory;

public abstract class AbstractNettyDockerClientTest extends AbstractDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractNettyDockerClientTest.class);

    @Override
    protected TestDockerCmdExecFactory initTestDockerCmdExecFactory() {


        return new TestDockerCmdExecFactory(new NettyDockerCmdExecFactory() {
            @Override
            public void init(DockerClientConfig dockerClientConfig) {
                if (dockerClientConfig.getDockerHost().getScheme().equals("unix")
                    && !System.getProperty("os.name").toLowerCase(Locale.UK).trim().startsWith("linux")) {
                    throw new SkipException("unix domain sockets not supported on netty on non-linux hosts");
                }
                super.init(dockerClientConfig);
            }
        });
    }
}
