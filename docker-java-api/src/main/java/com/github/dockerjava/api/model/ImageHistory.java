package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;

/**
 * Represents an individual image layer information in response to the ImageHistory operation.
 */
@EqualsAndHashCode
@ToString
public class ImageHistory extends DockerObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Created")
    private Long created;

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("Tags")
    private List<String> tags;

    @JsonProperty("Size")
    private Long size;

    @JsonProperty("Comment")
    private String comment;

    @CheckForNull
    public String getId() {
        return id;
    }

    public ImageHistory withId(String id) {
        this.id = id;
        return this;
    }

    @CheckForNull
    public Long getCreated() {
        return created;
    }

    public ImageHistory withCreated(Long created) {
        this.created = created;
        return this;
    }

    @CheckForNull
    public String getCreatedBy() {
        return createdBy;
    }

    public ImageHistory withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    @CheckForNull
    public List<String> getTags() {
        return tags;
    }

    public ImageHistory withTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    @CheckForNull
    public Long getSize() {
        return size;
    }

    public ImageHistory withSize(Long size) {
        this.size = size;
        return this;
    }

    @CheckForNull
    public String getComment() {
        return comment;
    }

    public ImageHistory withComment(String comment) {
        this.comment = comment;
        return this;
    }
}
