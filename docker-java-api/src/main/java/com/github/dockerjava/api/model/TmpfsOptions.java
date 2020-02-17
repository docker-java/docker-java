package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_29}
 */
@EqualsAndHashCode
@ToString
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
}
