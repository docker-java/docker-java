package com.github.dockerjava.api.command;

import java.util.List;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Marcus Linke
 */
@EqualsAndHashCode
@ToString
public class ListVolumesResponse {

    @FieldName("Volumes")
    private List<InspectVolumeResponse> volumes;

    public List<InspectVolumeResponse> getVolumes() {
        return volumes;
    }
}
