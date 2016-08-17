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
public class SwarmNodeManagerStatus implements Serializable {
    public static final Long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Leader")
    private boolean leader;

    /**
     * @since 1.24
     */
    @JsonProperty("Reachability")
    private Reachability reachability;

    /**
     * @since 1.24
     */
    @JsonProperty("Addr")
    private String addr;

    /**
     * @see #leader
     */
    @CheckForNull
    public boolean isLeader() {
        return leader;
    }

    /**
     * @see #leader
     */
    public SwarmNodeManagerStatus withLeader(boolean leader) {
        this.leader = leader;
        return this;
    }

    /**
     * @see #reachability
     */
    @CheckForNull
    public Reachability getReachability() {
        return reachability;
    }

    /**
     * @see #reachability
     */
    public SwarmNodeManagerStatus withReachability(Reachability reachability) {
        this.reachability = reachability;
        return this;
    }

    /**
     * @see #addr
     */
    @CheckForNull
    public String getAddr() {
        return addr;
    }

    /**
     * @see #addr
     */
    public SwarmNodeManagerStatus withAddr(String addr) {
        this.addr = addr;
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
