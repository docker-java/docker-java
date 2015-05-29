package com.github.dockerjava.core.command;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Binds;
import com.github.dockerjava.api.model.Capability;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.Links;
import com.github.dockerjava.api.model.LxcConf;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.RestartPolicy;


/**
 * Start a container
 */
@JsonInclude(NON_EMPTY)
public class StartContainerCmdImpl extends AbstrDockerCmd<StartContainerCmd, Void> implements StartContainerCmd {

	@JsonIgnore
	private String containerId;

	@JsonProperty("Binds")
	private Binds binds;

	@JsonProperty("Links")
	private Links links;

	@JsonProperty("LxcConf")
	private LxcConf[] lxcConf;

	@JsonProperty("Memory")
	private long memoryLimit = 0;

	@JsonProperty("MemorySwap")
	private long memorySwap = 0;

	@JsonProperty("CpuShares")
	private long cpuShares = 0;

	@JsonProperty("CpusetCpus")
	private String cpusetCpus;

	@JsonProperty("PortBindings")
	private Ports portBindings;

	@JsonProperty("PublishAllPorts")
	private Boolean publishAllPorts;

	@JsonProperty("Privileged")
	private Boolean privileged;

	@JsonProperty("Dns")
	private String[] dns;
	
	@JsonProperty("DnsSearch")
	private String[] dnsSearch;

	@JsonProperty("VolumesFrom")
	private String volumesFrom;
	
	@JsonProperty("NetworkMode")          
    private String networkMode;
	
	@JsonProperty("Devices")
	private Device[] devices;
	
	@JsonProperty("ExtraHosts")
	private String[] extraHosts;
	
	@JsonProperty("RestartPolicy")
	private RestartPolicy restartPolicy;
	
	@JsonProperty("CapAdd")
	private Capability[] capAdd;
	
	@JsonProperty("CapDrop")
	private Capability[] capDrop;

	@JsonProperty("ReadonlyRootfs")
	private Boolean readonlyRootfs;

	public StartContainerCmdImpl(StartContainerCmd.Exec exec, String containerId) {
		super(exec);
		withContainerId(containerId);
	}

	@Override
	@JsonIgnore
	public Bind[] getBinds() {
		return (binds == null) ? new Bind[0] : binds.getBinds();
	}

	@Override
	@JsonIgnore
	public Link[] getLinks() {
		return (links == null) ? new Link[0] : links.getLinks();
	}

	@Override
	public LxcConf[] getLxcConf() {
		return lxcConf;
	}

	@Override
	public long getMemoryLimit() {
		return memoryLimit;
	}

	@Override
	public long getMemorySwap() {
		return memorySwap;
	}

	@Override
	public Ports getPortBindings() {
		return portBindings;
	}

	@Override
	public Boolean isPublishAllPorts() {
		return publishAllPorts;
	}

	@Override
	public Boolean isPrivileged() {
		return privileged;
	}

	@Override
	public Boolean isReadonlyRootfs() {
		return null;
	}

	@Override
	public String[] getDns() {
		return dns;
	}
	
