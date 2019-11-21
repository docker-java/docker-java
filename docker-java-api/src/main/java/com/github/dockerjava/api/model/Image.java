package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@EqualsAndHashCode
@ToString
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("Created")
    private Long created;

    @FieldName("Id")
    private String id;

    @FieldName("ParentId")
    private String parentId;

    @FieldName("RepoTags")
    private String[] repoTags;

    @FieldName("Size")
    private Long size;

    @FieldName("VirtualSize")
    private Long virtualSize;

    public String getId() {
        return id;
    }

    public String[] getRepoTags() {
        return repoTags;
    }

    public String getParentId() {
        return parentId;
    }

    public Long getCreated() {
        return created;
    }

    public Long getSize() {
        return size;
    }

    public Long getVirtualSize() {
        return virtualSize;
    }
}
