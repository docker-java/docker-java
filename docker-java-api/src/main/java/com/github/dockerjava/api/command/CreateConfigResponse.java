package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The response of a {@link CreateConfigCmd}
 */
@EqualsAndHashCode
@ToString
public class CreateConfigResponse {
    @JsonProperty("ID")
    private String id;

    public String getId() {
        return id;
    }
}
