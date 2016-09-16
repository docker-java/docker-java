package com.github.dockerjava.api.model;

/**
 * The propagation mode of a file system or file: <code>shared</code>, <code>slave</code> or <code>private</code>.
 *
 * @see https://github.com/docker/docker/pull/17034
 * @since 1.22
 */
public enum PropagationMode {
    /** default */
    mount_default(""),

    /** shared */
    mount_shared("shared"),

    /** slave */
    mount_slave("slave"),

    /** private */
    mount_private("private");

    /**
     * The default {@link PropagationMode}: {@link #mount_default}
     */
    public static final PropagationMode DEFAULT = mount_default;

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
            return mount_shared;
        case "slave":
            return mount_slave;
        case "private":
            return mount_private;
        }
        return mount_default;
    }
}
