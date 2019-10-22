package com.github.dockerjava.api.command;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Marcus Linke
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListVolumesResponse {

    @JsonProperty("Volumes")
    private List<InspectVolumeResponse> volumes;

    public List<InspectVolumeResponse> getVolumes() {
        return volumes;
    }

    @Override
    public String toString() {
        return "ListVolumesResponse{" +
                "volumes=" + volumes +
                '}';
    }
}
