package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class SearchItem {

    @JsonProperty("Name")
    public String name;

    @JsonProperty("Description")
    public String description;

    @Override
    public String toString() {
        return  "name='" + name + '\'' +
                ", description='" + description + '\'' + '}';
    }
}
