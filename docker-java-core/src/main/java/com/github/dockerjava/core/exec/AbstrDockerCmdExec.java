package com.github.dockerjava.core.exec;

import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.InvocationBuilder;
import com.github.dockerjava.core.WebTarget;
import com.google.common.io.BaseEncoding;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstrDockerCmdExec {

    final transient DockerClientConfig dockerClientConfig;

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
