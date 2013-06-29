package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;
import java.util.Arrays;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class ContainerCreateResponse {

    @JsonProperty("Id")
    public String id;

    @JsonProperty("Warnings")
    public String[] warnings;

    @Override
    public String toString() {
        return "ContainerCreateResponse{" +
                "id='" + id + '\'' +
                ", warnings=" + Arrays.toString(warnings) +
                '}';
    }
}
