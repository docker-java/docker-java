package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("star_count")
    private Integer starCount;

    @JsonProperty("is_official")
    private Boolean isOfficial;

    @JsonProperty("is_trusted")
    private Boolean isTrusted;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    public Integer getStarCount() {
        return starCount;
    }

    public Boolean isOfficial() {
        return isOfficial;
    }

    public Boolean isTrusted() {
        return isTrusted;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
