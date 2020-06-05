package com.github.dockerjava.core;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.core.NameParser.HostnameReposName;
import com.github.dockerjava.core.NameParser.ReposTag;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang.BooleanUtils.isTrue;

/**
 * Respects some of the docker CLI options. See https://docs.docker.com/engine/reference/commandline/cli/#environment-variables
 */
public class DefaultDockerClientConfig implements Serializable, DockerClientConfig {

    private static final long serialVersionUID = 1L;

    public static final String DOCKER_HOST = "DOCKER_HOST";

    public static final String DOCKER_TLS_VERIFY = "DOCKER_TLS_VERIFY";

    public static final String DOCKER_CONFIG = "DOCKER_CONFIG";

    public static final String DOCKER_CERT_PATH = "DOCKER_CERT_PATH";

    public static final String API_VERSION = "api.version";

    public static final String REGISTRY_USERNAME = "registry.username";

    public static final String REGISTRY_PASSWORD = "registry.password";

    public static final String REGISTRY_EMAIL = "registry.email";

    public static final String REGISTRY_URL = "registry.url";

    private static final String DOCKER_JAVA_PROPERTIES = "docker-java.properties";

    private static final Set<String> CONFIG_KEYS = new HashSet<>();

    static final Properties DEFAULT_PROPERTIES = new Properties();

    static {
        CONFIG_KEYS.add(DOCKER_HOST);
        CONFIG_KEYS.add(DOCKER_TLS_VERIFY);
        CONFIG_KEYS.add(DOCKER_CONFIG);
        CONFIG_KEYS.add(DOCKER_CERT_PATH);
        CONFIG_KEYS.add(API_VERSION);
        CONFIG_KEYS.add(REGISTRY_USERNAME);
        CONFIG_KEYS.add(REGISTRY_PASSWORD);
        CONFIG_KEYS.add(REGISTRY_EMAIL);
        CONFIG_KEYS.add(REGISTRY_URL);

        DEFAULT_PROPERTIES.put(DOCKER_HOST, "unix:///var/run/docker.sock");
        DEFAULT_PROPERTIES.put(DOCKER_CONFIG, "${user.home}/.docker");
        DEFAULT_PROPERTIES.put(REGISTRY_URL, "https://index.docker.io/v1/");
        DEFAULT_PROPERTIES.put(REGISTRY_USERNAME, "${user.name}");
    }

    private final URI dockerHost;

    private final String registryUsername, registryPassword, registryEmail, registryUrl, dockerConfigPath;

    private final SSLConfig sslConfig;

    private final RemoteApiVersion apiVersion;

    private DockerConfigFile dockerConfig = null;

    DefaultDockerClientConfig(URI dockerHost, String dockerConfigPath, String apiVersion, String registryUrl,
            String registryUsername, String registryPassword, String registryEmail, SSLConfig sslConfig) {
        this.dockerHost = checkDockerHostScheme(dockerHost);
        this.dockerConfigPath = dockerConfigPath;
        this.apiVersion = RemoteApiVersion.parseConfigWithDefault(apiVersion);
        this.sslConfig = sslConfig;
        this.registryUsername = registryUsername;
        this.registryPassword = registryPassword;
        this.registryEmail = registryEmail;
        this.registryUrl = registryUrl;
    }

    private URI checkDockerHostScheme(URI dockerHost) {
        switch (dockerHost.getScheme()) {
            case "tcp":
            case "unix":
            case "npipe":
                return dockerHost;
            default:
                throw new DockerClientException("Unsupported protocol scheme found: '" + dockerHost);
        }
    }

