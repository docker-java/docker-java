package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The response of a {@link CreateServiceCmd}
 */
@EqualsAndHashCode
@ToString
public class CreateServiceResponse {
    @JsonProperty("ID")
    private String id;

    public String getId() {
        return id;
    }
}
