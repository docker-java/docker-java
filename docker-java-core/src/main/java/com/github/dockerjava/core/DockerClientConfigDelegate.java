package com.github.dockerjava.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;

import java.net.URI;

@SuppressWarnings("unused")
public class DockerClientConfigDelegate implements DockerClientConfig {

    final DockerClientConfig original;

    public DockerClientConfigDelegate(DockerClientConfig original) {
        this.original = original;
    }

    public URI getDockerHost() {
        return original.getDockerHost();
    }

    public RemoteApiVersion getApiVersion() {
        return original.getApiVersion();
    }

    public String getRegistryUsername() {
        return original.getRegistryUsername();
    }

    public String getRegistryPassword() {
        return original.getRegistryPassword();
    }

    public String getRegistryEmail() {
        return original.getRegistryEmail();
    }

    public String getRegistryUrl() {
        return original.getRegistryUrl();
    }

    public AuthConfig effectiveAuthConfig(String imageName) {
        return original.effectiveAuthConfig(imageName);
    }

    public AuthConfigurations getAuthConfigurations() {
        return original.getAuthConfigurations();
    }

    public SSLConfig getSSLConfig() {
        return original.getSSLConfig();
    }

    public ObjectMapper getObjectMapper() {
        return original.getObjectMapper();
    }
}
