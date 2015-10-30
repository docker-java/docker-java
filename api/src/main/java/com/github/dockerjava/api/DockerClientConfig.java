package com.github.dockerjava.api;

import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;

import java.io.Serializable;
import java.net.URI;

/**
 * Created by cruffalo on 10/30/15.
 */
public interface DockerClientConfig extends Serializable {
    URI getUri();

    void setUri(URI uri);

    RemoteApiVersion getVersion();

    String getUsername();

    String getPassword();

    String getEmail();

    String getServerAddress();

    SSLConfig getSslConfig();

    String getDockerCfgPath();

    AuthConfig effectiveAuthConfig(String imageName);

    AuthConfigurations getAuthConfigurations();
}
