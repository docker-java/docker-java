package com.github.dockerjava.core;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;


public class DockerClientImplTest {

    @Test
    public void configuredInstanceAuthConfig() {
        // given a config with null serverAddress
        DefaultDockerClientConfig dockerClientConfig = new DefaultDockerClientConfig(URI.create("tcp://foo"),
            new DockerConfigFile(), null, null, null, "", "", "", null);
        DockerClientImpl dockerClient = DockerClientImpl.getInstance(dockerClientConfig);

        // when we get the auth config
        try {
            dockerClient.authConfig();
            throw new AssertionError();
        } catch (NullPointerException e) {
            // then we get a NPE with expected message
            assertEquals("Configured serverAddress is null.", e.getMessage());
        }
    }

    @Test
    public void defaultInstanceAuthConfig() {

        System.setProperty("user.home", "target/test-classes/someHomeDir");

        // given a default client
        DockerClientImpl dockerClient = DockerClientImpl.getInstance();

        // when we get the auth config
        dockerClient.authConfig();

        // then we do not get an exception
    }
}
