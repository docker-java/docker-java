package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Created")
    private Long created;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("ParentId")
    private String parentId;

    @JsonProperty("RepoTags")
    private String[] repoTags;

    @JsonProperty("RepoDigests")
    private String[] repoDigests;

    @JsonProperty("Size")
    private Long size;

    @JsonProperty("VirtualSize")
    private Long virtualSize;

    @JsonProperty("SharedSize")
    private Long sharedSize;

    @JsonProperty("Labels")
    public Map<String, String> labels;

    @JsonProperty("Containers")
    private Integer containers;

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

    public String[] getRepoDigests() {
        return repoDigests;
    }

    public Long getSharedSize() {
        return sharedSize;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public Integer getContainers() {
        return containers;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
