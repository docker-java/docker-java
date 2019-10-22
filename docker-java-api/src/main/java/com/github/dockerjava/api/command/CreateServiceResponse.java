package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The response of a {@link CreateServiceCmd}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateServiceResponse {
    @JsonProperty("ID")
    private String id;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "CreateServiceResponse{" +
                "id='" + id + '\'' +
                '}';
    }
}
