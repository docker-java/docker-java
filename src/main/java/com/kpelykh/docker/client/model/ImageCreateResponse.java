package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

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
