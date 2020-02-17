package com.github.dockerjava.core.command;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.JoinSwarmCmd;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.util.List;

public class JoinSwarmCmdImpl extends AbstrDockerCmd<JoinSwarmCmd, Void> implements
        JoinSwarmCmd {

    @JsonProperty("ListenAddr")
    private String listenAddr = "0.0.0.0";

    @JsonProperty("AdvertiseAddr")
    private String advertiseAddr;

    @JsonProperty("RemoteAddrs")
    private List<String> remoteAddrs;

    @JsonProperty("JoinToken")
    private String joinToken;

    public JoinSwarmCmdImpl(JoinSwarmCmd.Exec exec) {
        super(exec);
    }

    @Override
    @CheckForNull
    public String getListenAddr() {
        return listenAddr;
    }

    @Override
    public JoinSwarmCmd withListenAddr(String listenAddr) {
        this.listenAddr = listenAddr;
        return this;
    }

    @Override
    @CheckForNull
    public String getAdvertiseAddr() {
        return advertiseAddr;
    }

    @Override
    public JoinSwarmCmd withAdvertiseAddr(String advertiseAddr) {
        this.advertiseAddr = advertiseAddr;
        return this;
    }

    @Override
    @CheckForNull
    public List<String> getRemoteAddrs() {
        return remoteAddrs;
    }

    @Override
    public JoinSwarmCmd withRemoteAddrs(List<String> remoteAddrs) {
        this.remoteAddrs = remoteAddrs;
        return this;
    }

    @Override
    @CheckForNull
    public String getJoinToken() {
        return joinToken;
    }

    @Override
    public JoinSwarmCmd withJoinToken(String joinToken) {
        this.joinToken = joinToken;
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
