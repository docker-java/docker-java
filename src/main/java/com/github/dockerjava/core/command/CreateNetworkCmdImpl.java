package com.github.dockerjava.core.command;


import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.DockerCmdSyncExec;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.Network.Ipam;

public class CreateNetworkCmdImpl extends AbstrDockerCmd<CreateNetworkCmd, CreateNetworkResponse>
    implements CreateNetworkCmd {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("IPAM")
    private Network.Ipam ipam;

    @JsonProperty("Options")
    private Map<String, String> options = new HashMap<>();

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
    public CreateNetworkCmd withIpamConfig(Ipam.Config config) {
        this.ipam.getConfig().add(config);
        return this;
    }

    @Override
    public CreateNetworkCmd withOptions(Map<String, String> options) {
        this.options = options;
        return this;
    }
}
