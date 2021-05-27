package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// This is not going to be serialized
@EqualsAndHashCode(callSuper = true)
@ToString
public class VolumesRW extends DockerObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private final VolumeRW[] volumesRW;

    public VolumesRW(VolumeRW... binds) {
        this.volumesRW = binds;
    }

    public VolumeRW[] getVolumesRW() {
        return volumesRW;
    }

    @JsonCreator
    public static VolumesRW fromPrimitive(Map<String, Boolean> map) {
        return new VolumesRW(
                map.entrySet().stream()
                    .map(entry -> new VolumeRW(new Volume(entry.getKey()), AccessMode.fromBoolean(entry.getValue())))
                    .toArray(VolumeRW[]::new)
        );
    }

    @JsonValue
    public Map<String, Boolean> toPrimitive() {
        return Stream.of(volumesRW).collect(Collectors.toMap(
                it -> it.getVolume().getPath(),
                it -> it.getAccessMode().toBoolean()
        ));
    }
}
