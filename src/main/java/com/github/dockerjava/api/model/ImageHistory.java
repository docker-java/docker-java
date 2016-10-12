package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a history entry in the history of an image.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Created")
    private Long created;

    @JsonProperty("CreatedBy")
    private String createdBy;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_19}
     */
    @JsonProperty("Tags")
    private List<String> tags;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_19}
     */
    @JsonProperty("Size")
    private Long size;

    /**
     * @since {@link RemoteApiVersion#VERSION_1_19}
     */
    @JsonProperty("Comment")
    private String comment;

    public String getId() {
        return id;
    }

    public Long getCreated() {
        return created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public List<String> getTags() {
        return tags;
    }

    public Long getSize() {
        return size;
    }

    public String getComment() {
        return comment;
    }

    public ImageHistory withId(String id) {
        this.id = id;
        return this;
    }

    public ImageHistory withCreated(Long created) {
        this.created = created;
        return this;
    }

    public ImageHistory withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ImageHistory withTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public ImageHistory withSize(Long size) {
        this.size = size;
        return this;
    }

    public ImageHistory withComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
