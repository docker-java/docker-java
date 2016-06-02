package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.DockerCmdSyncExec;
import com.github.dockerjava.api.command.RemoveNetworkCmd;

public class RemoveNetworkCmdImpl extends AbstrDockerCmd<RemoveNetworkCmd, Void> implements RemoveNetworkCmd {

    private String networkId;

    public RemoveNetworkCmdImpl(DockerCmdSyncExec<RemoveNetworkCmd, Void> execution, String networkId) {
        super(execution);
        withNetworkId(networkId);
    }

    @Override
    public String getNetworkId() {
        return networkId;
    }

    @Override
    public RemoveNetworkCmd withNetworkId(String networkId) {
        this.networkId = networkId;
        return this;
    }
}
