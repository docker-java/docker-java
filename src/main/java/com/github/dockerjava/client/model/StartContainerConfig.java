package com.github.dockerjava.client.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class StartContainerConfig {

    @JsonProperty("Binds")
    private Binds binds = new Binds();

    @JsonProperty("Links")
    private Links links = new Links();

    @JsonProperty("LxcConf")
    private LxcConf[] lxcConf;

    @JsonProperty("PortBindings")
    private Ports portBindings;

    @JsonProperty("PublishAllPorts")
    private boolean publishAllPorts;

    @JsonProperty("Privileged")
    private boolean privileged;

    @JsonProperty("Dns")
    private String dns;

    @JsonProperty("VolumesFrom")
    private String volumesFrom;

    @JsonIgnore
    public Bind[] getBinds() {
		return binds.getBinds();
	}

    @JsonIgnore
	public void setBinds(Bind[] binds) {
		this.binds = new Binds(binds);
	}

    @JsonIgnore
    public Link[] getLinks() {
		return links.getLinks();
	}

    @JsonIgnore
	public void setLinks(Link[] links) {
		this.links = new Links(links);
	}

	public LxcConf[] getLxcConf() {
		return lxcConf;
	}

	public void setLxcConf(LxcConf[] lxcConf) {
		this.lxcConf = lxcConf;
	}

	public Ports getPortBindings() {
		return portBindings;
	}

	public void setPortBindings(Ports portBindings) {
		this.portBindings = portBindings;
	}

	public boolean isPublishAllPorts() {
		return publishAllPorts;
	}

	public void setPublishAllPorts(boolean publishAllPorts) {
		this.publishAllPorts = publishAllPorts;
	}

	public boolean isPrivileged() {
		return privileged;
	}

	public void setPrivileged(boolean privileged) {
		this.privileged = privileged;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getVolumesFrom() {
		return volumesFrom;
	}

	public void setVolumesFrom(String volumesFrom) {
		this.volumesFrom = volumesFrom;
	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

