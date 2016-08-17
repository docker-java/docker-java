package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodePlugin {

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Name")
    private String name;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
