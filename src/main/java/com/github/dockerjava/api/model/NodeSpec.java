package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeSpec {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Role")
    private String id;

    @JsonProperty("Availability")
    private String availability;

    @JsonProperty("Labels")
    public Map<String, String> labels;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAvailability() {
        return availability;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }
}
