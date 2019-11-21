package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * File represents a specific target that is backed by a file.
 *
 * @since {@link RemoteApiVersion#VERSION_1_26}
 */
@EqualsAndHashCode
@ToString
public class ContainerSpecFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("Name")
    private String name;
    /**
     * UID represents the file UID.
     */
    @FieldName("UID")
    private String uid;
    /**
     * GID represents the file GID.
     */
    @FieldName("GID")
    private String gid;
    /**
     * Mode represents the FileMode of the file.
     */
    @FieldName("Mode")
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
}
