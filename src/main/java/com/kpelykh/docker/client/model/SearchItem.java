package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchItem {

    @JsonProperty("star_count")
    private int starCount;

    @JsonProperty("is_official")
    private boolean isOfficial;

    @JsonProperty("is_trusted")
    private boolean isTrusted;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    public int getStarCount() {
        return starCount;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public boolean isTrusted() {
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
        return  "name='" + name + '\'' +
                ", description='" + description + '\'' + '}';
    }
}
