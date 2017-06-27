package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;

/**
 * @since 1.24
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwarmInfo implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("NodeID")
    private String nodeID;

    /**
     * @since 1.24
     */
    @JsonProperty("NodeAddr")
    private String nodeAddr;

    /**
     * @since 1.24
     */
    @JsonProperty("LocalNodeState")
    private LocalNodeState localNodeState;

    /**
     * @since 1.24
     */
    @JsonProperty("ControlAvailable")
    private Boolean controlAvailable;

    /**
     * @since 1.24
     */
    @JsonProperty("Error")
    private String error;

    /**
     * @since 1.24
     */
    @JsonProperty("RemoteManagers")
    private List<PeerNode> remoteManagers;

    /**
     * @since 1.24
     */
    @JsonProperty("Nodes")
    private Integer nodes;

    /**
     * @since 1.24
     */
    @JsonProperty("Managers")
    private Integer managers;

    /**
     * @since 1.24
     */
    @JsonProperty("ClusterInfo")
    private ClusterInfo clusterInfo;

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
    public SwarmInfo withNodeID(String nodeID) {
        this.nodeID = nodeID;
        return this;
    }

    /**
     * @see #nodeAddr
     */
    @CheckForNull
    public String getNodeAddr() {
        return nodeAddr;
    }

    /**
     * @see #nodeAddr
     */
    public SwarmInfo withNodeAddr(String nodeAddr) {
        this.nodeAddr = nodeAddr;
        return this;
    }

    /**
     * @see #localNodeState
     */
    @CheckForNull
    public LocalNodeState getLocalNodeState() {
        return localNodeState;
    }

    /**
     * @see #localNodeState
     */
    public SwarmInfo withLocalNodeState(LocalNodeState localNodeState) {
        this.localNodeState = localNodeState;
        return this;
    }

    /**
     * @see #controlAvailable
     */
    @CheckForNull
    public Boolean getControlAvailable() {
        return controlAvailable;
    }

    /**
     * @see #controlAvailable
     */
    public SwarmInfo withControlAvailable(Boolean controlAvailable) {
        this.controlAvailable = controlAvailable;
        return this;
    }

    /**
     * @see #error
     */
    @CheckForNull
    public String getError() {
        return error;
    }

    /**
     * @see #error
     */
    public SwarmInfo withError(String error) {
        this.error = error;
        return this;
    }

    /**
     * @see #remoteManagers
     */
    @CheckForNull
    public List<PeerNode> getRemoteManagers() {
        return remoteManagers;
    }

    /**
     * @see #remoteManagers
     */
    public SwarmInfo withRemoteManagers(List<PeerNode> remoteManagers) {
        this.remoteManagers = remoteManagers;
        return this;
    }

    /**
     * @see #nodes
     */
    @CheckForNull
    public Integer getNodes() {
        return nodes;
    }

    /**
     * @see #nodes
     */
    public SwarmInfo withNodes(Integer nodes) {
        this.nodes = nodes;
        return this;
    }

    /**
     * @see #managers
     */
    @CheckForNull
    public Integer getManagers() {
        return managers;
    }

    /**
     * @see #managers
     */
    public SwarmInfo withManagers(Integer managers) {
        this.managers = managers;
        return this;
    }

    /**
     * @see #clusterInfo
     */
    @CheckForNull
    public ClusterInfo getClusterInfo() {
        return clusterInfo;
    }

    /**
     * @see #clusterInfo
     */
    public SwarmInfo withClusterInfo(ClusterInfo clusterInfo) {
        this.clusterInfo = clusterInfo;
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
