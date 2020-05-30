package com.github.dockerjava.core;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.CreateVolumeCmd;
import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.cmd.CmdIT;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.utils.LogContainerTestCallback;
import lombok.experimental.Delegate;
import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kanstantsin Shautsou
 */
public class DockerRule extends ExternalResource {
    public static final Logger LOG = LoggerFactory.getLogger(DockerRule.class);
    public static final String DEFAULT_IMAGE = "busybox:latest";

    private DockerClient dockerClient;

    private CmdIT cmdIT;

    private final Set<String> createdContainerIds = new HashSet<>();

    private final Set<String> createdNetworkIds = new HashSet<>();

    private final Set<String> createdVolumeNames = new HashSet<>();

    public DockerRule(CmdIT cmdIT) {
        this.cmdIT = cmdIT;
    }


    public DockerClient getClient() {
        if (dockerClient != null) {
            return dockerClient;
        }

        DockerClientImpl dockerClient = cmdIT.getFactoryType().createDockerClient(config());
        DockerHttpClient dockerHttpClient = dockerClient.getHttpClient();

        dockerClient.withDockerCmdExecFactory(
            new DockerCmdExecFactoryDelegate(dockerClient.dockerCmdExecFactory) {
                @Override
                public CreateContainerCmd.Exec createCreateContainerCmdExec() {
                    CreateContainerCmd.Exec exec = super.createCreateContainerCmdExec();
                    return command -> {
                        CreateContainerResponse response = exec.exec(command);
                        createdContainerIds.add(response.getId());
                        return response;
                    };
                }

                @Override
                public CreateNetworkCmd.Exec createCreateNetworkCmdExec() {
                    CreateNetworkCmd.Exec exec = super.createCreateNetworkCmdExec();
                    return command -> {
                        CreateNetworkResponse response = exec.exec(command);
                        createdNetworkIds.add(response.getId());
                        return response;
                    };
                }

                @Override
                public CreateVolumeCmd.Exec createCreateVolumeCmdExec() {
                    CreateVolumeCmd.Exec exec = super.createCreateVolumeCmdExec();
                    return command -> {
                        CreateVolumeResponse response = exec.exec(command);
                        createdVolumeNames.add(response.getName());
                        return response;
                    };
                }
            }
        );

        return this.dockerClient = new DockerClientDelegate(dockerClient) {
            @Override
            public DockerHttpClient getHttpClient() {
                return dockerHttpClient;
            }
        };
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
                    .start()
                    .awaitCompletion();
        }

//        assertThat(getClient(), notNullValue());
//        LOG.info("======================= END OF BEFORETEST =======================\n\n");
    }

    @Override
    protected void after() {
//        LOG.debug("======================= END OF AFTERTEST =======================");
        createdContainerIds.parallelStream().forEach(containerId -> {
            try {
                dockerClient.removeContainerCmd(containerId)
                        .withForce(true)
                        .withRemoveVolumes(true)
                        .exec();
            } catch (ConflictException | NotFoundException ignored) {
            } catch (Throwable e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                LOG.debug("Failed to remove container {}", containerId, e);
            }
        });
        createdNetworkIds.parallelStream().forEach(networkId -> {
            try {
                dockerClient.removeNetworkCmd(networkId).exec();
            } catch (ConflictException | NotFoundException ignored) {
            } catch (Throwable e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                LOG.debug("Failed to remove network {}", networkId, e);
            }
        });
        createdVolumeNames.parallelStream().forEach(volumeName -> {
            try {
                dockerClient.removeVolumeCmd(volumeName).exec();
            } catch (ConflictException | NotFoundException ignored) {
            } catch (Throwable e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                LOG.debug("Failed to remove volume {}", volumeName, e);
            }
        });

        try {
            dockerClient.close();
        } catch (Exception e) {
            LOG.warn("Failed to close the DockerClient", e);
        }
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
                .start()
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

    private static class CreateContainerCmdDelegate implements CreateContainerCmd {
        @Delegate(excludes = Closeable.class)
        private final CreateContainerCmd delegate;

        private CreateContainerCmdDelegate(CreateContainerCmd delegate) {
            this.delegate = delegate;
        }

        @Override
        public void close() {
            delegate.close();
        }
    }

    private static class CreateNetworkCmdDelegate implements CreateNetworkCmd {
        @Delegate(excludes = Closeable.class)
        private final CreateNetworkCmd delegate;

        private CreateNetworkCmdDelegate(CreateNetworkCmd delegate) {
            this.delegate = delegate;
        }

        @Override
        public void close() {
            delegate.close();
        }
    }

    private static class CreateVolumeCmdDelegate implements CreateVolumeCmd {
        @Delegate(excludes = Closeable.class)
        private final CreateVolumeCmd delegate;

        private CreateVolumeCmdDelegate(CreateVolumeCmd delegate) {
            this.delegate = delegate;
        }

        @Override
        public void close() {
            delegate.close();
        }
    }
}
