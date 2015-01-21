package com.github.dockerjava.core;

import com.github.dockerjava.api.model.AuthConfig;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.testng.Assert.assertEquals;

public class DockerClientConfigTest {

    public static final DockerClientConfig EXAMPLE_CONFIG = newExampleConfig();

    private static DockerClientConfig newExampleConfig() {
        return new DockerClientConfig(URI.create("http://foo"), "bar", "baz", "qux", "blam", "wham", "flam", 877, false, new LocalDirectorySSLConfig("flim"), 20, 2);
    }

    @Test
    public void string() throws Exception {
        assertEquals("DockerClientConfig{uri=http://foo, version='bar', username='baz', password='qux', email='blam', serverAddress='wham', dockerCfgPath='flam', sslConfig='LocalDirectorySSLConfig{dockerCertPath=flim}', readTimeout=877, loggingFilterEnabled=false}",
                EXAMPLE_CONFIG.toString());
    }

    @Test
    public void equals() throws Exception {
        assertEquals(EXAMPLE_CONFIG, newExampleConfig());
    }

    @Test
    public void environmentDockerHost() throws Exception {

        // given docker host in env
        Map<String, String> env = new HashMap<String, String>();
        env.put("DOCKER_HOST", "tcp://baz:8768");
        // and it looks to be SSL disabled
        env.remove("DOCKER_CERT_PATH");

        // when you build a config
        DockerClientConfig config = buildConfig(env, new Properties());

        // then the URL is that value with "http" instead of "tcp"
        assertEquals(config.getUri(), URI.create("http://baz:8768"));
    }

    @Test
    public void environmentDockerHostHttpsAutoDetectByCertPath() throws Exception {

        // given docker host in env
        Map<String, String> env = new HashMap<String, String>(System.getenv());
        env.put("DOCKER_HOST", "tcp://bar:8768");
        // and it looks to be SSL enabled
        env.put("DOCKER_CERT_PATH", "any value");

        // when you build a config
        DockerClientConfig config = buildConfig(env, new Properties());

        // then the URL is that value with "tcp" changed to "https"
        assertEquals(config.getUri(), URI.create("https://bar:8768"));
    }

    @Test
    public void environmentDockerHostHttpsAutoDetectByTlsVerify() throws Exception {

        // given docker host in env
        Map<String, String> env = new HashMap<String, String>(System.getenv());
        env.put("DOCKER_HOST", "tcp://bar:8768");
        // and it looks to be SSL enabled
        env.put("DOCKER_TLS_VERIFY", "1");

        // when you build a config
        DockerClientConfig config = buildConfig(env, new Properties());

        // then the URL is that value with "tcp" changed to "https"
        assertEquals(config.getUri(), URI.create("https://bar:8768"));
    }

    @Test
    public void environmentDockerHostWithInvalidTlsVerify() throws Exception {

        // given docker host in env
        Map<String, String> env = new HashMap<String, String>(System.getenv());
        env.put("DOCKER_HOST", "tcp://bar:8768");
        // and it looks to be SSL disabled
        env.remove("DOCKER_CERT_PATH");
        // and it has an invalid TLS_VERIFY value
        env.put("DOCKER_TLS_VERIFY", "any value different from '1'");

        // when you build a config
        DockerClientConfig config = buildConfig(env, new Properties());

        // then the URL is that value with "tcp" changed to "https"
        assertEquals(config.getUri(), URI.create("http://bar:8768"));
    }

    @Test
    public void environmentDockerHostWithInvalidTlsVerifyButWithCertPath() throws Exception {

        // given docker host in env
        Map<String, String> env = new HashMap<String, String>(System.getenv());
        env.put("DOCKER_HOST", "tcp://bar:8768");
        // and it looks to be SSL enabled
        env.put("DOCKER_CERT_PATH", "any value");
        // and it has an invalid TLS_VERIFY value
        env.put("DOCKER_TLS_VERIFY", "any value different from '1'");

        // when you build a config
        DockerClientConfig config = buildConfig(env, new Properties());

        // then the URL is that value with "tcp" changed to "https"
        assertEquals(config.getUri(), URI.create("https://bar:8768"));
    }

    @Test
    public void environment() throws Exception {

        // given a default config in env properties
        Map<String, String> env = new HashMap<String, String>();
        env.put("DOCKER_URL", "http://foo");
        env.put("DOCKER_VERSION", "bar");
        env.put("DOCKER_USERNAME", "baz");
        env.put("DOCKER_PASSWORD", "qux");
        env.put("DOCKER_EMAIL", "blam");
        env.put("DOCKER_SERVER_ADDRESS", "wham");
        env.put("DOCKER_CERT_PATH", "flim");
        env.put("DOCKER_CFG_PATH", "flam");
        env.put("DOCKER_READ_TIMEOUT", "877");
        env.put("DOCKER_LOGGING_FILTER_ENABLED", "false");

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
        DockerClientConfig config = buildConfig(Collections.<String, String>emptyMap(), systemProperties);

        // then the cert path is as expected
        assertEquals(config.getUri(), URI.create("https://localhost:2376"));
        assertEquals(config.getUsername(), "someUserName");
        assertEquals(config.getServerAddress(), AuthConfig.DEFAULT_SERVER_ADDRESS);
        assertEquals(config.getVersion(), null);
        assertEquals(config.isLoggingFilterEnabled(), true);
        assertEquals(config.getDockerCfgPath(), "someHomeDir/.dockercfg");
        assertEquals( ((LocalDirectorySSLConfig)config.getSslConfig()).getDockerCertPath(), "someHomeDir/.docker");
    }

    @Test
    public void systemProperties() throws Exception {

        // given system properties based on the example
        Properties systemProperties = new Properties();
        systemProperties.setProperty("docker.io.url", "http://foo");
        systemProperties.setProperty("docker.io.version", "bar");
        systemProperties.setProperty("docker.io.username", "baz");
        systemProperties.setProperty("docker.io.password", "qux");
        systemProperties.setProperty("docker.io.email", "blam");
        systemProperties.setProperty("docker.io.serverAddress", "wham");
        systemProperties.setProperty("docker.io.dockerCertPath", "flim");
        systemProperties.setProperty("docker.io.dockerCfgPath", "flam");
        systemProperties.setProperty("docker.io.readTimeout", "877");
        systemProperties.setProperty("docker.io.enableLoggingFilter", "false");

        // when you build new config
        DockerClientConfig config = buildConfig(Collections.<String, String>emptyMap(), systemProperties);

        // then it is the same as the example
        assertEquals(config, EXAMPLE_CONFIG);

    }
}