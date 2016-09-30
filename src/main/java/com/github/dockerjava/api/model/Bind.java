package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Represents a host path being bind mounted as a {@link Volume} in a Docker container.
 * The Bind can be in read only or read write access mode.
 */
public class Bind implements Serializable {
    private static final long serialVersionUID = 1L;

    private String path;

    private Volume volume;

    private AccessMode accessMode;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_23}
     */
    private Boolean noCopy;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_17}
     */
    private SELContext secMode;

    public Bind(String path, Volume volume) {
        this(path, volume, AccessMode.DEFAULT, SELContext.DEFAULT);
    }

    public Bind(String path, Volume volume, Boolean noCopy) {
        this(path, volume, AccessMode.DEFAULT, SELContext.DEFAULT, noCopy);
    }

    public Bind(String path, Volume volume, AccessMode accessMode) {
        this(path, volume, accessMode, SELContext.DEFAULT);
    }

    public Bind(String path, Volume volume, AccessMode accessMode, SELContext secMode) {
        this(path, volume, accessMode, secMode, null);
    }

    public Bind(String path, Volume volume, AccessMode accessMode, SELContext secMode, Boolean noCopy) {
        this.path = path;
        this.volume = volume;
        this.accessMode = accessMode;
        this.secMode = secMode;
        this.noCopy = noCopy;
    }

    public String getPath() {
        return path;
    }

    public Volume getVolume() {
        return volume;
    }

    public AccessMode getAccessMode() {
        return accessMode;
    }

    public SELContext getSecMode() {
        return secMode;
    }

    public Boolean getNoCopy() {
        return noCopy;
    }

    /**
     * Parses a bind mount specification to a {@link Bind}.
     *
     * @param serialized
     *            the specification, e.g. <code>/host:/container:ro</code>
     * @return a {@link Bind} matching the specification
     * @throws IllegalArgumentException
     *             if the specification cannot be parsed
     */
    public static Bind parse(String serialized) {
        try {
            String[] parts = serialized.split(":");
            switch (parts.length) {
            case 2: {
                return new Bind(parts[0], new Volume(parts[1]));
            }
            case 3: {
                String[] flags = parts[2].split(",");
                AccessMode accessMode = AccessMode.DEFAULT;
                SELContext seMode = SELContext.DEFAULT;
                Boolean nocopy = null;
                for (String p : flags) {
                    if (p.length() == 2) {
                        accessMode = AccessMode.valueOf(p.toLowerCase());
                    } else if ("nocopy".equals(p)) {
                        nocopy = true;
                    } else {
                        seMode = SELContext.fromString(p);
                    }
                }

                return new Bind(parts[0], new Volume(parts[1]), accessMode, seMode, nocopy);
            }
            default: {
                throw new IllegalArgumentException();
            }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing Bind '" + serialized + "'", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Bind) {
            Bind other = (Bind) obj;
            return new EqualsBuilder()
                    .append(path, other.getPath())
                    .append(volume, other.getVolume())
                    .append(accessMode, other.getAccessMode())
                    .append(secMode, other.getSecMode())
                    .append(noCopy, other.getNoCopy())
                    .isEquals();
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(path)
                .append(volume)
                .append(accessMode)
                .append(secMode)
                .append(noCopy)
                .toHashCode();
    }

    /**
     * Returns a string representation of this {@link Bind} suitable for inclusion in a JSON message.
     * The format is <code>&lt;host path&gt;:&lt;container path&gt;:&lt;access mode&gt;</code>,
     * like the argument in {@link #parse(String)}.
     *
     * @return a string representation of this {@link Bind}
     */
    @Override
    public String toString() {
        return String.format("%s:%s:%s%s%s",
                path,
                volume.getPath(),
                accessMode.toString(),
                secMode != SELContext.none ? "," + secMode.toString() : "",
                noCopy != null ? ",nocopy" : "");
    }
}
