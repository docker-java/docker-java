/*
 * Created on 08.06.2016
 */
package com.github.dockerjava.core;

import java.net.URI;

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

}
