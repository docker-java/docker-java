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
public class SwarmNodeStatus implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @FieldName("State")
    private SwarmNodeState state;

    /**
     * @see #state
     */
    @CheckForNull
    public SwarmNodeState getState() {
        return state;
    }

    /**
     * @see #state
     */
    public SwarmNodeStatus withState(SwarmNodeState state) {
        this.state = state;
        return this;
    }
}
