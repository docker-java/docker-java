/*
 * Created on 08.06.2016
 */
package com.github.dockerjava.core;

import java.net.URI;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;

/**
 * Interface that describes the docker client configuration.
 *
 * @author Marcus Linke
 *
 */
public interface DockerClientConfig {

    URI getDockerHost();

    RemoteApiVersion getApiVersion();

    String getRegistryUsername();

    String getRegistryPassword();

    String getRegistryEmail();

    String getRegistryUrl();

    AuthConfig effectiveAuthConfig(String imageName);

    AuthConfigurations getAuthConfigurations();

    /**
     * Returns an {@link SSLConfig} when secure connection is configured or null if not.
     */
    SSLConfig getSSLConfig();

    default ObjectMapper getObjectMapper() {
        return DefaultObjectMapperHolder.INSTANCE.getObjectMapper().copy();
    }
}

enum DefaultObjectMapperHolder {
    INSTANCE;

    private final ObjectMapper objectMapper = new ObjectMapper()
            // TODO .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
