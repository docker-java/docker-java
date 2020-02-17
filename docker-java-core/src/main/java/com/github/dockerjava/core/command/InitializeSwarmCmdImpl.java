package com.github.dockerjava.core.command;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.InitializeSwarmCmd;
import com.github.dockerjava.api.model.SwarmSpec;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;

public class InitializeSwarmCmdImpl extends AbstrDockerCmd<InitializeSwarmCmd, Void> implements
        InitializeSwarmCmd {

    @JsonProperty("ListenAddr")
    private String listenAddr = "0.0.0.0";

    @JsonProperty("AdvertiseAddr")
    private String advertiseAddr;

    @JsonProperty("ForceNewCluster")
    private boolean forceNewCluster;

    @JsonProperty("Spec")
    private SwarmSpec spec;

    public InitializeSwarmCmdImpl(InitializeSwarmCmd.Exec exec, SwarmSpec swarmSpec) {
        super(exec);
        this.spec = swarmSpec;
    }

    @Override
    @CheckForNull
    public String getListenAddr() {
        return listenAddr;
    }

    @Override
    public InitializeSwarmCmd withListenAddr(String listenAddr) {
        this.listenAddr = listenAddr;
        return this;
    }

    @Override
    @CheckForNull
    public String getAdvertiseAddr() {
        return advertiseAddr;
    }

    @Override
    public InitializeSwarmCmd withAdvertiseAddr(String advertiseAddr) {
        this.advertiseAddr = advertiseAddr;
        return this;
    }

    @Override
    @CheckForNull
    public Boolean isForceNewCluster() {
        return forceNewCluster;
    }

    @Override
    public InitializeSwarmCmd withForceNewCluster(Boolean forceNewCluster) {
        this.forceNewCluster = forceNewCluster;
        return this;
    }

    @Override
    @CheckForNull
    public SwarmSpec getSwarmSpec() {
        return spec;
    }

    @Override
    public InitializeSwarmCmd withSwarmSpec(SwarmSpec swarmSpec) {
        this.spec = swarmSpec;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
