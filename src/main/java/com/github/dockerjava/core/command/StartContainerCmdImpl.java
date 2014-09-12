package com.github.dockerjava.core.command;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Binds;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.Links;
import com.github.dockerjava.api.model.LxcConf;
import com.github.dockerjava.api.model.Ports;
import com.google.common.base.Preconditions;

/**
 * Start a container
 */
public class StartContainerCmdImpl extends AbstrDockerCmd<StartContainerCmd, Void> implements StartContainerCmd {

	private String containerId;

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
	private String[] dns;

	@JsonProperty("VolumesFrom")
	private String volumesFrom;
	
	@JsonProperty("NetworkMode")          
    private String networkMode = "bridge";
	
	@JsonProperty("Devices")
	private Device[] devices;
	
	@JsonProperty("CapAdd")
	private String[] capAdd;
	
	@JsonProperty("CapDrop")
	private String[] capDrop;
	
	public StartContainerCmdImpl(StartContainerCmd.Exec exec, String containerId) {
		super(exec);
		withContainerId(containerId);
	}

	@Override
	@JsonIgnore
	public Bind[] getBinds() {
		return binds.getBinds();
	}

	@Override
	@JsonIgnore
	public Link[] getLinks() {
		return links.getLinks();
	}

	@Override
	public LxcConf[] getLxcConf() {
		return lxcConf;
	}

	@Override
	public Ports getPortBindings() {
		return portBindings;
	}

	@Override
	public boolean isPublishAllPorts() {
		return publishAllPorts;
	}

	@Override
	public boolean isPrivileged() {
		return privileged;
	}

	@Override
	public String[] getDns() {
		return dns;
	}

	@Override
	public String getVolumesFrom() {
		return volumesFrom;
	}

	@Override
	public String getContainerId() {
		return containerId;
	}
	   
    @Override
	public String getNetworkMode() {        
        return networkMode;
    }
    
    @Override
    public Device[] getDevices() {
		return devices;
	}
    
    @Override
    public String[] getCapAdd() {
    	return capAdd;
    }
    
    @Override
    public String[] getCapDrop() {
    	return capDrop;
    }

	@Override
	@JsonIgnore
	public StartContainerCmd withBinds(Bind... binds) {
		Preconditions.checkNotNull(binds, "binds was not specified");
		this.binds = new Binds(binds);
		return this;
	}

	@Override
	@JsonIgnore
	public StartContainerCmd withLinks(Link... links) {
		Preconditions.checkNotNull(links, "links was not specified");
		this.links = new Links(links);
		return this;
	}

	@Override
	public StartContainerCmd withLxcConf(LxcConf... lxcConf) {
		Preconditions.checkNotNull(lxcConf, "lxcConf was not specified");
		this.lxcConf = lxcConf;
		return this;
	}

	@Override
	public StartContainerCmd withPortBindings(Ports portBindings) {
		Preconditions.checkNotNull(portBindings,
				"portBindings was not specified");
		this.portBindings = portBindings;
		return this;
	}

	@Override
	public StartContainerCmd withPrivileged(boolean privileged) {
		this.privileged = privileged;
		return this;
	}

	@Override
	public StartContainerCmd withPublishAllPorts(boolean publishAllPorts) {
		this.publishAllPorts = publishAllPorts;
		return this;
	}

	@Override
	public StartContainerCmd withDns(String... dns) {
		Preconditions.checkNotNull(dns, "dns was not specified");
		this.dns = dns;
		return this;
	}

	@Override
	public StartContainerCmd withVolumesFrom(String volumesFrom) {
		Preconditions
				.checkNotNull(volumesFrom, "volumesFrom was not specified");
		this.volumesFrom = volumesFrom;
		return this;
	}

	@Override
	public StartContainerCmd withContainerId(String containerId) {
		Preconditions
				.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
    }

    @Override
	public StartContainerCmd withNetworkMode(String networkMode) {
        Preconditions.checkNotNull(networkMode, "networkMode was not specified");
        this.networkMode = networkMode;
        return this;
    }
    
    @Override
	public StartContainerCmd withDevices(Device... devices) {
		Preconditions.checkNotNull(devices, "devices was not specified");
		this.devices = devices;
		return this;
	}
    
    @Override
	public StartContainerCmd withCapAdd(String... capAdd) {
		Preconditions.checkNotNull(capAdd, "capAdd was not specified");
		this.capAdd = capAdd;
		return this;
	}
    
    @Override
	public StartContainerCmd withCapDrop(String... capDrop) {
		Preconditions.checkNotNull(capDrop, "capDrop was not specified");
		this.capDrop = capDrop;
		return this;
	}
    
//    "RestartPolicy": {
//
//	    "MaximumRetryCount": 0,
//	    "Name": ""
//
//	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this).toString();
	}
	
	/**
	 * @throws NotFoundException No such container
	 * @throws NotModifiedException Container already started
	 */
	@Override
	public Void exec() throws NotFoundException, NotModifiedException {
		return super.exec();
	}
}
