package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.dockerjava.api.annotation.FromPrimitive;
import com.github.dockerjava.api.annotation.ToPrimitive;

public class Volumes implements Serializable {
    private static final long serialVersionUID = 1L;

    private String[] volumes;

    public Volumes(Volume... volumes) {
        this.volumes = Stream.of(volumes).map(Volume::getPath).toArray(String[]::new);
    }

    public Volumes(List<Volume> volumes) {
        this.volumes = volumes.stream().map(Volume::getPath).toArray(String[]::new);
    }

    public Volume[] getVolumes() {
        return Stream.of(volumes).map(Volume::new).toArray(Volume[]::new);
    }

    @FromPrimitive
    public static Volumes fromPrimitive(Map<String, Object> map) {
        return new Volumes(
                map.keySet().stream().map(Volume::new).toArray(Volume[]::new)
        );
    }

    @ToPrimitive
    public Map<String, Object> toPrimitive() {
        return Stream.of(volumes).collect(Collectors.toMap(
                it -> it,
                __ -> new Object()
        ));
    }

}
