package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwarmJoinTokens implements Serializable {

    public static final Long serialVersionUID = 1L;

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
