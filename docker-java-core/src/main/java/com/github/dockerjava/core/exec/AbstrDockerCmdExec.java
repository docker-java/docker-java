package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.InvocationBuilder;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.core.WebTarget;
import com.google.common.io.BaseEncoding;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;

import static com.github.dockerjava.core.RemoteApiVersion.UNKNOWN_VERSION;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_19;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstrDockerCmdExec {

    private final transient DockerClientConfig dockerClientConfig;

    private final transient WebTarget baseResource;

    public AbstrDockerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        checkNotNull(baseResource, "baseResource was not specified");
        checkNotNull(dockerClientConfig, "dockerClientConfig was not specified");
        this.baseResource = baseResource;
        this.dockerClientConfig = dockerClientConfig;
    }

    protected WebTarget getBaseResource() {
        return baseResource;
    }

    @CheckForNull
    protected AuthConfigurations getBuildAuthConfigs() {
        return dockerClientConfig.getAuthConfigurations();
    }

    protected String registryAuth(@Nonnull AuthConfig authConfig) {
        try {
            return BaseEncoding.base64Url().encode(dockerClientConfig.getObjectMapper().writeValueAsString(authConfig).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    protected String registryConfigs(@Nonnull AuthConfigurations authConfigs) {
        try {
            final String json;
            final RemoteApiVersion apiVersion = dockerClientConfig.getApiVersion();
            ObjectMapper objectMapper = dockerClientConfig.getObjectMapper();

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

    @Nonnull
    protected InvocationBuilder resourceWithAuthConfig(@Nonnull AuthConfig authConfig,
                                                       @Nonnull InvocationBuilder request) {
        return request.header("X-Registry-Auth", registryAuth(authConfig));
    }

    @Nonnull
    protected InvocationBuilder resourceWithOptionalAuthConfig(@CheckForNull AuthConfig authConfig,
                                                               @Nonnull InvocationBuilder request) {
        if (authConfig != null) {
            request = resourceWithAuthConfig(authConfig, request);
        }
        return request;
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
