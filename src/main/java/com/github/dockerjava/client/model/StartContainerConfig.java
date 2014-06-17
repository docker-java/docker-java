package com.github.dockerjava.client.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class StartContainerConfig {

    @JsonProperty("Binds")
    public String[] binds;
    
    @JsonProperty("LxcConf")
    public LxcConf[] lxcConf;
    
    @JsonProperty("PortBindings")
    public Ports portBindings;
    
    @JsonProperty("PublishAllPorts")
    public boolean publishAllPorts;
    
    @JsonProperty("Privileged")
    public boolean privileged;
    
    @JsonProperty("Dns")
    public String dns;
    
    @JsonProperty("VolumesFrom")
    public String volumesFrom;
    
    @Override
    public String toString() {
        return "StartContainerConfig{" +
                "binds=" + Arrays.toString(binds) +
                ", lxcConf=" + Arrays.toString(lxcConf) +
                ", portBindings=" + portBindings +
                ", privileged=" + privileged +
                ", publishAllPorts=" + publishAllPorts +
                ", dns='" + dns + '\'' +
                '}';
    }
    
    
    
}
