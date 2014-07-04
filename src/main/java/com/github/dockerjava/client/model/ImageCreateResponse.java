package com.github.dockerjava.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Parse reponses from /images/create
 *
 * @author Ryan Campbell (ryan.campbell@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageCreateResponse {

    @JsonProperty("status")
    private String id;


    public String getId() {
        return id;
    }


    @Override
    public String toString() {
        return "ContainerCreateResponse{" +
                "id='" + id + '\'' +
                '}';
    }
}
