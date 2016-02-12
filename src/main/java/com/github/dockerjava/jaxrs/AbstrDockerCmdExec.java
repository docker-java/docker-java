package com.github.dockerjava.jaxrs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.ws.rs.client.WebTarget;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.RemoteApiVersion;

public abstract class AbstrDockerCmdExec {

    private final DockerClientConfig dockerClientConfig;

    private final WebTarget baseResource;

    public AbstrDockerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        checkNotNull(baseResource, "baseResource was not specified");
        checkNotNull(dockerClientConfig, "dockerClientConfig was not specified");
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
            if (dockerClientConfig.getApiVersion().isGreaterOrEqual(RemoteApiVersion.VERSION_1_19)) {
                json = new ObjectMapper().writeValueAsString(authConfigs.getConfigs());
            } else {
                json = new ObjectMapper().writeValueAsString(authConfigs);
            }

            return Base64.encodeBase64String(json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean bool(Boolean bool) {
        return bool != null && bool;
    }

    protected WebTarget booleanQueryParam(WebTarget webTarget, String name, Boolean value) {
        if (bool(value)) {
            webTarget = webTarget.queryParam(name, bool(value) + "");
        }

        return webTarget;
    }

    protected boolean isSwarmEndpoint() {
        return dockerClientConfig.isSwarmEndpoint();
    }
}
