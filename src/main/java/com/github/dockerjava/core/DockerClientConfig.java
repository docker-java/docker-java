package com.github.dockerjava.core;

import static com.google.common.base.Preconditions.checkNotNull;

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

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.core.NameParser.HostnameReposName;
import com.github.dockerjava.core.NameParser.ReposTag;

/**
 * Respects some of the docker CLI options. See https://docs.docker.com/engine/reference/commandline/cli/#environment-variables
 */
public class DockerClientConfig implements Serializable {

    private static final long serialVersionUID = -4307357472441531489L;

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

    private static final String DOCKER_CFG = ".dockercfg";

    private static final Set<String> CONFIG_KEYS = new HashSet<String>();

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
    }

    private final URI dockerHost;

    private final String registryUsername, registryPassword, registryEmail, registryUrl, dockerConfig, dockerCertPath;

    private final boolean dockerTlsVerify;

    private final RemoteApiVersion apiVersion;

    DockerClientConfig(URI dockerHost, String dockerConfig, String apiVersion, String registryUrl,
            String registryUsername, String registryPassword, String registryEmail, String dockerCertPath,
            boolean dockerTslVerify) {
        this.dockerHost = checkDockerHostScheme(dockerHost);
        this.dockerTlsVerify = dockerTslVerify;
        this.dockerCertPath = checkDockerCertPath(dockerTslVerify, dockerCertPath);
        this.dockerConfig = dockerConfig;
        this.apiVersion = RemoteApiVersion.parseConfigWithDefault(apiVersion);
        this.registryUsername = registryUsername;
        this.registryPassword = registryPassword;
        this.registryEmail = registryEmail;
        this.registryUrl = registryUrl;
    }

    private URI checkDockerHostScheme(URI dockerHost) {
        if ("tcp".equals(dockerHost.getScheme()) || "unix".equals(dockerHost.getScheme())) {
            return dockerHost;
        } else {
            throw new DockerClientException("Unsupported protocol scheme found: '" + dockerHost
                    + "'. Only 'tcp://' or 'unix://' supported.");
        }
    }

    private String checkDockerCertPath(boolean dockerTlsVerify, String dockerCertPath) {
        if (dockerTlsVerify) {
            if (StringUtils.isEmpty(dockerCertPath)) {
                throw new DockerClientException(
                        "Enabled TLS verification (DOCKER_TLS_VERIFY=1) but certifate path (DOCKER_CERT_PATH) is not defined.");
            } else {
                File certPath = new File(dockerCertPath);

                if (!certPath.exists()) {
                    throw new DockerClientException(
                            "Certificate path (DOCKER_CERT_PATH) '" + dockerCertPath + "' doesn't exist.");
                }

                if (certPath.isDirectory()) {
                    return dockerCertPath;
                } else {
                    throw new DockerClientException(
                            "Certificate path (DOCKER_CERT_PATH) '" + dockerCertPath + "' doesn't point to a directory.");
                }
            }
        } else {
            return dockerCertPath;
        }
    }

    private static Properties loadIncludedDockerProperties(Properties systemProperties) {
        try (InputStream is = DockerClientConfig.class.getResourceAsStream("/" + DOCKER_JAVA_PROPERTIES)) {
            Properties p = new Properties();
            p.load(is);
            replaceProperties(p, systemProperties);
            return p;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

        final File usersDockerPropertiesFile = new File(systemProperties.getProperty("user.home"), "."
                + DOCKER_JAVA_PROPERTIES);
        if (usersDockerPropertiesFile.isFile()) {
            try {
                final FileInputStream in = new FileInputStream(usersDockerPropertiesFile);
                try {
                    overriddenProperties.load(in);
                } finally {
                    in.close();
                }
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
            overriddenProperties.setProperty(DOCKER_HOST, env.get(DOCKER_HOST));
        }

        for (Map.Entry<String, String> envEntry : env.entrySet()) {
            String envKey = envEntry.getKey();
            if (CONFIG_KEYS.contains(envKey)) {
                overriddenProperties.setProperty(envKey, envEntry.getValue());
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

    public static DockerClientConfigBuilder createDefaultConfigBuilder() {
        return createDefaultConfigBuilder(System.getenv(), System.getProperties());
    }

    /**
     * Allows you to build the config without system environment interfering for more robust testing
     */
    static DockerClientConfigBuilder createDefaultConfigBuilder(Map<String, String> env, Properties systemProperties) {
        Properties properties = loadIncludedDockerProperties(systemProperties);
        properties = overrideDockerPropertiesWithSettingsFromUserHome(properties, systemProperties);
        properties = overrideDockerPropertiesWithEnv(properties, env);
        properties = overrideDockerPropertiesWithSystemProperties(properties, systemProperties);
        return new DockerClientConfigBuilder().withProperties(properties);
    }

    public URI getDockerHost() {
        return dockerHost;
    }

    public RemoteApiVersion getApiVersion() {
        return apiVersion;
    }

    public String getRegistryUsername() {
        return registryUsername;
    }

    public String getRegistryPassword() {
        return registryPassword;
    }

    public String getRegistryEmail() {
        return registryEmail;
    }

    public String getRegistryUrl() {
        return registryUrl;
    }

    public String getDockerConfig() {
        return dockerConfig;
    }

    public String getDockerCertPath() {
        return dockerCertPath;
    }

    public boolean getDockerTlsVerify() {
        return dockerTlsVerify;
    }

    private AuthConfig getAuthConfig() {
        AuthConfig authConfig = null;
        if (getRegistryUsername() != null && getRegistryPassword() != null && getRegistryEmail() != null
                && getRegistryUrl() != null) {
            authConfig = new AuthConfig()
                    .withUsername(getRegistryUsername())
                    .withPassword(getRegistryPassword())
                    .withEmail(getRegistryEmail())
                    .withRegistryAddress(getRegistryUrl());
        }
        return authConfig;
    }

    public AuthConfig effectiveAuthConfig(String imageName) {
        AuthConfig authConfig = null;

        File dockerCfgFile = new File(getDockerConfig() + File.separator + DOCKER_CFG);

        if (dockerCfgFile.exists() && dockerCfgFile.isFile() && imageName != null) {
            AuthConfigFile authConfigFile;
            try {
                authConfigFile = AuthConfigFile.loadConfig(dockerCfgFile);
            } catch (IOException e) {
                throw new DockerClientException("Failed to parse dockerCfgFile", e);
            }
            ReposTag reposTag = NameParser.parseRepositoryTag(imageName);
            HostnameReposName hostnameReposName = NameParser.resolveRepositoryName(reposTag.repos);

            authConfig = authConfigFile.resolveAuthConfig(hostnameReposName.hostname);
        }

        AuthConfig otherAuthConfig = getAuthConfig();

        if (otherAuthConfig != null) {
            authConfig = otherAuthConfig;
        }

        return authConfig;
    }

    public AuthConfigurations getAuthConfigurations() {
        File dockerCfgFile = new File(getDockerConfig() + File.separator + DOCKER_CFG);
        if (dockerCfgFile.exists() && dockerCfgFile.isFile()) {
            AuthConfigFile authConfigFile;
            try {
                authConfigFile = AuthConfigFile.loadConfig(dockerCfgFile);
            } catch (IOException e) {
                throw new DockerClientException("Failed to parse dockerCfgFile", e);
            }

            return authConfigFile.getAuthConfigurations();
        }

        return new AuthConfigurations();
    }

    // CHECKSTYLE:OFF
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        DockerClientConfig that = (DockerClientConfig) o;

        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    // CHECKSTYLE:ON

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static class DockerClientConfigBuilder {
        private URI dockerHost;

        private String apiVersion, registryUsername, registryPassword, registryEmail, registryUrl, dockerConfig,
                dockerCertPath;

        private boolean dockerTlsVerify;

        /**
         * This will set all fields in the builder to those contained in the Properties object. The Properties object should contain the
         * following docker-java configuration keys: DOCKER_HOST, DOCKER_TLS_VERIFY, api.version, registry.username, registry.password,
         * registry.email, DOCKER_CERT_PATH, and DOCKER_CONFIG.
         */
        public DockerClientConfigBuilder withProperties(Properties p) {
            return withDockerHost(p.getProperty(DOCKER_HOST)).withDockerTlsVerify(p.getProperty(DOCKER_TLS_VERIFY))
                    .withDockerConfig(p.getProperty(DOCKER_CONFIG)).withDockerCertPath(p.getProperty(DOCKER_CERT_PATH))
                    .withApiVersion(p.getProperty(API_VERSION)).withRegistryUsername(p.getProperty(REGISTRY_USERNAME))
                    .withRegistryPassword(p.getProperty(REGISTRY_PASSWORD))
                    .withRegistryEmail(p.getProperty(REGISTRY_EMAIL)).withRegistryUrl(p.getProperty(REGISTRY_URL));
        }

        /**
         * configure DOCKER_HOST
         */
        public final DockerClientConfigBuilder withDockerHost(String dockerHost) {
            checkNotNull(dockerHost, "uri was not specified");
            this.dockerHost = URI.create(dockerHost);
            return this;
        }

        public final DockerClientConfigBuilder withApiVersion(String apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        public final DockerClientConfigBuilder withRegistryUsername(String registryUsername) {
            this.registryUsername = registryUsername;
            return this;
        }

        public final DockerClientConfigBuilder withRegistryPassword(String registryPassword) {
            this.registryPassword = registryPassword;
            return this;
        }

        public final DockerClientConfigBuilder withRegistryEmail(String registryEmail) {
            this.registryEmail = registryEmail;
            return this;
        }

        public DockerClientConfigBuilder withRegistryUrl(String registryUrl) {
            this.registryUrl = registryUrl;
            return this;
        }

        public final DockerClientConfigBuilder withDockerCertPath(String dockerCertPath) {
            this.dockerCertPath = dockerCertPath;
            return this;
        }

        public final DockerClientConfigBuilder withDockerConfig(String dockerConfig) {
            this.dockerConfig = dockerConfig;
            return this;
        }

        public final DockerClientConfigBuilder withDockerTlsVerify(String dockerTlsVerify) {
            this.dockerTlsVerify = BooleanUtils.toBoolean(dockerTlsVerify.trim())
                    || BooleanUtils.toBoolean(dockerTlsVerify.trim(), "1", "0");
            return this;
        }

        public final DockerClientConfigBuilder withDockerTlsVerify(Boolean dockerTlsVerify) {
            this.dockerTlsVerify = dockerTlsVerify;
            return this;
        }

        public DockerClientConfig build() {
            return new DockerClientConfig(dockerHost, dockerConfig, apiVersion, registryUrl, registryUsername,
                    registryPassword, registryEmail, dockerCertPath, dockerTlsVerify);
        }
    }
}

