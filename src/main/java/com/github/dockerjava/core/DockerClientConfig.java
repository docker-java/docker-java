   package com.github.dockerjava.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import com.google.common.base.Preconditions;

public class DockerClientConfig {
    private final URI uri;
    private final String version, username, password, email;
    private final Integer readTimeout;
    private final boolean loggingFilterEnabled;

    private DockerClientConfig(DockerClientConfigBuilder builder) {
        this.uri = builder.uri;
        this.version = builder.version;
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.readTimeout = builder.readTimeout;
        this.loggingFilterEnabled = builder.loggingFilterEnabled;
    }

    public URI getUri() {
        return uri;
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

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public boolean isLoggingFilterEnabled() {
        return loggingFilterEnabled;
    }

    public static Properties loadIncludedDockerProperties() {
        try {
            Properties p = new Properties();
            p.load(DockerClientConfig.class.getResourceAsStream("/docker.io.properties"));
            return p;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new Properties object containing values overridden from ${user.home}/.docker.io.properties
     * @param p The original set of properties to override
     * @return A copy of the original Properties with overridden values
     */
    public static Properties overrideDockerPropertiesWithSettingsFromUserHome(Properties p) {
        Properties overriddenProperties = new Properties();
        overriddenProperties.putAll(p);

        final File usersDockerPropertiesFile = new File(System.getProperty("user.home"), ".docker.io.properties");
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

    /**
     * Creates a new Properties object containing values overridden from the System properties
     * @param p The original set of properties to override
     * @return A copy of the original Properties with overridden values
     */
    public static Properties overrideDockerPropertiesWithSystemProperties(Properties p) {
        Properties overriddenProperties = new Properties();
        overriddenProperties.putAll(p);

        // TODO Add all values from system properties that begin with docker.io.*
        for (String s : new String[]{ "url", "version", "username", "password", "email", "readTimeout", "enableLoggingFilter"}) {
		    final String key = "docker.io." + s;
		    if (System.getProperties().containsKey(key)) {
			    overriddenProperties.setProperty(key, System.getProperty(key));
		    }
	    }
        return overriddenProperties;
    }

    public static DockerClientConfigBuilder createDefaultConfigBuilder() {
        Properties properties = loadIncludedDockerProperties();
        properties = overrideDockerPropertiesWithSettingsFromUserHome(properties);
        properties = overrideDockerPropertiesWithSystemProperties(properties);
        return new DockerClientConfigBuilder().withProperties(properties);
    }

    public static class DockerClientConfigBuilder {
        private URI uri;
        private String version, username, password, email;
        private Integer readTimeout;
        private boolean loggingFilterEnabled;

        public DockerClientConfigBuilder() {
        }

        /**
         * This will set all fields in the builder to those contained in the Properties object. The Properties object
         * should contain the following docker.io.* keys: url, version, username, password, and email. If
         * docker.io.readTimeout or docker.io.enableLoggingFilter are not contained, they will be set to 1000 and true,
         * respectively.
         *
         * @param p
         * @return
         */
        public DockerClientConfigBuilder withProperties(Properties p) {
            return withUri(p.getProperty("docker.io.url"))
                    .withVersion(p.getProperty("docker.io.version"))
                    .withUsername(p.getProperty("docker.io.username"))
                    .withPassword(p.getProperty("docker.io.password"))
                    .withEmail(p.getProperty("docker.io.email"))
                    .withReadTimeout(Integer.valueOf(p.getProperty("docker.io.readTimeout", "0")))
                    .withLoggingFilter(Boolean.valueOf(p.getProperty("docker.io.enableLoggingFilter", "true")));
        }

        public final DockerClientConfigBuilder withUri(String uri) {
        	Preconditions.checkNotNull(uri, "uri was not specified");
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
        public final DockerClientConfigBuilder withReadTimeout(Integer readTimeout) {
        	this.readTimeout = readTimeout;
            return this;
        }
        public final DockerClientConfigBuilder withLoggingFilter(boolean loggingFilterEnabled) {
            this.loggingFilterEnabled = loggingFilterEnabled;
            return this;
        }
        public DockerClientConfig build() {
            return new DockerClientConfig(this);
        }
    }
}
