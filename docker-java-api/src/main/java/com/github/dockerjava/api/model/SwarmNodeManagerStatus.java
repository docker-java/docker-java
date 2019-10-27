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
public class SwarmNodeManagerStatus implements Serializable {
    public static final long serialVersionUID = 1L;

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
        return "SwarmNodeManagerStatus{" +
                "leader=" + leader +
                ", reachability=" + reachability +
                ", addr='" + addr + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmNodeManagerStatus that = (SwarmNodeManagerStatus) o;
        return leader == that.leader &&
                reachability == that.reachability &&
                Objects.equals(addr, that.addr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leader, reachability, addr);
    }
}