	@Override
	public String[] getDnsSearch() {
		return dnsSearch;
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
    public String[] getExtraHosts() {
		return extraHosts;
	}
    
    @Override
    public RestartPolicy getRestartPolicy() {
    	return restartPolicy;
    }
    
    @Override
    public Capability[] getCapAdd() {
    	return capAdd;
    }
    
    @Override
    public Capability[] getCapDrop() {
    	return capDrop;
    }

	public Boolean getReadonlyRootfs() {
		return readonlyRootfs;
	}

	public long getCpuShares() {
		return cpuShares;
	}

	public String getCpusetCpus() {
		return cpusetCpus;
	}

	public Boolean getPublishAllPorts() {
		return publishAllPorts;
	}

	public Boolean getPrivileged() {
		return privileged;
	}

	@Override
	@JsonIgnore
	public StartContainerCmd withBinds(Bind... binds) {
		checkNotNull(binds, "binds was not specified");
		this.binds = new Binds(binds);
		return this;
	}

	@Override
	@JsonIgnore
	public StartContainerCmd withLinks(Link... links) {
		checkNotNull(links, "links was not specified");
		this.links = new Links(links);
		return this;
	}

	@Override
	public StartContainerCmd withLxcConf(LxcConf... lxcConf) {
		checkNotNull(lxcConf, "lxcConf was not specified");
		this.lxcConf = lxcConf;
		return this;
	}

	@Override
	public StartContainerCmd withMemoryLimit(long memoryLimit) {
		this.memoryLimit = memoryLimit;
		return this;
	}

	@Override
	public StartContainerCmd withMemorySwap(long memorySwap) {
		this.memorySwap = memorySwap;
		return this;
	}

	@Override
	public StartContainerCmd withCpusetCpus(String cpusetCpus) {
		this.cpusetCpus = cpusetCpus;
		return this;
	}

	@Override
	public StartContainerCmd withCpuShares(int cpuShares) {
		this.cpuShares = cpuShares;
		return this;
	}


	@Override
	public StartContainerCmd withPortBindings(Ports portBindings) {
		checkNotNull(portBindings, "portBindings was not specified");
		this.portBindings = portBindings;
		return this;
	}

	@Override
	public StartContainerCmd withPortBindings(PortBinding... portBindings) {
		checkNotNull(portBindings, "portBindings was not specified");
		if (this.portBindings == null) {
			this.portBindings = new Ports();
		}
		this.portBindings.add(portBindings);
		return this;
	}

	@Override
	public StartContainerCmd withPrivileged(Boolean privileged) {
		checkNotNull(privileged, "privileged was not specified");
		this.privileged = privileged;
		return this;
	}

	@Override
	public StartContainerCmd withReadonlyRootfs(Boolean readonlyRootfs) {
		checkNotNull(readonlyRootfs, "ReadonlyRootfs was not specified");
		this.readonlyRootfs = readonlyRootfs;
		return this;
	}

	@Override
	public StartContainerCmd withPublishAllPorts(Boolean publishAllPorts) {
		checkNotNull(publishAllPorts, "PublishAllPorts was not specified");
		this.publishAllPorts = publishAllPorts;
		return this;
	}

	@Override
	public StartContainerCmd withDns(String... dns) {
		checkNotNull(dns, "dns was not specified");
		this.dns = dns;
		return this;
	}
	
	@Override
	public StartContainerCmd withDnsSearch(String... dnsSearch) {
		checkNotNull(dnsSearch, "dnsSearch was not specified");
		this.dnsSearch = dnsSearch;
		return this;
	}

	@Override
	public StartContainerCmd withVolumesFrom(String volumesFrom) {
		checkNotNull(volumesFrom, "volumesFrom was not specified");
		this.volumesFrom = volumesFrom;
		return this;
	}

	@Override
	public StartContainerCmd withContainerId(String containerId) {
	    checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
    }

    @Override
	public StartContainerCmd withNetworkMode(String networkMode) {
        checkNotNull(networkMode, "networkMode was not specified");
        this.networkMode = networkMode;
        return this;
    }
    
    @Override
	public StartContainerCmd withDevices(Device... devices) {
		checkNotNull(devices, "devices was not specified");
		this.devices = devices;
		return this;
	}

    @Override
	public StartContainerCmd withExtraHosts(String... extraHosts) {
		checkNotNull(extraHosts, "extraHosts was not specified");
		this.extraHosts = extraHosts;
		return this;
	}
    
    
    @Override
   	public StartContainerCmd withRestartPolicy(RestartPolicy restartPolicy) {
   		checkNotNull(restartPolicy, "restartPolicy was not specified");
   		this.restartPolicy = restartPolicy;
   		return this;
   	}
    
    @Override
	public StartContainerCmd withCapAdd(Capability... capAdd) {
		checkNotNull(capAdd, "capAdd was not specified");
		this.capAdd = capAdd;
		return this;
	}
    
    @Override
	public StartContainerCmd withCapDrop(Capability... capDrop) {
		checkNotNull(capDrop, "capDrop was not specified");
		this.capDrop = capDrop;
		return this;
	}

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
