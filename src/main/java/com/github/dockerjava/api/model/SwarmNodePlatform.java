package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class SwarmNodePlatform implements Serializable {
    public static final Long serialVersionUID = 1L;

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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