    private static Properties loadIncludedDockerProperties(Properties systemProperties) {
        Properties p = new Properties();
        p.putAll(DEFAULT_PROPERTIES);
        try (InputStream is = DefaultDockerClientConfig.class.getResourceAsStream("/" + DOCKER_JAVA_PROPERTIES)) {
            if (is != null) {
                p.load(is);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        replaceProperties(p, systemProperties);
        return p;
    }

    private static void replaceProperties(Properties properties, Properties replacements) {
        for (Object objectKey : properties.keySet()) {
            String key = objectKey.toString();
            properties.setProperty(key, replaceProperties(properties.getProperty(key), replacements));
        }
    }

    private static String replaceProperties(String s, Properties replacements) {
        for (Map.Entry<Object, Object> entry : replacements.entrySet()) {
            String key = "${" + entry.getKey() + "}";
            while (s.contains(key)) {
                s = s.replace(key, String.valueOf(entry.getValue()));
            }
        }
        return s;
    }

    /**
     * Creates a new Properties object containing values overridden from ${user.home}/.docker.io.properties
     *
     * @param p
     *            The original set of properties to override
     * @return A copy of the original Properties with overridden values
     */
    private static Properties overrideDockerPropertiesWithSettingsFromUserHome(Properties p, Properties systemProperties) {
        Properties overriddenProperties = new Properties();
        overriddenProperties.putAll(p);

        final File usersDockerPropertiesFile = new File(systemProperties.getProperty("user.home"),
                "." + DOCKER_JAVA_PROPERTIES);
        if (usersDockerPropertiesFile.isFile()) {
            try (FileInputStream in = new FileInputStream(usersDockerPropertiesFile)) {
                overriddenProperties.load(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return overriddenProperties;
    }

    private static Properties overrideDockerPropertiesWithEnv(Properties properties, Map<String, String> env) {
        Properties overriddenProperties = new Properties();
        overriddenProperties.putAll(properties);

        // special case which is a sensible default
        if (env.containsKey(DOCKER_HOST)) {
            String value = env.get(DOCKER_HOST);
            if (value != null && value.trim().length() != 0) {
                overriddenProperties.setProperty(DOCKER_HOST, value);
            }
        }

        for (Map.Entry<String, String> envEntry : env.entrySet()) {
            String envKey = envEntry.getKey();
            if (CONFIG_KEYS.contains(envKey)) {
                String value = envEntry.getValue();
                if (value != null && value.trim().length() != 0) {
                    overriddenProperties.setProperty(envKey, value);
                }
            }
        }

        return overriddenProperties;
    }

    /**
     * Creates a new Properties object containing values overridden from the System properties
     *
     * @param p
     *            The original set of properties to override
     * @return A copy of the original Properties with overridden values
     */
    private static Properties overrideDockerPropertiesWithSystemProperties(Properties p, Properties systemProperties) {
        Properties overriddenProperties = new Properties();
        overriddenProperties.putAll(p);

        for (String key : CONFIG_KEYS) {
            if (systemProperties.containsKey(key)) {
                overriddenProperties.setProperty(key, systemProperties.getProperty(key));
            }
        }
        return overriddenProperties;
    }

    public static Builder createDefaultConfigBuilder() {
        return createDefaultConfigBuilder(System.getenv(), (Properties) System.getProperties().clone());
    }

    /**
     * Allows you to build the config without system environment interfering for more robust testing
     */
    static Builder createDefaultConfigBuilder(Map<String, String> env, Properties systemProperties) {
        Properties properties = loadIncludedDockerProperties(systemProperties);
        properties = overrideDockerPropertiesWithSettingsFromUserHome(properties, systemProperties);
        properties = overrideDockerPropertiesWithEnv(properties, env);
        properties = overrideDockerPropertiesWithSystemProperties(properties, systemProperties);
        return new Builder().withProperties(properties);
    }

    @Override
    public URI getDockerHost() {
        return dockerHost;
    }

    @Override
    public RemoteApiVersion getApiVersion() {
        return apiVersion;
    }

    @Override
    public String getRegistryUsername() {
        return registryUsername;
    }

    @Override
    public String getRegistryPassword() {
        return registryPassword;
    }

    @Override
    public String getRegistryEmail() {
        return registryEmail;
    }

    @Override
    public String getRegistryUrl() {
        return registryUrl;
    }

    @CheckForNull
    public String getDockerConfigPath() {
        return dockerConfigPath;
    }

    @Nonnull
    public DockerConfigFile getDockerConfig() {
        if (dockerConfig == null) {
            try {
                dockerConfig = DockerConfigFile.loadConfig(getObjectMapper(), getDockerConfigPath());
            } catch (IOException e) {
                throw new DockerClientException("Failed to parse docker configuration file", e);
            }
        }
        return dockerConfig;
    }

    private AuthConfig getAuthConfig() {
        AuthConfig authConfig = null;
        if (getRegistryUsername() != null && getRegistryPassword() != null && getRegistryUrl() != null) {
            authConfig = new AuthConfig()
                    .withUsername(getRegistryUsername())
                    .withPassword(getRegistryPassword())
                    .withEmail(getRegistryEmail())
                    .withRegistryAddress(getRegistryUrl());
        }
        return authConfig;
    }

    @Override
    public AuthConfig effectiveAuthConfig(String imageName) {
        AuthConfig authConfig = getAuthConfig();

        if (authConfig != null) {
            return authConfig;
        }

        DockerConfigFile dockerCfg = getDockerConfig();

        ReposTag reposTag = NameParser.parseRepositoryTag(imageName);
        HostnameReposName hostnameReposName = NameParser.resolveRepositoryName(reposTag.repos);

        return dockerCfg.resolveAuthConfig(hostnameReposName.hostname);
    }

    @Override
    public AuthConfigurations getAuthConfigurations() {
        return getDockerConfig().getAuthConfigurations();
    }

    @Override
    public SSLConfig getSSLConfig() {
        return sslConfig;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static class Builder {
        private URI dockerHost;

        private String apiVersion, registryUsername, registryPassword, registryEmail, registryUrl, dockerConfig,
                dockerCertPath;

        private Boolean dockerTlsVerify;

        private SSLConfig customSslConfig = null;

        /**
         * This will set all fields in the builder to those contained in the Properties object. The Properties object should contain the
         * following docker-java configuration keys: DOCKER_HOST, DOCKER_TLS_VERIFY, api.version, registry.username, registry.password,
         * registry.email, DOCKER_CERT_PATH, and DOCKER_CONFIG.
         */
        public Builder withProperties(Properties p) {
            return withDockerHost(p.getProperty(DOCKER_HOST))
                    .withDockerTlsVerify(p.getProperty(DOCKER_TLS_VERIFY))
                    .withDockerConfig(p.getProperty(DOCKER_CONFIG))
                    .withDockerCertPath(p.getProperty(DOCKER_CERT_PATH))
                    .withApiVersion(p.getProperty(API_VERSION))
                    .withRegistryUsername(p.getProperty(REGISTRY_USERNAME))
                    .withRegistryPassword(p.getProperty(REGISTRY_PASSWORD))
                    .withRegistryEmail(p.getProperty(REGISTRY_EMAIL))
                    .withRegistryUrl(p.getProperty(REGISTRY_URL));
        }

        /**
         * configure DOCKER_HOST
         */
        public final Builder withDockerHost(String dockerHost) {
            checkNotNull(dockerHost, "uri was not specified");
            this.dockerHost = URI.create(dockerHost);
            return this;
        }

        public final Builder withApiVersion(RemoteApiVersion apiVersion) {
            this.apiVersion = apiVersion.getVersion();
            return this;
        }

        public final Builder withApiVersion(String apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        public final Builder withRegistryUsername(String registryUsername) {
            this.registryUsername = registryUsername;
            return this;
        }

        public final Builder withRegistryPassword(String registryPassword) {
            this.registryPassword = registryPassword;
            return this;
        }

        public final Builder withRegistryEmail(String registryEmail) {
            this.registryEmail = registryEmail;
            return this;
        }

        public Builder withRegistryUrl(String registryUrl) {
            this.registryUrl = registryUrl;
            return this;
        }

        public final Builder withDockerCertPath(String dockerCertPath) {
            this.dockerCertPath = dockerCertPath;
            return this;
        }

        public final Builder withDockerConfig(String dockerConfig) {
            this.dockerConfig = dockerConfig;
            return this;
        }

        public final Builder withDockerTlsVerify(String dockerTlsVerify) {
            if (dockerTlsVerify != null) {
                String trimmed = dockerTlsVerify.trim();
                this.dockerTlsVerify = "true".equalsIgnoreCase(trimmed) || "1".equals(trimmed);
            } else {
                this.dockerTlsVerify = false;
            }
            return this;
        }

        public final Builder withDockerTlsVerify(Boolean dockerTlsVerify) {
            this.dockerTlsVerify = dockerTlsVerify;
            return this;
        }

        /**
         * Overrides the default {@link SSLConfig} that is used when calling {@link Builder#withDockerTlsVerify(java.lang.Boolean)} and
         * {@link Builder#withDockerCertPath(String)}. This way it is possible to pass a custom {@link SSLConfig} to the resulting
         * {@link DockerClientConfig} that may be created by other means than the local file system.
         */
        public final Builder withCustomSslConfig(SSLConfig customSslConfig) {
            this.customSslConfig = customSslConfig;
            return this;
        }

        public DefaultDockerClientConfig build() {

            SSLConfig sslConfig = null;

            if (customSslConfig == null) {
                if (isTrue(dockerTlsVerify)) {
                    dockerCertPath = checkDockerCertPath(dockerCertPath);
                    sslConfig = new LocalDirectorySSLConfig(dockerCertPath);
                }
            } else {
                sslConfig = customSslConfig;
            }

            return new DefaultDockerClientConfig(dockerHost, dockerConfig, apiVersion, registryUrl, registryUsername,
                    registryPassword, registryEmail, sslConfig);
        }

        private String checkDockerCertPath(String dockerCertPath) {
            if (StringUtils.isEmpty(dockerCertPath)) {
                throw new DockerClientException(
                        "Enabled TLS verification (DOCKER_TLS_VERIFY=1) but certifate path (DOCKER_CERT_PATH) is not defined.");
            }

            File certPath = new File(dockerCertPath);

            if (!certPath.exists()) {
                throw new DockerClientException(
                        "Enabled TLS verification (DOCKER_TLS_VERIFY=1) but certificate path (DOCKER_CERT_PATH) '"
                                + dockerCertPath + "' doesn't exist.");
            } else if (!certPath.isDirectory()) {
                throw new DockerClientException(
                        "Enabled TLS verification (DOCKER_TLS_VERIFY=1) but certificate path (DOCKER_CERT_PATH) '"
                                + dockerCertPath + "' doesn't point to a directory.");
            }

            return dockerCertPath;
        }
    }
}
