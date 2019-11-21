package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
public class PidsStatsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("current")
    private Long current;

    /**
     * @see #current
     */
    @CheckForNull
    public Long getCurrent() {
        return current;
    }
}
