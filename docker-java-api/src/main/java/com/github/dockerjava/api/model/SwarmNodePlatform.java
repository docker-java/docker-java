package com.github.dockerjava.api.model;


import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class SwarmNodePlatform implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @FieldName("Architecture")
    private String architecture;

    /**
     * @since 1.24
     */
    @FieldName("OS")
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
}
