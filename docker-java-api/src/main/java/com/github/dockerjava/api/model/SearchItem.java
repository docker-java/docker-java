package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
}
