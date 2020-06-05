package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;

/**
 * Represents a bind mounted volume in a Docker container.
 *
 * @see Bind
 * @deprecated since {@link RemoteApiVersion#VERSION_1_20}
 */
@Deprecated
@EqualsAndHashCode
public class VolumeRW implements Serializable {
    private static final long serialVersionUID = 1L;

    private Volume volume;

    private AccessMode accessMode = AccessMode.rw;

    public VolumeRW(Volume volume) {
        this.volume = volume;
    }

    public VolumeRW(Volume volume, AccessMode accessMode) {
        this.volume = volume;
        this.accessMode = accessMode;
    }

    public Volume getVolume() {
        return volume;
    }

    public AccessMode getAccessMode() {
        return accessMode;
    }

    /**
     * Returns a string representation of this {@link VolumeRW} suitable for inclusion in a JSON message. The returned String is simply the
     * container path, {@link #getPath()}.
     *
     * @return a string representation of this {@link VolumeRW}
     */
    @Override
    public String toString() {
        return getVolume() + ":" + getAccessMode();
    }

    @JsonCreator
    public static VolumeRW fromPrimitive(Map<String, Boolean> map) {
        Entry<String, Boolean> entry = map.entrySet().iterator().next();
        return new VolumeRW(new Volume(entry.getKey()), AccessMode.fromBoolean(entry.getValue()));
    }

    @JsonValue
    public Map<String, Boolean> toPrimitive() {
        return Collections.singletonMap(volume.getPath(), accessMode.toBoolean());
    }

}
