package com.github.dockerjava.api.model;

import java.util.HashMap;

public final class DockerObjectAccessor {

    /**
     * @deprecated not for public usage, unless you _really_ understand what you're doing
     */
    @Deprecated
    public static void overrideRawValues(DockerObject o, HashMap<String, Object> rawValues) {
        o.rawValues = rawValues != null ? rawValues : new HashMap<>();
    }

    /**
     * This is an advanced method for setting raw values on the resulting object
     * that will fully overwrite any previously set value for given key.
     *
     * Make sure to check Docker's API before using it.
     */
    public static void overrideRawValue(DockerObject o, String key, Object value) {
        o.rawValues.put(key, value);
    }

    private DockerObjectAccessor() {
    }
}
