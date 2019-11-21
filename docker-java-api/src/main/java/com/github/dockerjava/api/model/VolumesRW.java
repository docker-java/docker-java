package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.dockerjava.api.annotation.FromPrimitive;
import com.github.dockerjava.api.annotation.ToPrimitive;

// This is not going to be serialized
public class VolumesRW implements Serializable {
    private static final long serialVersionUID = 1L;

    private final VolumeRW[] volumesRW;

    public VolumesRW(VolumeRW... binds) {
        this.volumesRW = binds;
    }

    public VolumeRW[] getVolumesRW() {
        return volumesRW;
    }

    @FromPrimitive
    public static VolumesRW fromPrimitive(Map<String, Boolean> map) {
        return new VolumesRW(
                map.entrySet().stream()
                    .map(entry -> new VolumeRW(new Volume(entry.getKey()), AccessMode.fromBoolean(entry.getValue())))
                    .toArray(VolumeRW[]::new)
        );
    }

    @ToPrimitive
    public Map<String, Boolean> toPrimitive() {
        return Stream.of(volumesRW).collect(Collectors.toMap(
                it -> it.getVolume().getPath(),
                it -> it.getAccessMode().toBoolean()
        ));
    }
}
