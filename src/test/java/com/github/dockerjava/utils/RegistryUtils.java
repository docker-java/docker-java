package com.github.dockerjava.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.github.dockerjava.junit.DockerRule;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.junit.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class RegistryUtils {

    private static AuthConfig privateRegistryAuthConfig;

    /**
     * Starts a local test registry when it is not already started and returns the auth configuration for it
     * This method is synchronized so that only the first invocation starts the registry
     * @return The auth configuration for the started private docker registry
     * @throws Exception
     */
    public static synchronized AuthConfig runPrivateRegistry(DockerClient dockerClient) throws Exception {
        if (privateRegistryAuthConfig == null) {
            int port = 5050;

            String containerName = "private-registry";
            String imageName = "private-registry-image";

            File baseDir = new File(DockerRule.class.getResource("/privateRegistry").getFile());

            try {
                dockerClient.removeContainerCmd(containerName)
                        .withForce(true)
                        .withRemoveVolumes(true)
                        .exec();
            } catch (NotFoundException ex) {
                // ignore
            }

            String registryImageId = dockerClient.buildImageCmd(baseDir)
                    .withNoCache(true)
                    .exec(new BuildImageResultCallback())
                    .awaitImageId();

            InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(registryImageId).exec();
            assertThat(inspectImageResponse, not(nullValue()));
            DockerRule.LOG.info("Image Inspect: {}", inspectImageResponse.toString());

            dockerClient.tagImageCmd(registryImageId, imageName, "2")
                    .withForce().exec();

            // see https://github.com/docker/distribution/blob/master/docs/deploying.md#native-basic-auth
            CreateContainerResponse testregistry = dockerClient
                    .createContainerCmd(imageName + ":2")
                    .withName(containerName)
                    .withPortBindings(new PortBinding(Ports.Binding.bindPort(port), ExposedPort.tcp(5000)))
                    .withEnv("REGISTRY_AUTH=htpasswd", "REGISTRY_AUTH_HTPASSWD_REALM=Registry Realm",
                            "REGISTRY_AUTH_HTPASSWD_PATH=/auth/htpasswd", "REGISTRY_LOG_LEVEL=debug",
                            "REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.crt", "REGISTRY_HTTP_TLS_KEY=/certs/domain.key")
                    .exec();

            dockerClient.startContainerCmd(testregistry.getId()).exec();

            // wait for registry to boot
            Thread.sleep(3000);

            // credentials as configured in /auth/htpasswd
            privateRegistryAuthConfig = new AuthConfig()
                    .withUsername("testuser")
                    .withPassword("testpassword")
                    .withEmail("foo@bar.de")
                    .withRegistryAddress("localhost:" + port);
        }

        return privateRegistryAuthConfig;
    }

    public static String createPrivateImage(DockerRule dockerRule, String tagName) throws InterruptedException {
        if (privateRegistryAuthConfig == null)
            throw new IllegalStateException("Ensure that you have invoked runPrivateRegistry beforehand.");

        String imgNameWithTag = createTestImage(dockerRule, tagName);

        dockerRule.getClient().pushImageCmd(imgNameWithTag)
                .withAuthConfig(privateRegistryAuthConfig)
                .exec(new PushImageResultCallback())
                .awaitCompletion(30, TimeUnit.SECONDS);

        dockerRule.getClient().removeImageCmd(imgNameWithTag)
                .exec();

        //ensures that the image is available, the private registry needs some time to reflect a tag push
        Thread.sleep(5000);

        return imgNameWithTag;
    }

    public static String createTestImage(DockerRule dockerRule, String tagName) {
        String tag = dockerRule.getKind() + "-" + tagName;
        String imgName = privateRegistryAuthConfig.getRegistryAddress() + "/busybox";
        String imgNameWithTag = imgName + ":" + tag;

        dockerRule.getClient().tagImageCmd(DEFAULT_IMAGE, imgName, tag)
                .exec();
        return imgNameWithTag;
    }
}
