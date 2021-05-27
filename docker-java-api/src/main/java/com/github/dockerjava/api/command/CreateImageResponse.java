package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.DockerObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Parse reponses from /images/create
 *
 * @author Ryan Campbell (ryan.campbell@gmail.com)
 *
 */
@EqualsAndHashCode(callSuper = true)
@ToString
public class CreateImageResponse extends DockerObject {

    @JsonProperty("status")
    private String id;

    public String getId() {
        return id;
    }
}
