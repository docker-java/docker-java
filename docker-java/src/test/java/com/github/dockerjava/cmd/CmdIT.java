package com.github.dockerjava.cmd;

import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.DockerRule;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.jaxrs.JerseyDockerHttpClient;
import com.github.dockerjava.junit.category.Integration;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import com.github.dockerjava.okhttp.OkDockerHttpClient;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

/**
 * @author Kanstantsin Shautsou
 */
@Category(Integration.class)
@RunWith(Parameterized.class)
public abstract class CmdIT {
    public enum FactoryType {
        NETTY(true) {
            @Override
            public DockerClientImpl createDockerClient(DockerClientConfig config) {
                return (DockerClientImpl) DockerClientBuilder.getInstance(config)
                    .withDockerCmdExecFactory(
                        new NettyDockerCmdExecFactory()
                            .withConnectTimeout(30 * 1000)
                    )
                    .build();
            }
        },
        JERSEY(false) {
            @Override
            public DockerClientImpl createDockerClient(DockerClientConfig config) {
                return (DockerClientImpl) DockerClientBuilder.getInstance(config)
                    .withDockerHttpClient(
                        new JerseyDockerHttpClient.Builder()
                            .dockerHost(config.getDockerHost())
                            .sslConfig(config.getSSLConfig())
                            .connectTimeout(30 * 1000)
                            .build()
                    )
                    .build();
            }
        },
        OKHTTP(true) {
            @Override
            public DockerClientImpl createDockerClient(DockerClientConfig config) {
                return (DockerClientImpl) DockerClientBuilder.getInstance(config)
                    .withDockerHttpClient(
                        new OkDockerHttpClient.Builder()
                            .dockerHost(config.getDockerHost())
                            .sslConfig(config.getSSLConfig())
                            .connectTimeout(30 * 100)
                            .build()
                    )
                    .build();
            }
        },
        HTTPCLIENT5(true) {
            @Override
            public DockerClientImpl createDockerClient(DockerClientConfig config) {
                return (DockerClientImpl) DockerClientBuilder.getInstance(config)
                    .withDockerHttpClient(
                        new ApacheDockerHttpClient.Builder()
                            .dockerHost(config.getDockerHost())
                            .sslConfig(config.getSSLConfig())
                            .build()
                    )
                    .build();
            }
        };

        private final String subnetPrefix;
        private final boolean supportsStdinAttach;

        FactoryType(boolean supportsStdinAttach) {
            this.subnetPrefix = "10." + (100 + ordinal()) + ".";
            this.supportsStdinAttach = supportsStdinAttach;
        }

        public String getSubnetPrefix() {
            return subnetPrefix;
        }

        public boolean supportsStdinAttach() {
            return supportsStdinAttach;
        }

        public abstract DockerClientImpl createDockerClient(DockerClientConfig config);
    }

    @Parameterized.Parameters(name = "{index}:{0}")
    public static Iterable<FactoryType> data() {
        return Arrays.asList(FactoryType.values());
    }

    @Parameterized.Parameter
    public FactoryType factoryType;

    public FactoryType getFactoryType() {
        return factoryType;
    }

    @Rule
    public DockerRule dockerRule = new DockerRule( this);

}
