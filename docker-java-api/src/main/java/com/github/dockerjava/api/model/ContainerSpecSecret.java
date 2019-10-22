package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * docker secrets that will be exposed to the service
 *
 * @since {@link RemoteApiVersion#VERSION_1_26}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Override
    public String toString() {
        return "ContainerSpecSecret{" +
                "file=" + file +
                ", secretId='" + secretId + '\'' +
                ", secretName='" + secretName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerSpecSecret that = (ContainerSpecSecret) o;
        return Objects.equals(file, that.file) &&
                Objects.equals(secretId, that.secretId) &&
                Objects.equals(secretName, that.secretName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, secretId, secretName);
    }
}
