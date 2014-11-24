package com.github.dockerjava.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.AuthConfig;

public class AuthConfigFile {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, AuthConfig>> CONFIG_MAP_TYPE = new TypeReference<Map<String, AuthConfig>>() {};
    private final Map<String, AuthConfig> authConfigMap;

    public AuthConfigFile() {
        authConfigMap = new HashMap<String, AuthConfig>();
    }

    void addConfig(AuthConfig config) {
        authConfigMap.put(config.getServerAddress(), config);
    }

    public AuthConfig resolveAuthConfig(String hostname) {
        if (StringUtils.isEmpty(hostname) || AuthConfig.DEFAULT_SERVER_ADDRESS.equals(hostname)) {
            return authConfigMap.get(AuthConfig.DEFAULT_SERVER_ADDRESS);
        }
        AuthConfig c = authConfigMap.get(hostname);
        if (c != null) {
            return c;
        }

        // Maybe they have a legacy config file, we will iterate the keys converting
        // them to the new format and testing
        String normalizedHostname = convertToHostname(hostname);
        for (Map.Entry<String, AuthConfig> entry : authConfigMap.entrySet()) {
            String registry = entry.getKey();
            AuthConfig config = entry.getValue();
            if (convertToHostname(registry).equals(normalizedHostname)) {
                return config;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authConfigMap == null) ? 0 : authConfigMap.hashCode());
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
        AuthConfigFile other = (AuthConfigFile) obj;
        if (authConfigMap == null) {
            if (other.authConfigMap != null)
                return false;
        } else if (!authConfigMap.equals(other.authConfigMap))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AuthConfigFile [authConfigMap=" + authConfigMap + "]";
    }


    public static AuthConfigFile loadConfig(File confFile) throws IOException {
        AuthConfigFile configFile = new AuthConfigFile();
        if (!confFile.exists()) {
            return new AuthConfigFile();
        }
        Map<String, AuthConfig> configMap = null;
        try {
            configMap = MAPPER.readValue(confFile, CONFIG_MAP_TYPE);
        } catch (IOException e) {
            // pass
        }
        if (configMap != null) {
            for (Map.Entry<String, AuthConfig> entry : configMap.entrySet()) {
                AuthConfig authConfig = entry.getValue();
                decodeAuth(authConfig.getAuth(), authConfig);
                authConfig.setAuth(null);
                authConfig.setServerAddress(entry.getKey());
                configFile.addConfig(authConfig);
            }
        } else {
            List<String> authFileContent = FileUtils.readLines(confFile);
            if (authFileContent.size() < 2) {
                throw new IOException("The Auth Config file is empty");
            }
            AuthConfig config = new AuthConfig();
            String[] origAuth = authFileContent.get(0).split(" = ");
            if (origAuth.length != 2) {
                throw new IOException("Invalid Auth config file");
            }
            decodeAuth(origAuth[1], config);

            String[] origEmail = authFileContent.get(1).split(" = ");
            if (origEmail.length != 2) {
                throw new IOException("Invalid Auth config file");
            }
            config.setEmail(origEmail[1]);
            configFile.addConfig(config);
        }
        return configFile;

    }

    static void decodeAuth(String auth, AuthConfig config) throws IOException {
        String str = new String(Base64.decodeBase64(auth), Charset.forName("UTF-8"));
        String[] parts = str.split(":", 2);
        if (parts.length != 2) {
            throw new IOException("Invalid auth configuration file");
        }
        config.setUsername(parts[0]);
        config.setPassword(parts[1]);
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
