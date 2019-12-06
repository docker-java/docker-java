package com.github.dockerjava.junit;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.cmd.CmdIT;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.utils.LogContainerTestCallback;
import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author Kanstantsin Shautsou
 */
public class DockerRule extends ExternalResource {
    public static final Logger LOG = LoggerFactory.getLogger(DockerRule.class);
    public static final String DEFAULT_IMAGE = "busybox:latest";

    private DockerClient dockerClient;

    private CmdIT cmdIT;

    public DockerRule(CmdIT cmdIT) {
        this.cmdIT = cmdIT;
    }


    public DockerClient getClient() {
        if (dockerClient != null) {
            return dockerClient;
        }
        return dockerClient = DockerClientBuilder.getInstance(config())
                .withDockerCmdExecFactory(cmdIT.getFactoryType().createExecFactory())
                .build();
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
        return cmdIT.getFactoryType().name().toLowerCase();
    }

    public void ensureContainerRemoved(String container1Name) {
        try {
            getClient().removeContainerCmd(container1Name)
                    .withForce(true)
                    .withRemoveVolumes(true)
                    .exec();
        } catch (NotFoundException ex) {
            // ignore
        }
    }

    public void ensureImageRemoved(String imageId) {
        try {
            getClient().removeImageCmd(imageId)
                    .withForce(true)
                    .exec();
        } catch (NotFoundException ex) {
            // ignore
        }
    }
}
