package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The response of a {@link CreateSecretCmd}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
@ToString
public class CreateSecretResponse {
    @JsonProperty("ID")
    private String id;

    public String getId() {
        return id;
    }
}
