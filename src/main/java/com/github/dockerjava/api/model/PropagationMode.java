package com.github.dockerjava.api.model;

/**
 * The propagation mode of a file system or file: <code>shared</code>, <code>slave</code> or <code>private</code>.
 *
 * @see https://github.com/docker/docker/pull/17034
 * @since 1.22
 */
public enum PropagationMode {
    /** default */
    DEFAULT(""),

    /** shared */
    SHARED("shared"),

    /** slave */
    SLAVE("slave"),

    /** private */
    PRIVATE("private");

    /**
     * The default {@link PropagationMode}: {@link #DEFAULT}
     */
    public static final PropagationMode DEFAULT_MODE = DEFAULT;

    private String value;

    PropagationMode(String v) {
        value = v;
    }

    @Override
    public String toString() {
        return value;
    }

    public static PropagationMode fromString(String v) {
        switch (v) {
        case "shared":
            return SHARED;
        case "slave":
            return SLAVE;
        case "private":
            return PRIVATE;
        default:
            return DEFAULT;
        }
    }
}
