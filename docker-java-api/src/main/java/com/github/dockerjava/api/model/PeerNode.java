package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @since 1.24
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Override
    public String toString() {
        return "PeerNode{" +
                "nodeID='" + nodeID + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeerNode peerNode = (PeerNode) o;
        return Objects.equals(nodeID, peerNode.nodeID) &&
                Objects.equals(addr, peerNode.addr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeID, addr);
    }
}
