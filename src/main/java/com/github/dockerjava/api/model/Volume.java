package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents a bind mounted volume in a Docker container.
 * 
 * @see Bind
 */
public class Volume {

    private String path;

    public Volume(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return getPath();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Volume) {
            Volume other = (Volume) obj;
            return new EqualsBuilder().append(path, other.getPath()).isEquals();
        } else
            return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).toHashCode();
    }

}
