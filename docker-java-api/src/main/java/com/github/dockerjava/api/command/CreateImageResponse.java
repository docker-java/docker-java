package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Parse reponses from /images/create
 *
 * @author Ryan Campbell (ryan.campbell@gmail.com)
 *
 */
@EqualsAndHashCode
@ToString
public class CreateImageResponse {

    @JsonProperty("status")
    private String id;

    public String getId() {
        return id;
    }
}
