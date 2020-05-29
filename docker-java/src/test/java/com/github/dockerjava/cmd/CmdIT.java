package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.DelegatingDockerCmdExecFactory;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerCmdExecFactory;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientConfigAware;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import com.github.dockerjava.junit.DockerRule;
import com.github.dockerjava.junit.category.Integration;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import com.github.dockerjava.okhttp.OkHttpDockerCmdExecFactory;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Kanstantsin Shautsou
 */
@Category(Integration.class)
@RunWith(Parameterized.class)
public abstract class CmdIT {

    public enum FactoryType {
        SSH(true, true) {
            @Override
            public DockerCmdExecFactory createExecFactory() {
                return new SSHClientFactory();
            }
        },
        NETTY(true, false) {
            @Override
            public DockerCmdExecFactory createExecFactory() {
                return new NettyDockerCmdExecFactory().withConnectTimeout(30 * 1000);
            }
        },
        JERSEY(false, false) {
            @Override
            public DockerCmdExecFactory createExecFactory() {
                return new JerseyDockerCmdExecFactory().withConnectTimeout(30 * 1000);
            }
        },
        OKHTTP(true, false) {
            @Override
            public DockerCmdExecFactory createExecFactory() {
                return new OkHttpDockerCmdExecFactory().withConnectTimeout(30 * 1000);
            }
        },
        HTTPCLIENT5(true, false) {
            @Override
            public DockerCmdExecFactory createExecFactory() {
                class FakeFactory extends DelegatingDockerCmdExecFactory implements DockerClientConfigAware {

                    private DefaultDockerCmdExecFactory dockerCmdExecFactory;

                    @Override
                    public final DockerCmdExecFactory getDockerCmdExecFactory() {
                        return dockerCmdExecFactory;
                    }

                    @Override
                    public void init(DockerClientConfig dockerClientConfig) {
                        dockerCmdExecFactory = new DefaultDockerCmdExecFactory(
                            new ApacheDockerHttpClient.Factory()
                                .dockerClientConfig(dockerClientConfig)
                                .build(),
                            dockerClientConfig.getObjectMapper()
                        );
                        dockerCmdExecFactory.init(dockerClientConfig);
                    }
                }
                return new FakeFactory();
            }
        };

        private final String subnetPrefix;
        private final boolean supportsStdinAttach;
        private final boolean supportsSSH;

        FactoryType(boolean supportsStdinAttach, boolean supportsSSH) {
            this.subnetPrefix = "10." + (100 + ordinal()) + ".";
            this.supportsStdinAttach = supportsStdinAttach;
            this.supportsSSH = supportsSSH;
        }

        public String getSubnetPrefix() {
            return subnetPrefix;
        }

        public boolean supportsStdinAttach() {
            return supportsStdinAttach;
        }

        public abstract DockerCmdExecFactory createExecFactory();
    }

    @Parameterized.Parameters(name = "{index}:{0}")
    public static Iterable<FactoryType> data() {
        if (System.getenv("DOCKER_HOST").matches("ssh://.*")) {
            return Stream.of(FactoryType.values()).filter(f -> f.supportsSSH).collect(Collectors.toList());
        } else {
            return Stream.of(FactoryType.values()).filter(f -> !f.supportsSSH).collect(Collectors.toList());
        }
    }

    @Parameterized.Parameter
    public FactoryType factoryType;

    public FactoryType getFactoryType() {
        return factoryType;
    }

    @Rule
    public DockerRule dockerRule = new DockerRule(this);

}
