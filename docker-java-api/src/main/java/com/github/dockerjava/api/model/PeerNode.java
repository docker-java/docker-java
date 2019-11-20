package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since 1.24
 */
@EqualsAndHashCode
@ToString
public class PeerNode implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("NodeID")
    private String nodeID;

    /**
     * @since 1.24
     */
    @JsonProperty("Addr")
    private String addr;

    /**
     * @see #nodeID
     */
    @CheckForNull
    public String getNodeID() {
        return nodeID;
    }

    /**
     * @see #nodeID
     */
    public PeerNode withNodeID(String nodeID) {
        this.nodeID = nodeID;
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
    public PeerNode withAddr(String addr) {
        this.addr = addr;
        return this;
    }
}
