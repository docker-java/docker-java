package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class VolumeBinds implements Serializable {
    private static final long serialVersionUID = 1L;

    private final VolumeBind[] binds;

    public VolumeBinds(VolumeBind... binds) {
        this.binds = binds;
    }

    public VolumeBind[] getBinds() {
        return binds;
    }

    @JsonCreator
    public static VolumeBinds fromPrimitive(Map<String, String> primitive) {
        return new VolumeBinds(
                primitive.entrySet().stream()
                .map(it -> new VolumeBind(it.getValue(), it.getKey()))
                .toArray(VolumeBind[]::new)
        );
    }

    @JsonValue
    public Map<String, String> toPrimitive() {
        return Stream.of(binds).collect(Collectors.toMap(
                VolumeBind::getContainerPath,
                VolumeBind::getHostPath
        ));
    }

}
