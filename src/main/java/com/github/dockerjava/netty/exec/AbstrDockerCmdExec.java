package com.github.dockerjava.netty.exec;

import static com.github.dockerjava.core.RemoteApiVersion.UNKNOWN_VERSION;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_19;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.netty.WebTarget;

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
            final ObjectMapper objectMapper = new ObjectMapper();
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

}
