package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
public class PidsStatsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("current")
    private Long current;

    /**
     * @see #current
     */
    @CheckForNull
    public Long getCurrent() {
        return current;
    }
}
