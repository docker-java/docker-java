package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwarmCAConfig implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("NodeCertExpiry")
    private Long nodeCertExpiry;

    /**
     * @since 1.24
     */
    @JsonProperty("ExternalCAs")
    private List<ExternalCA> externalCA;

    /**
     * @see #nodeCertExpiry
     */
    @CheckForNull
    public Long getNodeCertExpiry() {
        return nodeCertExpiry;
    }

    /**
     * @see #nodeCertExpiry
     */
    public SwarmCAConfig withNodeCertExpiry(Long nodeCertExpiry) {
        this.nodeCertExpiry = nodeCertExpiry;
        return this;
    }

    /**
     * @see #externalCA
     */
    @CheckForNull
    public List<ExternalCA> getExternalCA() {
        return externalCA;
    }

    /**
     * @see #externalCA
     */
    public SwarmCAConfig withExternalCA(List<ExternalCA> externalCA) {
        this.externalCA = externalCA;
        return this;
    }

    @Override
    public String toString() {
        return "SwarmCAConfig{" +
                "nodeCertExpiry=" + nodeCertExpiry +
                ", externalCA=" + externalCA +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmCAConfig that = (SwarmCAConfig) o;
        return Objects.equals(nodeCertExpiry, that.nodeCertExpiry) &&
                Objects.equals(externalCA, that.externalCA);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeCertExpiry, externalCA);
    }
}
