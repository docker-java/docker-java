package com.github.dockerjava.core;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.fail;

public class DockerClientImplTest {

    @Test
    public void configuredInstanceAuthConfig() throws Exception {
        // given a config with null serverAddress
        DockerClientConfig dockerClientConfig = new DockerClientConfig(null, null, "", "", "", null, null,  0, false, null, 20, 2);
        DockerClientImpl dockerClient = DockerClientImpl.getInstance(dockerClientConfig);

        // when we get the auth config
        try {
            dockerClient.authConfig();
            fail();
        } catch (NullPointerException e) {
            // then we get a NPE with expected message
            assertEquals(e.getMessage(), "Configured serverAddress is null.");
        }
    }

    @Test
    public void defaultInstanceAuthConfig() throws Exception {
        // given a default client
        DockerClientImpl dockerClient = DockerClientImpl.getInstance();

        // when we get the auth config
        dockerClient.authConfig();

        // then we do not get an exception
    }
}