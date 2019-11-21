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
public class SearchItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("star_count")
    private Integer starCount;

    @FieldName("is_official")
    private Boolean isOfficial;

    @FieldName("is_trusted")
    private Boolean isTrusted;

    @FieldName("name")
    private String name;

    @FieldName("description")
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
