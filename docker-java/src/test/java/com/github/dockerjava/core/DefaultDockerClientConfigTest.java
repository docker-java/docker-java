package com.github.dockerjava.core;

import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.google.common.io.Resources;
import java.io.IOException;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DefaultDockerClientConfigTest {

    public static final DefaultDockerClientConfig EXAMPLE_CONFIG = newExampleConfig();
    public static final DefaultDockerClientConfig EXAMPLE_CONFIG_FULLY_LOADED = newExampleConfigFullyLoaded();

    private static DefaultDockerClientConfig newExampleConfig() {
        String dockerCertPath = dockerCertPath();
        return new DefaultDockerClientConfig(URI.create("tcp://foo"), null, "dockerConfig", "apiVersion", "registryUrl",
            "registryUsername", "registryPassword", "registryEmail",
            new LocalDirectorySSLConfig(dockerCertPath));
    }

    private static DefaultDockerClientConfig newExampleConfigFullyLoaded() {
        try {
            String dockerCertPath = dockerCertPath();
            String dockerConfig = "dockerConfig";
            DockerConfigFile loadedConfigFile = DockerConfigFile.loadConfig(DockerClientConfig.getDefaultObjectMapper(), dockerConfig);
            return new DefaultDockerClientConfig(URI.create("tcp://foo"), loadedConfigFile, dockerConfig, "apiVersion", "registryUrl",
                "registryUsername", "registryPassword", "registryEmail",
                new LocalDirectorySSLConfig(dockerCertPath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static String homeDir() {
        return "target/test-classes/someHomeDir";
    }

    private static String dockerCertPath() {
        return homeDir() + "/.docker";
    }

    @Test
    public void equals() {
        assertEquals(EXAMPLE_CONFIG, newExampleConfig());
    }

    @Test
    public void environmentDockerHost() {

        // given docker host in env
        Map<String, String> env = new HashMap<>();
        env.put(DefaultDockerClientConfig.DOCKER_HOST, "tcp://baz:8768");
        // and it looks to be SSL disabled
        env.remove("DOCKER_CERT_PATH");

        // given default cert path
        Properties systemProperties = new Properties();
        systemProperties.setProperty("user.name", "someUserName");
        systemProperties.setProperty("user.home", homeDir());

        // when you build a config
        DefaultDockerClientConfig config = buildConfig(env, systemProperties);

        assertEquals(URI.create("tcp://baz:8768"), config.getDockerHost());
    }

    @Test
    public void dockerContextFromConfig() {
        // given home directory with docker contexts configured
        Properties systemProperties = new Properties();
        systemProperties.setProperty("user.home", "target/test-classes/dockerContextHomeDir");

        // and an empty environment
        Map<String, String> env = new HashMap<>();

        // when you build a config
        DefaultDockerClientConfig config = buildConfig(env, systemProperties);

        assertEquals(URI.create("unix:///configcontext.sock"), config.getDockerHost());
    }

    @Test
    public void dockerContextFromEnvironmentVariable() {
        // given home directory with docker contexts
        Properties systemProperties = new Properties();
        systemProperties.setProperty("user.home", "target/test-classes/dockerContextHomeDir");

        // and an environment variable that overrides docker context
        Map<String, String> env = new HashMap<>();
        env.put(DefaultDockerClientConfig.DOCKER_CONTEXT, "envvarcontext");

        // when you build a config
        DefaultDockerClientConfig config = buildConfig(env, systemProperties);

        assertEquals(URI.create("unix:///envvarcontext.sock"), config.getDockerHost());
    }

    @Test
    public void dockerContextWithDockerHostAndTLS() {
        // given home directory with docker contexts
        Properties systemProperties = new Properties();
        systemProperties.setProperty("user.home", "target/test-classes/dockerContextHomeDir");

        // and an environment variable that overrides docker context
        Map<String, String> env = new HashMap<>();
        env.put(DefaultDockerClientConfig.DOCKER_CONTEXT, "remote");

        // when you build a config
        DefaultDockerClientConfig config = buildConfig(env, systemProperties);

        assertEquals(URI.create("tcp://remote:2376"), config.getDockerHost());
        assertTrue("SSL config is set", config.getSSLConfig() instanceof LocalDirectorySSLConfig);
        assertEquals("target/test-classes/com/github/dockerjava/core/util/CertificateUtilsTest/allFilesExist",
            ((LocalDirectorySSLConfig)config.getSSLConfig()).getDockerCertPath());
    }

    @Test
    public void environment() {

        // given a default config in env properties
        Map<String, String> env = new HashMap<>();
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
        assertEquals(EXAMPLE_CONFIG_FULLY_LOADED, config);
    }

    @Test
    public void emptyHost() {
        Map<String, String> env = new HashMap<>();
        env.put(DefaultDockerClientConfig.DOCKER_HOST, " ");

        DefaultDockerClientConfig config = buildConfig(env, new Properties());

        assertEquals(
            DefaultDockerClientConfig.DEFAULT_DOCKER_HOST,
            config.getDockerHost().toString()
        );
    }

    private DefaultDockerClientConfig buildConfig(Map<String, String> env, Properties systemProperties) {
        return DefaultDockerClientConfig.createDefaultConfigBuilder(env, systemProperties).build();
    }

    @Test
    public void defaults() {

        // given default cert path
        Properties systemProperties = new Properties();
        systemProperties.setProperty("user.name", "someUserName");
        systemProperties.setProperty("user.home", homeDir());

        // when you build config
        DefaultDockerClientConfig config = buildConfig(Collections.<String, String> emptyMap(), systemProperties);

        // then the cert path is as expected
        assertEquals(URI.create("unix:///var/run/docker.sock"), config.getDockerHost());
        assertEquals("someUserName", config.getRegistryUsername());
        assertEquals(AuthConfig.DEFAULT_SERVER_ADDRESS, config.getRegistryUrl());
        assertEquals(RemoteApiVersion.unknown(), config.getApiVersion());
        assertEquals(homeDir() + "/.docker", config.getDockerConfigPath());
        assertNull(config.getSSLConfig());
    }

    @Test
    public void systemProperties() {

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
        assertEquals(EXAMPLE_CONFIG_FULLY_LOADED, config);

    }

    @Test
    public void serializableTest() {
        final byte[] serialized = SerializationUtils.serialize(EXAMPLE_CONFIG);
        final DefaultDockerClientConfig deserialized = (DefaultDockerClientConfig) SerializationUtils.deserialize(serialized);

        assertThat("Deserialized object mush match source object", deserialized, equalTo(EXAMPLE_CONFIG));
    }

    @Test()
    public void testSslContextEmpty() {
        new DefaultDockerClientConfig(URI.create("tcp://foo"), new DockerConfigFile(), "dockerConfig", "apiVersion", "registryUrl", "registryUsername", "registryPassword", "registryEmail",
                null);
    }



    @Test()
    public void testTlsVerifyAndCertPath() {
        new DefaultDockerClientConfig(URI.create("tcp://foo"), new DockerConfigFile(), "dockerConfig", "apiVersion", "registryUrl", "registryUsername", "registryPassword", "registryEmail",
                new LocalDirectorySSLConfig(dockerCertPath()));
    }

    @Test()
    public void testAnyHostScheme() {
        URI dockerHost = URI.create("a" + UUID.randomUUID().toString().replace("-", "") + "://foo");
        new DefaultDockerClientConfig(dockerHost, new DockerConfigFile(), "dockerConfig", "apiVersion", "registryUrl", "registryUsername", "registryPassword", "registryEmail",
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
    public void dockerHostSetExplicitlyOnSetter() {
        DefaultDockerClientConfig.Builder builder = DefaultDockerClientConfig.createDefaultConfigBuilder(Collections.emptyMap(), new Properties());
        assertThat(builder.isDockerHostSetExplicitly(), is(false));

        builder.withDockerHost("tcp://foo");
        assertThat(builder.isDockerHostSetExplicitly(), is(true));
    }

    @Test
    public void dockerHostSetExplicitlyOnSystemProperty() {
        Properties systemProperties = new Properties();
        systemProperties.put(DefaultDockerClientConfig.DOCKER_HOST, "tcp://foo");

        DefaultDockerClientConfig.Builder builder =  DefaultDockerClientConfig.createDefaultConfigBuilder(Collections.emptyMap(), systemProperties);

        assertThat(builder.isDockerHostSetExplicitly(), is(true));
    }

    @Test
    public void dockerHostSetExplicitlyOnEnv() {
        Map<String, String> env = new HashMap<>();
        env.put(DefaultDockerClientConfig.DOCKER_HOST, "tcp://foo");

        DefaultDockerClientConfig.Builder builder =  DefaultDockerClientConfig.createDefaultConfigBuilder(env, new Properties());

        assertThat(builder.isDockerHostSetExplicitly(), is(true));
    }

    @Test
    public void dockerHostSetExplicitlyIfSetToDefaultByUser() {
        Map<String, String> env = new HashMap<>();
        env.put(DefaultDockerClientConfig.DOCKER_HOST, DefaultDockerClientConfig.DEFAULT_DOCKER_HOST);

        DefaultDockerClientConfig.Builder builder =  DefaultDockerClientConfig.createDefaultConfigBuilder(env, new Properties());

        assertThat(builder.isDockerHostSetExplicitly(), is(true));
    }


    @Test
    public void testGetAuthConfigurationsFromDockerCfg() throws URISyntaxException, IOException {
        File cfgFile = new File(Resources.getResource("com.github.dockerjava.core/registry.v1").toURI());
        DockerConfigFile dockerConfigFile =
            DockerConfigFile.loadConfig(DockerClientConfig.getDefaultObjectMapper(), cfgFile.getAbsolutePath());
        DefaultDockerClientConfig clientConfig = new DefaultDockerClientConfig(URI.create(
            "unix://foo"), dockerConfigFile, cfgFile.getAbsolutePath(), "apiVersion", "registryUrl", "registryUsername", "registryPassword",
            "registryEmail", null);

        AuthConfigurations authConfigurations = clientConfig.getAuthConfigurations();
        assertThat(authConfigurations, notNullValue());
        assertThat(authConfigurations.getConfigs().get("https://test.docker.io/v1/"), notNullValue());

        AuthConfig authConfig = authConfigurations.getConfigs().get("https://test.docker.io/v1/");
        assertThat(authConfig.getUsername(), equalTo("user"));
        assertThat(authConfig.getPassword(), equalTo("password"));
    }

    @Test
    public void testGetAuthConfigurationsFromConfigJson() throws URISyntaxException, IOException {
        File cfgFile = new File(Resources.getResource("com.github.dockerjava.core/registry.v2").toURI());
        DockerConfigFile dockerConfigFile =
            DockerConfigFile.loadConfig(DockerClientConfig.getDefaultObjectMapper(), cfgFile.getAbsolutePath());
        DefaultDockerClientConfig clientConfig = new DefaultDockerClientConfig(URI.create(
            "unix://foo"), dockerConfigFile, cfgFile.getAbsolutePath(), "apiVersion", "registryUrl", "registryUsername", "registryPassword",
            "registryEmail", null);

        AuthConfigurations authConfigurations = clientConfig.getAuthConfigurations();
        assertThat(authConfigurations, notNullValue());
        assertThat(authConfigurations.getConfigs().get("https://test.docker.io/v2/"), notNullValue());

        AuthConfig authConfig = authConfigurations.getConfigs().get("https://test.docker.io/v2/");
        assertThat(authConfig.getUsername(), equalTo("user"));
        assertThat(authConfig.getPassword(), equalTo("password"));
    }

}
