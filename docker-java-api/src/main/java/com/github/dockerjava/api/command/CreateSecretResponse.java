package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The response of a {@link CreateSecretCmd}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSecretResponse {
    @JsonProperty("ID")
    private String id;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "CreateSecretResponse{" +
                "id='" + id + '\'' +
                '}';
    }
}
