package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Override
    public String toString() {
        return "SwarmJoinTokens{" +
                "worker='" + worker + '\'' +
                ", manager='" + manager + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmJoinTokens that = (SwarmJoinTokens) o;
        return Objects.equals(worker, that.worker) &&
                Objects.equals(manager, that.manager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worker, manager);
    }
}
