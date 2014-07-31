package com.github.dockerjava.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;


import java.util.Arrays;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HostConfig {

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

    @JsonProperty("ContainerIDFile")
    public String containerIDFile;

    @JsonProperty("DnsSearch")
    public String dnsSearch;

    @JsonProperty("Links")
    public String[] links;

    @JsonProperty("NetworkMode")
    public String networkMode;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
