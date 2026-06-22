package com.github.dockerjava.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;

import java.net.URI;
import java.util.Objects;

/**
 * Decorates a {@link DockerClientConfig} and overrides only {@link #getApiVersion()} with a value
 * produced by {@link ApiVersionNegotiator}. All other accessors delegate to the original config.
 * <p>
 * Used by {@link DockerClientImpl#getInstance(DockerClientConfig, com.github.dockerjava.transport.DockerHttpClient)}
 * when {@link DockerClientConfig#isApiVersionAutoNegotiationEnabled() auto-negotiation} is enabled.
 * Keeping the negotiated version in a wrapper avoids mutating the immutable
 * {@link DefaultDockerClientConfig}.
 */
final class NegotiatedDockerClientConfig implements DockerClientConfig {

    private final DockerClientConfig delegate;

    private final RemoteApiVersion negotiatedApiVersion;

    NegotiatedDockerClientConfig(DockerClientConfig delegate, RemoteApiVersion negotiatedApiVersion) {
        this.delegate = Objects.requireNonNull(delegate, "delegate config was not specified");
        this.negotiatedApiVersion = Objects.requireNonNull(negotiatedApiVersion, "negotiatedApiVersion was not specified");
    }

    @Override
    public URI getDockerHost() {
        return delegate.getDockerHost();
    }

    @Override
    public RemoteApiVersion getApiVersion() {
        return negotiatedApiVersion;
    }

    @Override
    public String getRegistryUsername() {
        return delegate.getRegistryUsername();
    }

    @Override
    public String getRegistryPassword() {
        return delegate.getRegistryPassword();
    }

    @Override
    public String getRegistryEmail() {
        return delegate.getRegistryEmail();
    }

    @Override
    public String getRegistryUrl() {
        return delegate.getRegistryUrl();
    }

    @Override
    public AuthConfig effectiveAuthConfig(String imageName) {
        return delegate.effectiveAuthConfig(imageName);
    }

    @Override
    public AuthConfigurations getAuthConfigurations() {
        return delegate.getAuthConfigurations();
    }

    @Override
    public SSLConfig getSSLConfig() {
        return delegate.getSSLConfig();
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return delegate.getObjectMapper();
    }

    @Override
    public boolean isApiVersionAutoNegotiationEnabled() {
        // Negotiation has already happened; report as resolved so downstream consumers don't re-negotiate.
        return false;
    }
}
