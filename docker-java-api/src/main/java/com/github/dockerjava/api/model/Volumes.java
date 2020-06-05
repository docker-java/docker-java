package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Volumes implements Serializable {
    private static final long serialVersionUID = 1L;

    private Volume[] volumes;

    public Volumes(Volume... volumes) {
        this.volumes = volumes;
    }

    public Volumes(List<Volume> volumes) {
        this.volumes = volumes.toArray(new Volume[volumes.size()]);
    }

    public Volume[] getVolumes() {
        return volumes;
    }

    @JsonCreator
    public static Volumes fromPrimitive(Map<String, Object> map) {
        return new Volumes(
                map.keySet().stream().map(Volume::new).toArray(Volume[]::new)
        );
    }

    @JsonValue
    public Map<String, Object> toPrimitive() {
        return Stream.of(volumes).collect(Collectors.toMap(
                Volume::getPath,
                __ -> new Object()
        ));
    }

}
