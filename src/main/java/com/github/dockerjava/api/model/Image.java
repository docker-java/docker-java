package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private List<String> repoTags;

    @JsonProperty("RepoDigests")
    private List<String> repoDigests;

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

    @CheckForNull
    public List<String> getRepoTags() {
        return repoTags;
    }

    @CheckForNull
    public List<String> getRepoDigests() {
        return repoDigests;
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

    public Long getSharedSize() {
        return sharedSize;
    }

    @CheckForNull
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
