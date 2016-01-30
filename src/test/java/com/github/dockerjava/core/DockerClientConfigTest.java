package com.github.dockerjava.core;

import com.github.dockerjava.api.model.AuthConfig;
import org.apache.commons.lang.SerializationUtils;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

public class DockerClientConfigTest {

    public static final DockerClientConfig EXAMPLE_CONFIG = newExampleConfig();

    private static DockerClientConfig newExampleConfig() {
        return new DockerClientConfig(URI.create("tcp://foo"), "dockerConfig", "apiVersion", "registryUrl", "registryUsername", "registryPassword", "registryEmail",
                "dockerCertPath", true);
    }

    @Test
    public void equals() throws Exception {
        assertEquals(EXAMPLE_CONFIG, newExampleConfig());
    }

    @Test
    public void environmentDockerHost() throws Exception {

        // given docker host in env
        Map<String, String> env = new HashMap<String, String>();
        env.put(DockerClientConfig.DOCKER_HOST, "tcp://baz:8768");
        // and it looks to be SSL disabled
        env.remove("DOCKER_CERT_PATH");

        // when you build a config
        DockerClientConfig config = buildConfig(env, new Properties());

        assertEquals(config.getDockerHost(), URI.create("tcp://baz:8768"));
    }

    @Test
    public void environment() throws Exception {

        // given a default config in env properties
        Map<String, String> env = new HashMap<String, String>();
        env.put(DockerClientConfig.DOCKER_HOST, "tcp://foo");
        env.put(DockerClientConfig.API_VERSION, "apiVersion");
        env.put(DockerClientConfig.REGISTRY_USERNAME, "registryUsername");
        env.put(DockerClientConfig.REGISTRY_PASSWORD, "registryPassword");
        env.put(DockerClientConfig.REGISTRY_EMAIL, "registryEmail");
        env.put(DockerClientConfig.REGISTRY_URL, "registryUrl");
        env.put(DockerClientConfig.DOCKER_CONFIG, "dockerConfig");
        env.put(DockerClientConfig.DOCKER_CERT_PATH, "dockerCertPath");
        env.put(DockerClientConfig.DOCKER_TLS_VERIFY, "1");


        // when you build a config
        DockerClientConfig config = buildConfig(env, new Properties());

        // then we get the example object
        assertEquals(config, EXAMPLE_CONFIG);
    }

    private DockerClientConfig buildConfig(Map<String, String> env, Properties systemProperties) {
        return DockerClientConfig.createDefaultConfigBuilder(env, systemProperties).build();
    }

    @Test
    public void defaults() throws Exception {

        // given default cert path
        Properties systemProperties = new Properties();
        systemProperties.setProperty("user.name", "someUserName");
        systemProperties.setProperty("user.home", "someHomeDir");

        // when you build config
        DockerClientConfig config = buildConfig(Collections.<String, String> emptyMap(), systemProperties);

        // then the cert path is as expected
        assertEquals(config.getDockerHost(), URI.create("tcp://localhost:2376"));
        assertEquals(config.getRegistryUsername(), "someUserName");
        assertEquals(config.getRegistryUrl(), AuthConfig.DEFAULT_SERVER_ADDRESS);
        assertEquals(config.getApiVersion(), RemoteApiVersion.unknown());
        assertEquals(config.getDockerConfig(), "someHomeDir/.docker");
        assertEquals(config.getDockerCertPath(), "someHomeDir/.docker/certs");
    }

    @Test
    public void systemProperties() throws Exception {

        // given system properties based on the example
        Properties systemProperties = new Properties();
        systemProperties.put(DockerClientConfig.DOCKER_HOST, "tcp://foo");
        systemProperties.put(DockerClientConfig.API_VERSION, "apiVersion");
        systemProperties.put(DockerClientConfig.REGISTRY_USERNAME, "registryUsername");
        systemProperties.put(DockerClientConfig.REGISTRY_PASSWORD, "registryPassword");
        systemProperties.put(DockerClientConfig.REGISTRY_EMAIL, "registryEmail");
        systemProperties.put(DockerClientConfig.REGISTRY_URL, "registryUrl");
        systemProperties.put(DockerClientConfig.DOCKER_CONFIG, "dockerConfig");
        systemProperties.put(DockerClientConfig.DOCKER_CERT_PATH, "dockerCertPath");
        systemProperties.put(DockerClientConfig.DOCKER_TLS_VERIFY, "1");

        // when you build new config
        DockerClientConfig config = buildConfig(Collections.<String, String> emptyMap(), systemProperties);

        // then it is the same as the example
        assertEquals(config, EXAMPLE_CONFIG);

    }

    @Test
    public void serializableTest() {
        final byte[] serialized = SerializationUtils.serialize(EXAMPLE_CONFIG);
        final DockerClientConfig deserialized = (DockerClientConfig) SerializationUtils.deserialize(serialized);

        assertThat("Deserialized object mush match source object", deserialized, equalTo(EXAMPLE_CONFIG));
    }
}
