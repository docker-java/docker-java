package com.github.dockerjava.junit;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.cmd.CmdTest;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import com.github.dockerjava.utils.LogContainerTestCallback;
import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.github.dockerjava.cmd.CmdTest.FactoryType.JERSEY;
import static com.github.dockerjava.cmd.CmdTest.FactoryType.NETTY;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Kanstantsin Shautsou
 */
public class DockerRule extends ExternalResource {
    public static final Logger LOG = LoggerFactory.getLogger(DockerRule.class);
    public static final String DEFAULT_IMAGE = "busybox:latest";

    private DockerClient nettyClient;
    private DockerClient jerseyClient;

    private CmdTest cmdTest;
    private Object cmdExecFactory;


    public DockerRule(CmdTest cmdTest) {
        this.cmdTest = cmdTest;
    }


    public DockerClient getClient() {
        if (cmdTest.getFactoryType() == NETTY) {
            if (nettyClient == null) {
                nettyClient = DockerClientBuilder.getInstance(config())
                        .withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
                        .build();
            }

            return nettyClient;
        } else if (cmdTest.getFactoryType() == JERSEY) {
            if (jerseyClient == null) {
                jerseyClient = DockerClientBuilder.getInstance(config())
                        .withDockerCmdExecFactory(new JerseyDockerCmdExecFactory())
                        .build();
            }
            return jerseyClient;
        }

        throw new IllegalStateException("Why factory type is not set?");
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return super.apply(base, description);
    }

    @Override
    protected void before() throws Throwable {
//        LOG.info("======================= BEFORETEST =======================");
        LOG.debug("Connecting to Docker server");


        try {
            getClient().inspectImageCmd(DEFAULT_IMAGE).exec();
        } catch (NotFoundException e) {
            LOG.info("Pulling image 'busybox'");
            // need to block until image is pulled completely
            getClient().pullImageCmd("busybox")
                    .withTag("latest")
                    .exec(new PullImageResultCallback())
                    .awaitSuccess();
        }

//        assertThat(getClient(), notNullValue());
//        LOG.info("======================= END OF BEFORETEST =======================\n\n");
    }

    @Override
    protected void after() {
//        LOG.debug("======================= END OF AFTERTEST =======================");
    }

    private static DefaultDockerClientConfig config() {
        return config(null);
    }

    public static DefaultDockerClientConfig config(String password) {
        DefaultDockerClientConfig.Builder builder = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withRegistryUrl("https://index.docker.io/v1/");
        if (password != null) {
            builder = builder.withRegistryPassword(password);
        }

        return builder.build();
    }

    public String buildImage(File baseDir) throws Exception {
        return getClient().buildImageCmd(baseDir)
                .withNoCache(true)
                .exec(new BuildImageResultCallback())
                .awaitImageId();
    }

    public String containerLog(String containerId) throws Exception {
        return getClient().logContainerCmd(containerId)
                .withStdOut(true)
                .exec(new LogContainerTestCallback())
                .awaitCompletion()
                .toString();
    }

    public String getKind() {
        if (cmdTest.getFactoryType() == NETTY) {
            return "netty";
        } else if (cmdTest.getFactoryType() == JERSEY) {
            return "jersey";
        }

        return "default";
    }

    public void ensureRemoved(String container1Name) {
        try {
            getClient().removeContainerCmd(container1Name)
                    .withForce(true)
                    .withRemoveVolumes(true)
                    .exec();
        } catch (NotFoundException ex) {
            // ignore
        }
    }
}
