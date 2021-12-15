package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.DockerCmdSyncExec;
import com.github.dockerjava.api.command.InspectNetworkCmd;
import com.github.dockerjava.api.model.Network;

public class InpectNetworkCmdImpl extends AbstrDockerCmd<InspectNetworkCmd, Network> implements InspectNetworkCmd {

    private String networkId;

    public InpectNetworkCmdImpl(DockerCmdSyncExec<InspectNetworkCmd, Network> exec) {
        super(exec);
    }

    @Override
    public String getNetworkId() {
        return networkId;
    }

    @Override
    public InspectNetworkCmd withNetworkId(String networkId) {

        this.networkId = networkId;
        return this;
    }
}
