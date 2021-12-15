package com.github.dockerjava.junit;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerRule;
import com.github.dockerjava.core.DockerClientBuilder;
import org.junit.rules.ExternalResource;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;
import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class PrivateRegistryRule extends ExternalResource {

    private final DockerClient dockerClient;

    private AuthConfig authConfig;

    private String containerId;

    public PrivateRegistryRule() {
        this.dockerClient = DockerClientBuilder.getInstance().build();
    }

    public AuthConfig getAuthConfig() {
        return authConfig;
    }

    public String createPrivateImage(String tagName) throws InterruptedException {
        String imgNameWithTag = createTestImage(tagName);

        dockerClient.pushImageCmd(imgNameWithTag)
                .withAuthConfig(authConfig)
                .start()
                .awaitCompletion(30, TimeUnit.SECONDS);

        dockerClient.removeImageCmd(imgNameWithTag).exec();

        //ensures that the image is available, the private registry needs some time to reflect a tag push
        Thread.sleep(5000);

        return imgNameWithTag;
    }

    public String createTestImage(String tagName) {
        String imgName = authConfig.getRegistryAddress() + "/busybox";

        dockerClient.tagImageCmd(DEFAULT_IMAGE, imgName, tagName).exec();
        return imgName + ":" + tagName;
    }

    /**
     * Starts a local test registry when it is not already started and returns the auth configuration for it
     * This method is synchronized so that only the first invocation starts the registry
     */
    @Override
    protected void before() throws Throwable {

        int port = 5050;

        String imageName = "private-registry-image";

        File baseDir = new File(DockerRule.class.getResource("/privateRegistry").getFile());

        String registryImageId = dockerClient.buildImageCmd(baseDir)
                .withNoCache(true)
                .start()
                .awaitImageId();

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(registryImageId).exec();
        assertThat(inspectImageResponse, not(nullValue()));
        DockerRule.LOG.info("Image Inspect: {}", inspectImageResponse.toString());

        dockerClient.tagImageCmd(registryImageId, imageName, "2")
                .withForce().exec();

        // see https://github.com/docker/distribution/blob/master/docs/deploying.md#native-basic-auth
        CreateContainerResponse testregistry = dockerClient
                .createContainerCmd(imageName + ":2")
                .withHostConfig(newHostConfig()
                        .withPortBindings(new PortBinding(Ports.Binding.bindPort(port), ExposedPort.tcp(5000))))
                .withEnv("REGISTRY_AUTH=htpasswd", "REGISTRY_AUTH_HTPASSWD_REALM=Registry Realm",
                        "REGISTRY_AUTH_HTPASSWD_PATH=/auth/htpasswd", "REGISTRY_LOG_LEVEL=debug",
                        "REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.crt", "REGISTRY_HTTP_TLS_KEY=/certs/domain.key")
                .exec();

        containerId = testregistry.getId();
        dockerClient.startContainerCmd(containerId).exec();

        // wait for registry to boot
        Thread.sleep(3000);

        // credentials as configured in /auth/htpasswd
        authConfig = new AuthConfig()
                .withUsername("testuser")
                .withPassword("testpassword")
                .withRegistryAddress("localhost:" + port);
    }

    @Override
    protected void after() {
        if (containerId != null) {
            dockerClient.removeContainerCmd(containerId)
                    .withForce(true)
                    .withRemoveVolumes(true)
                    .exec();
        }
    }
}
