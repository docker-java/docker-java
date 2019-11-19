package com.github.dockerjava.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DockerConfigFile {
    private static final String DOCKER_LEGACY_CFG = ".dockercfg";
    private static final String DOCKER_CFG = "config.json";

    private static final TypeReference<Map<String, AuthConfig>> CONFIG_MAP_TYPE = new TypeReference<Map<String, AuthConfig>>() {
    };

    @JsonProperty
    private final Map<String, AuthConfig> auths;

    public DockerConfigFile() {
        this(new HashMap<>());
    }

    private DockerConfigFile(Map<String, AuthConfig> authConfigMap) {
        auths = authConfigMap;
    }

    @Nonnull
    public Map<String, AuthConfig> getAuths() {
        return auths;
    }

    void addAuthConfig(AuthConfig config) {
        auths.put(config.getRegistryAddress(), config);
    }

    @CheckForNull
    public AuthConfig resolveAuthConfig(@CheckForNull String hostname) {
        if (StringUtils.isEmpty(hostname) || AuthConfig.DEFAULT_SERVER_ADDRESS.equals(hostname)) {
            return auths.get(AuthConfig.DEFAULT_SERVER_ADDRESS);
        }

        AuthConfig c = auths.get(hostname);
        if (c != null) {
            return c;
        }

        // Maybe they have a legacy config file, we will iterate the keys converting
        // them to the new format and testing
        String normalizedHostname = convertToHostname(hostname);
        for (Map.Entry<String, AuthConfig> entry : auths.entrySet()) {
            String registry = entry.getKey();
            AuthConfig config = entry.getValue();
            if (convertToHostname(registry).equals(normalizedHostname)) {
                return config;
            }
        }

        return null;
    }

    @Nonnull
    public AuthConfigurations getAuthConfigurations() {
        final AuthConfigurations authConfigurations = new AuthConfigurations();
        for (Map.Entry<String, AuthConfig> authConfigEntry : auths.entrySet()) {
            authConfigurations.addConfig(authConfigEntry.getValue());
        }

        return authConfigurations;
    }

    // CHECKSTYLE:OFF
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((auths == null) ? 0 : auths.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DockerConfigFile other = (DockerConfigFile) obj;
        if (auths == null) {
            if (other.auths != null)
                return false;
        } else if (!auths.equals(other.auths))
            return false;
        return true;
    }

    // CHECKSTYLE:ON

    @Override
    public String toString() {
        return "DockerConfigFile [auths=" + auths + "]";
    }

    @Nonnull
    @Deprecated
    public static DockerConfigFile loadConfig(@CheckForNull String dockerConfigPath) throws IOException {
        return loadConfig(DefaultObjectMapperHolder.INSTANCE.getObjectMapper(), dockerConfigPath);
    }

    @Nonnull
    public static DockerConfigFile loadConfig(ObjectMapper objectMapper, @CheckForNull String dockerConfigPath) throws IOException {
        // no any configs, but for empty auths return non null object
        if (dockerConfigPath == null) {
            return new DockerConfigFile();
        }

        //parse new docker config file format
        DockerConfigFile dockerConfig = loadCurrentConfig(objectMapper, dockerConfigPath);

        //parse old auth config file format
        if (dockerConfig == null) {
            dockerConfig = loadLegacyConfig(objectMapper, dockerConfigPath);
        }

        //otherwise create default config
        if (dockerConfig == null) {
            dockerConfig = new DockerConfigFile();
        }

        for (Map.Entry<String, AuthConfig> entry : dockerConfig.getAuths().entrySet()) {
            AuthConfig authConfig = entry.getValue();
            decodeAuth(authConfig);
            authConfig.withAuth(null);
            authConfig.withRegistryAddress(entry.getKey());
        }

        return dockerConfig;
    }

    @CheckForNull
    private static DockerConfigFile loadCurrentConfig(ObjectMapper objectMapper, @CheckForNull String dockerConfigPath) throws IOException {
        File dockerCfgFile = new File(dockerConfigPath, DOCKER_CFG);

        if (!dockerCfgFile.exists() || !dockerCfgFile.isFile()) {
            return null;
        }

        try {
            return objectMapper.readValue(dockerCfgFile, DockerConfigFile.class);
        } catch (IOException e) {
            throw new IOException("Failed to parse docker " + DOCKER_CFG, e);
        }
    }

    @CheckForNull
    private static DockerConfigFile loadLegacyConfig(ObjectMapper objectMapper, String dockerConfigPath) throws IOException {
        File dockerLegacyCfgFile = new File(dockerConfigPath, DOCKER_LEGACY_CFG);

        if (!dockerLegacyCfgFile.exists() || !dockerLegacyCfgFile.isFile()) {
            return null;
        }

        //parse legacy auth config file format
        try {
            return new DockerConfigFile(objectMapper.<Map<String, AuthConfig>>readValue(dockerLegacyCfgFile, CONFIG_MAP_TYPE));
        } catch (IOException e) {
            // pass
        }

        List<String> authFileContent = FileUtils.readLines(dockerLegacyCfgFile, StandardCharsets.UTF_8);
        if (authFileContent.size() < 2) {
            throw new IOException("The Auth Config file is empty");
        }

        AuthConfig config = new AuthConfig();
        String[] origAuth = authFileContent.get(0).split(" = ");
        if (origAuth.length != 2) {
            throw new IOException("Invalid Auth config file");
        }

        config.withAuth(origAuth[1]);

        String[] origEmail = authFileContent.get(1).split(" = ");
        if (origEmail.length != 2) {
            throw new IOException("Invalid Auth config file");
        }
        config.withEmail(origEmail[1]);

        return new DockerConfigFile(new HashMap<>(Collections.singletonMap(config.getRegistryAddress(), config)));
    }

    private static void decodeAuth(AuthConfig config) throws IOException {
        if (config.getAuth() == null) {
            return;
        }

        String str = new String(Base64.getDecoder().decode(config.getAuth()), StandardCharsets.UTF_8);
        String[] parts = str.split(":", 2);
        if (parts.length != 2) {
            throw new IOException("Invalid auth configuration file");
        }
        config.withUsername(parts[0]);
        config.withPassword(parts[1]);
    }

    static String convertToHostname(String server) {
        String stripped = server;
        if (server.startsWith("http://")) {
            stripped = server.substring(7);
        } else if (server.startsWith("https://")) {
            stripped = server.substring(8);
        }
        String[] numParts = stripped.split("/", 2);
        return numParts[0];
    }
}
