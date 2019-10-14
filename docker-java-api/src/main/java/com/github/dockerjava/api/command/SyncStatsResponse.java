package com.github.dockerjava.api.command;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncStatsResponse {

    @JsonProperty("Volumes")
    private List<InspectVolumeResponse> volumes;

    public List<InspectVolumeResponse> getVolumes() {
        return volumes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
