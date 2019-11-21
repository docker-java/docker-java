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
public class SwarmNodeManagerStatus implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @FieldName("Leader")
    private boolean leader;

    /**
     * @since 1.24
     */
    @FieldName("Reachability")
    private Reachability reachability;

    /**
     * @since 1.24
     */
    @FieldName("Addr")
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
}
