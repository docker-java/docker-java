package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class SwarmJoinTokens implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Worker")
    private String worker;

    /**
     * @since 1.24
     */
    @JsonProperty("Manager")
    private String manager;

    /**
     * @see #worker
     */
    @CheckForNull
    public String getWorker() {
        return worker;
    }

    /**
     * @see #worker
     */
    public SwarmJoinTokens withWorker(String worker) {
        this.worker = worker;
        return this;
    }

    /**
     * @see #manager
     */
    @CheckForNull
    public String getManager() {
        return manager;
    }

    /**
     * @see #manager
     */
    public SwarmJoinTokens withManager(String manager) {
        this.manager = manager;
        return this;
    }
}
