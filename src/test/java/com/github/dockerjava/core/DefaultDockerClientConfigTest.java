package com.github.dockerjava.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.SerializationUtils;
import org.testng.annotations.Test;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.google.common.io.Resources;

public class DefaultDockerClientConfigTest {

    public static final DefaultDockerClientConfig EXAMPLE_CONFIG = newExampleConfig();

    private static DefaultDockerClientConfig newExampleConfig() {

        String dockerCertPath = dockerCertPath();

        return new DefaultDockerClientConfig(URI.create("tcp://foo"), "dockerConfig", "apiVersion", "registryUrl", "registryUsername", "registryPassword", "registryEmail",
                new LocalDirectorySSLConfig(dockerCertPath));
    }

    private static String homeDir() {
        return "target/test-classes/someHomeDir";
    }

    private static String dockerCertPath() {
        return homeDir() + "/.docker";
    }

    @Test
    public void equals() throws Exception {
        assertEquals(EXAMPLE_CONFIG, newExampleConfig());
    }

    @Test
    public void environmentDockerHost() throws Exception {

        // given docker host in env
        Map<String, String> env = new HashMap<String, String>();
        env.put(DefaultDockerClientConfig.DOCKER_HOST, "tcp://baz:8768");
        // and it looks to be SSL disabled
        env.remove("DOCKER_CERT_PATH");

        // given default cert path
        Properties systemProperties = new Properties();
        systemProperties.setProperty("user.name", "someUserName");
        systemProperties.setProperty("user.home", homeDir());

        // when you build a config
        DefaultDockerClientConfig config = buildConfig(env, systemProperties);

        assertEquals(config.getDockerHost(), URI.create("tcp://baz:8768"));
    }

    @Test
    public void environment() throws Exception {

        // given a default config in env properties
        Map<String, String> env = new HashMap<String, String>();
        env.put(DefaultDockerClientConfig.DOCKER_HOST, "tcp://foo");
        env.put(DefaultDockerClientConfig.API_VERSION, "apiVersion");
        env.put(DefaultDockerClientConfig.REGISTRY_USERNAME, "registryUsername");
        env.put(DefaultDockerClientConfig.REGISTRY_PASSWORD, "registryPassword");
        env.put(DefaultDockerClientConfig.REGISTRY_EMAIL, "registryEmail");
        env.put(DefaultDockerClientConfig.REGISTRY_URL, "registryUrl");
        env.put(DefaultDockerClientConfig.DOCKER_CONFIG, "dockerConfig");
        env.put(DefaultDockerClientConfig.DOCKER_CERT_PATH, dockerCertPath());
        env.put(DefaultDockerClientConfig.DOCKER_TLS_VERIFY, "1");

        // when you build a config
        DefaultDockerClientConfig config = buildConfig(env, new Properties());

        // then we get the example object
        assertEquals(config, EXAMPLE_CONFIG);
    }

    private DefaultDockerClientConfig buildConfig(Map<String, String> env, Properties systemProperties) {
        return DefaultDockerClientConfig.createDefaultConfigBuilder(env, systemProperties).build();
    }

    @Test
    public void defaults() throws Exception {

        // given default cert path
        Properties systemProperties = new Properties();
        systemProperties.setProperty("user.name", "someUserName");
        systemProperties.setProperty("user.home", homeDir());

        // when you build config
        DefaultDockerClientConfig config = buildConfig(Collections.<String, String> emptyMap(), systemProperties);

        // then the cert path is as expected
        assertEquals(config.getDockerHost(), URI.create("unix:///var/run/docker.sock"));
        assertEquals(config.getRegistryUsername(), "someUserName");
        assertEquals(config.getRegistryUrl(), AuthConfig.DEFAULT_SERVER_ADDRESS);
        assertEquals(config.getApiVersion(), RemoteApiVersion.unknown());
        assertEquals(config.getDockerConfig(), homeDir() + "/.docker");
        assertNull(config.getSSLConfig());
    }

    @Test
    public void systemProperties() throws Exception {

        // given system properties based on the example
        Properties systemProperties = new Properties();
        systemProperties.put(DefaultDockerClientConfig.DOCKER_HOST, "tcp://foo");
        systemProperties.put(DefaultDockerClientConfig.API_VERSION, "apiVersion");
        systemProperties.put(DefaultDockerClientConfig.REGISTRY_USERNAME, "registryUsername");
        systemProperties.put(DefaultDockerClientConfig.REGISTRY_PASSWORD, "registryPassword");
        systemProperties.put(DefaultDockerClientConfig.REGISTRY_EMAIL, "registryEmail");
        systemProperties.put(DefaultDockerClientConfig.REGISTRY_URL, "registryUrl");
        systemProperties.put(DefaultDockerClientConfig.DOCKER_CONFIG, "dockerConfig");
        systemProperties.put(DefaultDockerClientConfig.DOCKER_CERT_PATH, dockerCertPath());
        systemProperties.put(DefaultDockerClientConfig.DOCKER_TLS_VERIFY, "1");

        // when you build new config
        DefaultDockerClientConfig config = buildConfig(Collections.<String, String> emptyMap(), systemProperties);

        // then it is the same as the example
        assertEquals(config, EXAMPLE_CONFIG);

    }

