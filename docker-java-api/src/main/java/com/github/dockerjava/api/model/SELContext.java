package com.github.dockerjava.api.model;

/**
 * The context mode of bound volume in SELinux.
 * Host path <code>shared</code> - can be used by all containers, <code>private</code> - by only this container
 *
 * @see http://www.projectatomic.io/blog/2015/06/using-volumes-with-docker-can-cause-problems-with-selinux/
 * @since 1.17
 */
public enum SELContext {
    /** no selinux */
    none(""),

    /** z option */
    shared("z"),

    /** Z option */
    single("Z");

    /**
     * The default {@link SELContext}: {@link #none}
     */
    public static final SELContext DEFAULT = none;

    private String value;

    SELContext(String v) {
        this.value = v;
    }

    @Override
    public String toString() {
        return value;
    }

    public static SELContext fromString(String p) {
        switch (p) {
        case "z":
            return shared;
        case "Z":
            return single;
        }
        return none;
    }
}
