package com.github.dockerjava.jaxrs.jersey.jaxrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.api.RemoteApiVersion;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.google.common.base.Preconditions;
import org.apache.commons.codec.binary.Base64;

import javax.ws.rs.client.WebTarget;
import java.io.IOException;

public abstract class AbstrDockerCmdExec {

    private final DockerClientConfig dockerClientConfig;

    private final WebTarget baseResource;

    public AbstrDockerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        Preconditions.checkNotNull(baseResource, "baseResource was not specified");
        Preconditions.checkNotNull(dockerClientConfig, "dockerClientConfig was not specified");
        this.baseResource = baseResource;
        this.dockerClientConfig = dockerClientConfig;
    }

    protected WebTarget getBaseResource() {
        return baseResource;
    }

    protected AuthConfigurations getBuildAuthConfigs() {
        return dockerClientConfig.getAuthConfigurations();
    }

    protected String registryAuth(AuthConfig authConfig) {
        try {
            return Base64.encodeBase64String(new ObjectMapper().writeValueAsString(authConfig).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String registryConfigs(AuthConfigurations authConfigs) {
        try {
            final String json;
            if (dockerClientConfig.getVersion().isGreaterOrEqual(RemoteApiVersion.VERSION_1_19)) {
                json = new ObjectMapper().writeValueAsString(authConfigs.getConfigs());
            } else {
                json = new ObjectMapper().writeValueAsString(authConfigs);
            }

            return Base64.encodeBase64String(json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
