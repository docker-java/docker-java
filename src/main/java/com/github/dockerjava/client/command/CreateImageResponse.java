package com.github.dockerjava.client.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Parse reponses from /images/create
 *
 * @author Ryan Campbell (ryan.campbell@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateImageResponse {

    @JsonProperty("status")
    private String id;


    public String getId() {
        return id;
    }


    @Override
    public String toString() {
        return "CreateImageResponse{" +
                "id='" + id + '\'' +
                '}';
    }
}
