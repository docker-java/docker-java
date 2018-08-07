package com.github.dockerjava.junit;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.cmd.CmdIT;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.TestDockerCmdExecFactory;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import com.github.dockerjava.utils.LogContainerTestCallback;
import com.github.dockerjava.utils.RegistryUtils;

import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.github.dockerjava.cmd.CmdIT.FactoryType.JERSEY;
import static com.github.dockerjava.cmd.CmdIT.FactoryType.NETTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Kanstantsin Shautsou
 */
public class DockerRule extends ExternalResource {
  
    private RegistryUtils registryUtils= new RegistryUtils();
    
    public static final Logger LOG = LoggerFactory.getLogger(DockerRule.class);
    public static final String DEFAULT_IMAGE = "busybox:latest";

    private DockerClient nettyClient;
    private DockerClient jerseyClient;

    private CmdIT cmdIT;
    
    private TestDockerCmdExecFactory testDockerCmdExecFactory;

    public DockerRule(CmdIT cmdIT) {
        this.cmdIT = cmdIT;
    }


    public DockerClient getClient() {
        if (cmdIT.getFactoryType() == NETTY) {
            if (nettyClient == null) {
                testDockerCmdExecFactory = new TestDockerCmdExecFactory(
                  new NettyDockerCmdExecFactory().withConnectTimeout(10 * 1000));
              
                nettyClient = DockerClientBuilder.getInstance(config())
                  .withDockerCmdExecFactory((testDockerCmdExecFactory)).build();
            }

            return nettyClient;
        } else if (cmdIT.getFactoryType() == JERSEY) {
            if (jerseyClient == null) {
                testDockerCmdExecFactory = new TestDockerCmdExecFactory(
                  new JerseyDockerCmdExecFactory().withConnectTimeout(10 * 1000));

                jerseyClient = DockerClientBuilder.getInstance(config())
                  .withDockerCmdExecFactory((testDockerCmdExecFactory)).build();
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
//      LOG.debug("======================= END OF AFTERTEST =======================");
        if (testDockerCmdExecFactory != null) {
            for (String string : testDockerCmdExecFactory.getContainerNames()) {
                ensureContainerRemoved(string);
            }
            for (String string : testDockerCmdExecFactory.getNetworkIds()) {
                ensureNetworkRemoved(string);
            }
            ensurePrivateRegistryRemoved();
        }
    }

    private DefaultDockerClientConfig config() {
        return config(null);
    }

    public DefaultDockerClientConfig config(String password) {
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
        if (cmdIT.getFactoryType() == NETTY) {
            return "netty";
        } else if (cmdIT.getFactoryType() == JERSEY) {
            return "jersey";
        }

        return "default";
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
    
    public void ensureNetworkRemoved(String networkId) {
        try {
            getClient().removeNetworkCmd(networkId).exec();
        } catch (NotFoundException ex) {
            // ignore
        }
    }
    
    public AuthConfig runPrivateRegistry() throws Exception {
        return registryUtils.runPrivateRegistry(getClient());
    }
    
    public void ensurePrivateRegistryRemoved() {
      registryUtils.removePrivateRegistry(getClient());
    }

    public String createPrivateImage(String tagName) throws InterruptedException {
        return registryUtils.createPrivateImage(this, tagName);
    }

    public String createTestImage(String tagName) {
        return registryUtils.createTestImage(this, tagName);
    }
}