    @Test
    public void serializableTest() {
        final byte[] serialized = SerializationUtils.serialize(EXAMPLE_CONFIG);
        final DefaultDockerClientConfig deserialized = (DefaultDockerClientConfig) SerializationUtils.deserialize(serialized);

        assertThat("Deserialized object mush match source object", deserialized, equalTo(EXAMPLE_CONFIG));
    }

    @Test()
    public void testSslContextEmpty() throws Exception {
        new DefaultDockerClientConfig(URI.create("tcp://foo"), "dockerConfig", "apiVersion", "registryUrl", "registryUsername", "registryPassword", "registryEmail",
                null);
    }



    @Test()
    public void testTlsVerifyAndCertPath() throws Exception {
        new DefaultDockerClientConfig(URI.create("tcp://foo"), "dockerConfig", "apiVersion", "registryUrl", "registryUsername", "registryPassword", "registryEmail",
                new LocalDirectorySSLConfig(dockerCertPath()));
    }

    @Test(expectedExceptions = DockerClientException.class)
    public void testWrongHostScheme() throws Exception {
        new DefaultDockerClientConfig(URI.create("http://foo"), "dockerConfig", "apiVersion", "registryUrl", "registryUsername", "registryPassword", "registryEmail",
                null);
    }

    @Test()
    public void testTcpHostScheme() throws Exception {
        new DefaultDockerClientConfig(URI.create("tcp://foo"), "dockerConfig", "apiVersion", "registryUrl", "registryUsername", "registryPassword", "registryEmail",
                null);
    }

    @Test()
    public void testUnixHostScheme() throws Exception {
        new DefaultDockerClientConfig(URI.create("unix://foo"), "dockerConfig", "apiVersion", "registryUrl", "registryUsername", "registryPassword", "registryEmail",
                null);
    }

    @Test
    public void withDockerTlsVerify() throws Exception {
        DefaultDockerClientConfig.Builder builder = new DefaultDockerClientConfig.Builder();
        Field field = builder.getClass().getDeclaredField("dockerTlsVerify");
        field.setAccessible(true);

        builder.withDockerTlsVerify("");
        assertThat((Boolean) field.get(builder), is(false));

        builder.withDockerTlsVerify("false");
        assertThat((Boolean) field.get(builder), is(false));

        builder.withDockerTlsVerify("FALSE");
        assertThat((Boolean) field.get(builder), is(false));

        builder.withDockerTlsVerify("true");
        assertThat((Boolean) field.get(builder), is(true));

        builder.withDockerTlsVerify("TRUE");
        assertThat((Boolean) field.get(builder), is(true));

        builder.withDockerTlsVerify("0");
        assertThat((Boolean) field.get(builder), is(false));

        builder.withDockerTlsVerify("1");
        assertThat((Boolean) field.get(builder), is(true));
    }

    @Test
    public void testGetAuthConfigurationsFromDockerCfg() throws URISyntaxException {
        File cfgFile = new File(Resources.getResource("com.github.dockerjava.core/registry.v1").toURI());
        DefaultDockerClientConfig clientConfig = new DefaultDockerClientConfig(URI.create(
            "unix://foo"), cfgFile.getAbsolutePath(), "apiVersion", "registryUrl", "registryUsername", "registryPassword",
            "registryEmail", null);

        AuthConfigurations authConfigurations = clientConfig.getAuthConfigurations();
        assertThat(authConfigurations, notNullValue());
        assertThat(authConfigurations.getConfigs().get("https://test.docker.io/v1/"), notNullValue());

        AuthConfig authConfig = authConfigurations.getConfigs().get("https://test.docker.io/v1/");
        assertThat(authConfig.getUsername(), equalTo("user"));
        assertThat(authConfig.getPassword(), equalTo("password"));
    }

    @Test
    public void testGetAuthConfigurationsFromConfigJson() throws URISyntaxException {
        File cfgFile = new File(Resources.getResource("com.github.dockerjava.core/registry.v2").toURI());
        DefaultDockerClientConfig clientConfig = new DefaultDockerClientConfig(URI.create(
            "unix://foo"), cfgFile.getAbsolutePath(), "apiVersion", "registryUrl", "registryUsername", "registryPassword",
            "registryEmail", null);

        AuthConfigurations authConfigurations = clientConfig.getAuthConfigurations();
        assertThat(authConfigurations, notNullValue());
        assertThat(authConfigurations.getConfigs().get("https://test.docker.io/v2/"), notNullValue());

        AuthConfig authConfig = authConfigurations.getConfigs().get("https://test.docker.io/v2/");
        assertThat(authConfig.getUsername(), equalTo("user"));
        assertThat(authConfig.getPassword(), equalTo("password"));
    }

}
