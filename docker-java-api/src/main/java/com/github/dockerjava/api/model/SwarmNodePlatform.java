package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class SwarmNodePlatform implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Architecture")
    private String architecture;

    /**
     * @since 1.24
     */
    @JsonProperty("OS")
    private String os;

    /**
     * @see #architecture
     */
    @CheckForNull
    public String getArchitecture() {
        return architecture;
    }

    /**
     * @see #architecture
     */
    public SwarmNodePlatform withArchitecture(String architecture) {
        this.architecture = architecture;
        return this;
    }

    /**
     * @see #os
     */
    @CheckForNull
    public String getOs() {
        return os;
    }

    /**
     * @see #os
     */
    public SwarmNodePlatform withOs(String os) {
        this.os = os;
        return this;
    }

    @Override
    public String toString() {
        return "SwarmNodePlatform{" +
                "architecture='" + architecture + '\'' +
                ", os='" + os + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmNodePlatform that = (SwarmNodePlatform) o;
        return Objects.equals(architecture, that.architecture) &&
                Objects.equals(os, that.os);
    }

    @Override
    public int hashCode() {
        return Objects.hash(architecture, os);
    }
}
