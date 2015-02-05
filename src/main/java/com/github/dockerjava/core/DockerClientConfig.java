package com.github.dockerjava.core;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.core.NameParser.HostnameReposName;
import com.github.dockerjava.core.NameParser.ReposTag;

public class DockerClientConfig implements Serializable {

	private static final long serialVersionUID = -4307357472441531489L;
	
	private static final String DOCKER_HOST_PROPERTY = "DOCKER_HOST";
    private static final String DOCKER_CERT_PATH_PROPERTY = "DOCKER_CERT_PATH";
    private static final String DOCKER_VERIFY_TLS_PROPERTY = "DOCKER_TLS_VERIFY";
    private static final String DOCKER_IO_URL_PROPERTY = "docker.io.url";
    private static final String DOCKER_IO_VERSION_PROPERTY = "docker.io.version";
    private static final String DOCKER_IO_USERNAME_PROPERTY = "docker.io.username";
    private static final String DOCKER_IO_PASSWORD_PROPERTY = "docker.io.password";
    private static final String DOCKER_IO_EMAIL_PROPERTY = "docker.io.email";
    private static final String DOCKER_IO_SERVER_ADDRESS_PROPERTY = "docker.io.serverAddress";
    private static final String DOCKER_IO_READ_TIMEOUT_PROPERTY = "docker.io.readTimeout";
    // this is really confusing, as there are two ways to spell it
    private static final String DOCKER_IO_ENABLE_LOGGING_FILTER_PROPERTY = "docker.io.enableLoggingFilter";
    private static final String DOCKER_IO_DOCKER_CERT_PATH_PROPERTY = "docker.io.dockerCertPath";
    private static final String DOCKER_IO_DOCKER_CFG_PATH_PROPERTY = "docker.io.dockerCfgPath";
    // connection pooling properties
    private static final String DOCKER_IO_MAX_PER_ROUTE_PROPERTY = "docker.io.perRouteConnections";
    private static final String DOCKER_IO_MAX_TOTAL_PROPERTY = "docker.io.totalConnections";
    /**
     * A map from the environment name to the interval name.
     */
    // Immutable ish
    private static final Map<String, String> ENV_NAME_TO_IO_NAME;
    static {
        Map<String, String> m = new HashMap<String, String>();
        m.put("DOCKER_URL", DOCKER_IO_URL_PROPERTY);
        m.put("DOCKER_VERSION", DOCKER_IO_VERSION_PROPERTY);
        m.put("DOCKER_USERNAME", DOCKER_IO_USERNAME_PROPERTY);
        m.put("DOCKER_PASSWORD", DOCKER_IO_PASSWORD_PROPERTY);
        m.put("DOCKER_EMAIL", DOCKER_IO_EMAIL_PROPERTY);
        m.put("DOCKER_SERVER_ADDRESS", DOCKER_IO_SERVER_ADDRESS_PROPERTY);
        m.put("DOCKER_READ_TIMEOUT", DOCKER_IO_READ_TIMEOUT_PROPERTY);
        m.put("DOCKER_LOGGING_FILTER_ENABLED", DOCKER_IO_ENABLE_LOGGING_FILTER_PROPERTY);
        m.put(DOCKER_CERT_PATH_PROPERTY, DOCKER_IO_DOCKER_CERT_PATH_PROPERTY);
        m.put("DOCKER_CFG_PATH", DOCKER_IO_DOCKER_CFG_PATH_PROPERTY);
        ENV_NAME_TO_IO_NAME = Collections.unmodifiableMap(m);
    }

    private static final String DOCKER_IO_PROPERTIES_PROPERTY = "docker.io.properties";
    private URI uri;
    private final String version, username, password, email, serverAddress, dockerCfgPath;
    private final Integer readTimeout;
    private final boolean loggingFilterEnabled;
    private final SSLConfig sslConfig;
    
    private final int maxTotalConnections;
    private final int maxPerRouteConnections;

    DockerClientConfig(URI uri, String version, String username, String password, String email, String serverAddress,
                       String dockerCfgPath, Integer readTimeout, boolean loggingFilterEnabled, SSLConfig sslConfig,
                       int maxTotalConns, int maxPerRouteConns) {
        this.uri = uri;
        this.version = version;
        this.username = username;
        this.password = password;
        this.email = email;
        this.serverAddress = serverAddress;
        this.dockerCfgPath = dockerCfgPath;
        this.readTimeout = readTimeout;
        this.loggingFilterEnabled = loggingFilterEnabled;
        this.sslConfig = sslConfig;
        this.maxTotalConnections = maxTotalConns;
        this.maxPerRouteConnections = maxPerRouteConns;
    }

