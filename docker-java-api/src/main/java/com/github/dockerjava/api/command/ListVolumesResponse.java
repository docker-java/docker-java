package com.github.dockerjava.api.command;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Marcus Linke
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
@ToString
public class ListVolumesResponse {

    @JsonProperty("Volumes")
    private List<InspectVolumeResponse> volumes;

    public List<InspectVolumeResponse> getVolumes() {
        return volumes;
    }
}
