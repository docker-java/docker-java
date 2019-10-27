package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_29}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TmpfsOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("SizeBytes")
    //The size for the tmpfs mount in bytes.
    private Long sizeBytes;

    //The permission mode for the tmpfs mount in an integer.
    @JsonProperty("Mode")
    private Integer mode;

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public TmpfsOptions withSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
        return this;
    }

    public Integer getMode() {
        return mode;
    }

    public TmpfsOptions withMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    @Override
    public String toString() {
        return "TmpfsOptions{" +
                "sizeBytes=" + sizeBytes +
                ", mode=" + mode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TmpfsOptions that = (TmpfsOptions) o;
        return Objects.equals(sizeBytes, that.sizeBytes) &&
                Objects.equals(mode, that.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sizeBytes, mode);
    }
}