    private static Properties loadIncludedDockerProperties(Properties systemProperties) {
        try {
            Properties p = new Properties();
            p.load(DockerClientConfig.class.getResourceAsStream("/" + DOCKER_IO_PROPERTIES_PROPERTY));
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
     * @param p The original set of properties to override
     * @return A copy of the original Properties with overridden values
     */
    private static Properties overrideDockerPropertiesWithSettingsFromUserHome(Properties p, Properties systemProperties) {
        Properties overriddenProperties = new Properties();
        overriddenProperties.putAll(p);

        final File usersDockerPropertiesFile = new File(systemProperties.getProperty("user.home"), "." + DOCKER_IO_PROPERTIES_PROPERTY);
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
        if (env.containsKey(DOCKER_HOST_PROPERTY)) {
            overriddenProperties.setProperty(DOCKER_IO_URL_PROPERTY, env.get(DOCKER_HOST_PROPERTY).replace("tcp", protocol(env)));
        }

        for (Map.Entry<String, String> envEntry : env.entrySet()) {
            String envKey = envEntry.getKey();
            if (ENV_NAME_TO_IO_NAME.containsKey(envKey)) {
                overriddenProperties.setProperty(ENV_NAME_TO_IO_NAME.get(envKey), envEntry.getValue());
            }
        }

        return overriddenProperties;
    }

    private static String protocol(Map<String, String> env) {
        // if this is set, we assume we need SSL
        return env.containsKey(DOCKER_CERT_PATH_PROPERTY) || "1".equals(env.get(DOCKER_VERIFY_TLS_PROPERTY)) ? "https" : "http";
    }

    /**
     * Creates a new Properties object containing values overridden from the System properties
     *
     * @param p The original set of properties to override
     * @return A copy of the original Properties with overridden values
     */
    private static Properties overrideDockerPropertiesWithSystemProperties(Properties p, Properties systemProperties) {
        Properties overriddenProperties = new Properties();
        overriddenProperties.putAll(p);

        for (String key : new String[]{
                DOCKER_IO_URL_PROPERTY,
                DOCKER_IO_VERSION_PROPERTY,
                DOCKER_IO_USERNAME_PROPERTY,
                DOCKER_IO_PASSWORD_PROPERTY,
                DOCKER_IO_EMAIL_PROPERTY,
                DOCKER_IO_SERVER_ADDRESS_PROPERTY,
                DOCKER_IO_READ_TIMEOUT_PROPERTY,
                DOCKER_IO_ENABLE_LOGGING_FILTER_PROPERTY,
                DOCKER_IO_DOCKER_CERT_PATH_PROPERTY,
                DOCKER_IO_DOCKER_CFG_PATH_PROPERTY,
        }) {
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

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
		this.uri = uri;
	}

    public String getVersion() {
        return version;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public boolean isLoggingFilterEnabled() {
        return loggingFilterEnabled;
    }

    public SSLConfig getSslConfig() {
      return sslConfig;
    }

    public String getDockerCfgPath() {
        return dockerCfgPath;
    }
    
    private AuthConfig getAuthConfig() {
    	AuthConfig authConfig = null;
    	if (getUsername() != null && getPassword() != null && getEmail() != null
    			&& getServerAddress() != null) {
    		authConfig = new AuthConfig();
    		authConfig.setUsername(getUsername());
    		authConfig.setPassword(getPassword());
    		authConfig.setEmail(getEmail());
    		authConfig.setServerAddress(getServerAddress());
    	} 
    	return authConfig;
    }
    
    public AuthConfig effectiveAuthConfig(String imageName) {
		AuthConfig authConfig = null;

		String dockerCfgFile = getDockerCfgPath();

		if (dockerCfgFile != null && imageName != null) {
			AuthConfigFile authConfigFile;
			try {
				authConfigFile = AuthConfigFile.loadConfig(new File(
						dockerCfgFile));
			} catch (IOException e) {
				throw new DockerClientException(
						"Failed to parse dockerCfgFile", e);
			}
			ReposTag reposTag = NameParser.parseRepositoryTag(imageName);
			HostnameReposName hostnameReposName = NameParser
					.resolveRepositoryName(reposTag.repos);
			
			authConfig = authConfigFile
					.resolveAuthConfig(hostnameReposName.hostname);	
		}
		
		AuthConfig _authConfig = getAuthConfig();
		
		if(_authConfig != null) authConfig = _authConfig;
		
		return authConfig;
	}

  @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DockerClientConfig that = (DockerClientConfig) o;

        if (loggingFilterEnabled != that.loggingFilterEnabled) return false;
        if (sslConfig != null ? !sslConfig.equals(that.sslConfig) : that.sslConfig != null)
            return false;
        if (dockerCfgPath != null ? !dockerCfgPath.equals(that.dockerCfgPath) : that.dockerCfgPath != null)
            return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (readTimeout != null ? !readTimeout.equals(that.readTimeout) : that.readTimeout != null) return false;
        if (serverAddress != null ? !serverAddress.equals(that.serverAddress) : that.serverAddress != null)
            return false;
        if (uri != null ? !uri.equals(that.uri) : that.uri != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uri != null ? uri.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (serverAddress != null ? serverAddress.hashCode() : 0);
        result = 31 * result + (dockerCfgPath != null ? dockerCfgPath.hashCode() : 0);
        result = 31 * result + (sslConfig != null ? sslConfig.hashCode() : 0);
        result = 31 * result + (readTimeout != null ? readTimeout.hashCode() : 0);
        result = 31 * result + (loggingFilterEnabled ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DockerClientConfig{" +
                "uri=" + uri +
                ", version='" + version + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", dockerCfgPath='" + dockerCfgPath + '\'' +
                ", sslConfig='" + sslConfig + '\'' +
                ", readTimeout=" + readTimeout +
                ", loggingFilterEnabled=" + loggingFilterEnabled +
                '}';
    }

    public static class DockerClientConfigBuilder {
        private URI uri;
        private String version, username, password, email, serverAddress, dockerCfgPath;
        private Integer readTimeout, maxTotalConnections, maxPerRouteConnections;
        private boolean loggingFilterEnabled;
        private SSLConfig sslConfig;

        /**
         * This will set all fields in the builder to those contained in the Properties object. The Properties object
         * should contain the following docker.io.* keys: url, version, username, password, email, dockerCertPath, and
         * dockerCfgPath. If docker.io.readTimeout or docker.io.enableLoggingFilter are not contained, they will be set
         *  to 1000 and true, respectively.
         */
        public DockerClientConfigBuilder withProperties(Properties p) {
            return withUri(p.getProperty(DOCKER_IO_URL_PROPERTY))
                    .withVersion(p.getProperty(DOCKER_IO_VERSION_PROPERTY))
                    .withUsername(p.getProperty(DOCKER_IO_USERNAME_PROPERTY))
                    .withPassword(p.getProperty(DOCKER_IO_PASSWORD_PROPERTY))
                    .withEmail(p.getProperty(DOCKER_IO_EMAIL_PROPERTY))
                    .withServerAddress(p.getProperty(DOCKER_IO_SERVER_ADDRESS_PROPERTY))
                    .withReadTimeout(Integer.valueOf(p.getProperty(DOCKER_IO_READ_TIMEOUT_PROPERTY, "0")))
                    .withLoggingFilter(Boolean.valueOf(p.getProperty(DOCKER_IO_ENABLE_LOGGING_FILTER_PROPERTY, "true")))
                    .withDockerCertPath(p.getProperty(DOCKER_IO_DOCKER_CERT_PATH_PROPERTY))
                    .withDockerCfgPath(p.getProperty(DOCKER_IO_DOCKER_CFG_PATH_PROPERTY))
                    .withMaxPerRouteConnections(Integer.valueOf(p.getProperty(DOCKER_IO_MAX_PER_ROUTE_PROPERTY, "2")))
                    .withMaxTotalConnections(Integer.valueOf(p.getProperty(DOCKER_IO_MAX_TOTAL_PROPERTY, "20")))
                    ;
        }

        public final DockerClientConfigBuilder withUri(String uri) {
            checkNotNull(uri, "uri was not specified");
            this.uri = URI.create(uri);
            return this;
        }

        public final DockerClientConfigBuilder withVersion(String version) {
            this.version = version;
            return this;
        }

        public final DockerClientConfigBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public final DockerClientConfigBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public final DockerClientConfigBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public DockerClientConfigBuilder withServerAddress(String serverAddress) {
            this.serverAddress = serverAddress;
            return this;
        }

        public final DockerClientConfigBuilder withReadTimeout(Integer readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }
        
        public final DockerClientConfigBuilder withMaxTotalConnections(Integer maxTotalConnections) {
            this.maxTotalConnections = maxTotalConnections;
            return this;
        }
        
        public final DockerClientConfigBuilder withMaxPerRouteConnections(Integer maxPerRouteConnections) {
            this.maxPerRouteConnections = maxPerRouteConnections;
            return this;
        }

        public final DockerClientConfigBuilder withLoggingFilter(boolean loggingFilterEnabled) {
            this.loggingFilterEnabled = loggingFilterEnabled;
            return this;
        }

        public final DockerClientConfigBuilder withDockerCertPath(String dockerCertPath) {
            this.sslConfig = new LocalDirectorySSLConfig(dockerCertPath);
            return this;
        }

        public final DockerClientConfigBuilder withDockerCfgPath(String dockerCfgPath) {
            this.dockerCfgPath = dockerCfgPath;
            return this;
        }


        public final DockerClientConfigBuilder withSSLConfig(SSLConfig config) {
            this.sslConfig = config;
            return this;
        }

        public DockerClientConfig build() {
            return new DockerClientConfig(
                    uri,
                    version,
                    username,
                    password,
                    email,
                    serverAddress,
                    dockerCfgPath,
                    readTimeout,
                    loggingFilterEnabled,
                    sslConfig,
                    maxTotalConnections,
                    maxPerRouteConnections
            );
        }
    }

    public int getMaxTotalConnections() {
      return maxTotalConnections;
    }

    public int getMaxPerRoutConnections() {
      return maxPerRouteConnections;
    }
}
