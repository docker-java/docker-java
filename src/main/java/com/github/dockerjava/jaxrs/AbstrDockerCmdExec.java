package com.github.dockerjava.jaxrs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.ws.rs.client.WebTarget;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;

public abstract class AbstrDockerCmdExec {

    private WebTarget baseResource;

    public AbstrDockerCmdExec(WebTarget baseResource) {
        checkNotNull(baseResource, "baseResource was not specified");
        this.baseResource = baseResource;
    }

    protected WebTarget getBaseResource() {
        return baseResource;
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
            return Base64.encodeBase64String(new ObjectMapper().writeValueAsString(authConfigs).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
