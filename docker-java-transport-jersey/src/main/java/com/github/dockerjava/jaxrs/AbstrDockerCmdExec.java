package com.github.dockerjava.jaxrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.RemoteApiVersion;
import com.google.common.io.BaseEncoding;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;

import static com.github.dockerjava.core.RemoteApiVersion.UNKNOWN_VERSION;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_19;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstrDockerCmdExec {

    private final DockerClientConfig dockerClientConfig;

    private final WebTarget baseResource;

    protected final ObjectMapper objectMapper;

    public AbstrDockerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        checkNotNull(baseResource, "baseResource was not specified");
        checkNotNull(dockerClientConfig, "dockerClientConfig was not specified");
        this.baseResource = baseResource;
        this.dockerClientConfig = dockerClientConfig;
        this.objectMapper = dockerClientConfig.getObjectMapper();
        checkNotNull(objectMapper, "objectMapper was not specified");
    }

    protected WebTarget getBaseResource() {
        return baseResource;
    }

    protected AuthConfigurations getBuildAuthConfigs() {
        return dockerClientConfig.getAuthConfigurations();
    }

    protected String registryAuth(AuthConfig authConfig) {
        try {
            return BaseEncoding.base64Url().encode(objectMapper.writeValueAsString(authConfig).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Invocation.Builder resourceWithAuthConfig(AuthConfig authConfig, Invocation.Builder request) {
        request = request.header("X-Registry-Auth", registryAuth(authConfig));
        return request;
    }

    protected Invocation.Builder resourceWithOptionalAuthConfig(AuthConfig authConfig, Invocation.Builder request) {
        if (authConfig != null) {
            request = resourceWithAuthConfig(authConfig, request);
        }
        return request;
    }

    protected String registryConfigs(AuthConfigurations authConfigs) {
        try {
            final String json;
            final RemoteApiVersion apiVersion = dockerClientConfig.getApiVersion();

            if (apiVersion.equals(UNKNOWN_VERSION)) {
                ObjectNode rootNode = objectMapper.valueToTree(authConfigs.getConfigs()); // all registries
                final ObjectNode authNodes = objectMapper.valueToTree(authConfigs); // wrapped in "configs":{}
                rootNode.setAll(authNodes); // merge 2 variants
                json = rootNode.toString();
            } else if (apiVersion.isGreaterOrEqual(VERSION_1_19)) {
                json = objectMapper.writeValueAsString(authConfigs.getConfigs());
            } else {
                json = objectMapper.writeValueAsString(authConfigs);
            }

            return BaseEncoding.base64Url().encode(json.getBytes());
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

}
