package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
