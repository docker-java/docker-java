package com.github.dockerjava.core.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.DisconnectFromNetworkCmd;
import com.github.dockerjava.api.command.DockerCmdSyncExec;

public class DisconnectFromNetworkCmdImpl extends AbstrDockerCmd<DisconnectFromNetworkCmd, Void>
    implements DisconnectFromNetworkCmd {

    @JsonIgnore
    private String networkId;

    @JsonProperty("Container")
    private String containerId;

    public DisconnectFromNetworkCmdImpl(DockerCmdSyncExec<DisconnectFromNetworkCmd, Void> execution) {
        super(execution);
    }

    @Override
    public String getNetworkId() {
        return networkId;
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override public DisconnectFromNetworkCmd withNetworkId(String networkId) {
        this.networkId = networkId;
        return this;
    }

    @Override public DisconnectFromNetworkCmd withContainerId(String containerId) {
        this.containerId = containerId;
        return this;
    }
}
