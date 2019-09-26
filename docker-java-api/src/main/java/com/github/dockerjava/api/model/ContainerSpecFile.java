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
 * File represents a specific target that is backed by a file.
 *
 * @since {@link RemoteApiVersion#VERSION_1_26}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContainerSpecFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Name")
    private String name;
    /**
     * UID represents the file UID.
     */
    @JsonProperty("UID")
    private String uid;
    /**
     * GID represents the file GID.
     */
    @JsonProperty("GID")
    private String gid;
    /**
     * Mode represents the FileMode of the file.
     */
    @JsonProperty("Mode")
    private Long mode;

    public String getName() {
        return name;
    }

    public ContainerSpecFile withName(String name) {
        this.name = name;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public ContainerSpecFile withUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getGid() {
        return gid;
    }

    public ContainerSpecFile withGid(String gid) {
        this.gid = gid;
        return this;
    }

    public Long getMode() {
        return mode;
    }

    public ContainerSpecFile withMode(Long mode) {
        this.mode = mode;
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
