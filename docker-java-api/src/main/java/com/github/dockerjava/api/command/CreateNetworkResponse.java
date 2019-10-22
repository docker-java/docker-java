package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateNetworkResponse {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Warnings")
    private String[] warnings;

    public String getId() {
        return id;
    }

    public String[] getWarnings() {
        return warnings;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWarnings(String[] warnings) {
        this.warnings = warnings;
    }

    @Override
    public String toString() {
        return "CreateNetworkResponse{" +
                "id='" + id + '\'' +
                ", warnings=" + Arrays.toString(warnings) +
                '}';
    }
}
