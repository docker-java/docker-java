package com.github.dockerjava.api.command;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.DockerObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Marcus Linke
 */
@EqualsAndHashCode(callSuper = true)
@ToString
public class ListVolumesResponse extends DockerObject {

    @JsonProperty("Volumes")
    private List<InspectVolumeResponse> volumes;

    public List<InspectVolumeResponse> getVolumes() {
        return volumes;
    }
}
