package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * docker secrets that will be exposed to the service
 *
 * @since {@link RemoteApiVersion#VERSION_1_26}
 */
@EqualsAndHashCode
@ToString
public class ContainerSpecSecret implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("File")
    private ContainerSpecFile file;

    @JsonProperty("SecretID")
    private String secretId;

    @JsonProperty("SecretName")
    private String secretName;

    public ContainerSpecFile getFile() {
        return file;
    }

    public ContainerSpecSecret withFile(ContainerSpecFile file) {
        this.file = file;
        return this;
    }

    public String getSecretId() {
        return secretId;
    }

    public ContainerSpecSecret withSecretId(String secretId) {
        this.secretId = secretId;
        return this;
    }

    public String getSecretName() {
        return secretName;
    }

    public ContainerSpecSecret withSecretName(String secretName) {
        this.secretName = secretName;
        return this;
    }
}
