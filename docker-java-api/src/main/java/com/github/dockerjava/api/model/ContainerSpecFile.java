package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

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
        return "ContainerSpecFile{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", gid='" + gid + '\'' +
                ", mode=" + mode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerSpecFile that = (ContainerSpecFile) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(uid, that.uid) &&
                Objects.equals(gid, that.gid) &&
                Objects.equals(mode, that.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uid, gid, mode);
    }
}
