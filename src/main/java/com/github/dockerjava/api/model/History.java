package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class History implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Created")
    private Long created;

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("Tags")
    private String[] tags;

    @JsonProperty("Size")
    private Long size;

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

    public String[] getTags() {
        return tags;
    }

    public Long getSize() {
        return size;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
