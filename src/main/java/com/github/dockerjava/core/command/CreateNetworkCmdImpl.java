package com.github.dockerjava.core.command;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.DockerCmdSyncExec;
import com.github.dockerjava.api.model.Network;

import java.util.Map;

public class CreateNetworkCmdImpl extends AbstrDockerCmd<CreateNetworkCmd, CreateNetworkResponse>
    implements CreateNetworkCmd {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("IPAM")
    private Network.Ipam ipam;

    public CreateNetworkCmdImpl(DockerCmdSyncExec<CreateNetworkCmd, CreateNetworkResponse> execution) {
        super(execution);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDriver() {
        return driver;
    }

    @Override
    public Network.Ipam getIpam() {
        return ipam;
    }

    @Override
    public CreateNetworkCmd withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public CreateNetworkCmd withDriver(String driver) {
        this.driver = driver;
        return this;
    }

    @Override
    public CreateNetworkCmd withIpamConfig(Map<String, String> config) {
        ipam.getConfig().add(config);
        return this;
    }
}
