package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;


@JsonIgnoreProperties(ignoreUnknown = true)
public class HostConfig {

	@JsonProperty("Binds")
	private Binds binds;

	@JsonProperty("Links")
	private Links links;

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

	@JsonProperty("DnsSearch")
	private String[] dnsSearch;

	@JsonProperty("VolumesFrom")
	private VolumesFrom[] volumesFrom;

	@JsonProperty("ContainerIDFile")
	private String containerIDFile;

	@JsonProperty("CapAdd")
	private Capability[] capAdd;

	@JsonProperty("CapDrop")
	private Capability[] capDrop;

	@JsonProperty("RestartPolicy")
	private RestartPolicy restartPolicy;

	@JsonProperty("NetworkMode")
	private String networkMode;

	@JsonProperty("Devices")
	private Device[] devices;

	@JsonProperty("ExtraHosts")
	private String[] extraHosts;

	@JsonProperty("Ulimits")
	private Ulimit[] ulimits;

	@JsonProperty("CpuShares")
	private String cpuShares;

	@JsonProperty("CpusetCpus")
	private String cpusetCpus;

	public HostConfig() {
	}

	public HostConfig(Bind[] binds, Link[] links, LxcConf[] lxcConf, Ports portBindings, boolean publishAllPorts,
			boolean privileged, String[] dns, String[] dnsSearch, VolumesFrom[] volumesFrom, String containerIDFile,
			Capability[] capAdd, Capability[] capDrop, RestartPolicy restartPolicy, String networkMode, Device[] devices,
            String[] extraHosts, Ulimit[] ulimits, String cpuShares, String cpusetCpus) {
		this.binds = new Binds(binds);
		this.links = new Links(links);
		this.lxcConf = lxcConf;
		this.portBindings = portBindings;
		this.publishAllPorts = publishAllPorts;
		this.privileged = privileged;
		this.dns = dns;
		this.dnsSearch = dnsSearch;
		this.volumesFrom = volumesFrom;
		this.containerIDFile = containerIDFile;
		this.capAdd = capAdd;
		this.capDrop = capDrop;
		this.restartPolicy = restartPolicy;
		this.networkMode = networkMode;
		this.devices = devices;
		this.extraHosts = extraHosts;
		this.ulimits = ulimits;
		this.cpuShares = cpuShares;
		this.cpusetCpus = cpusetCpus;
	}


	@JsonIgnore
	public Bind[] getBinds() {
		return (binds == null) ? new Bind[0] : binds.getBinds();
	}

	public LxcConf[] getLxcConf() {
		return lxcConf;
	}
	
	public Ports getPortBindings() {
		return portBindings;
	}

	public boolean isPublishAllPorts() {
		return publishAllPorts;
	}

	public boolean isPrivileged() {
		return privileged;
	}

	public String[] getDns() {
		return dns;
	}

	public VolumesFrom[] getVolumesFrom() {
		return volumesFrom;
	}

	public String getContainerIDFile() {
		return containerIDFile;
	}

	public String[] getDnsSearch() {
		return dnsSearch;
	}

	@JsonIgnore
	public Link[] getLinks() {
		return (links == null) ? new Link[0] : links.getLinks();
	}

	public String getNetworkMode() {
		return networkMode;
	}

	public Device[] getDevices() {
		return devices;
	}

	public String[] getExtraHosts() {
		return extraHosts;
	}

	public RestartPolicy getRestartPolicy() {
		return restartPolicy;
	}

	public Capability[] getCapAdd() {
		return capAdd;
	}

	public Capability[] getCapDrop() {
		return capDrop;
	}

	public Ulimit[] getUlimits() {
		return ulimits;
	}

	public String getCpuShares() {
		return cpuShares;
	}

	public String getCpusetCpus() {
		return cpusetCpus;
	}

	@JsonIgnore
	public void setBinds(Bind... binds) {
		this.binds = new Binds(binds);
	}

	@JsonIgnore
	public void setLinks(Link... links) {
		this.links = new Links(links);
	}

	public void setLxcConf(LxcConf[] lxcConf) {
		this.lxcConf = lxcConf;
	}

	public void setPortBindings(Ports portBindings) {
		this.portBindings = portBindings;
	}

	public void setPublishAllPorts(boolean publishAllPorts) {
		this.publishAllPorts = publishAllPorts;
	}

	public void setPrivileged(boolean privileged) {
		this.privileged = privileged;
	}

	public void setDns(String[] dns) {
		this.dns = dns;
	}

	public void setDnsSearch(String[] dnsSearch) {
		this.dnsSearch = dnsSearch;
	}

	public void setVolumesFrom(VolumesFrom[] volumesFrom) {
		this.volumesFrom = volumesFrom;
	}

	public void setContainerIDFile(String containerIDFile) {
		this.containerIDFile = containerIDFile;
	}

	public void setCapAdd(Capability[] capAdd) {
		this.capAdd = capAdd;
	}

	public void setCapDrop(Capability[] capDrop) {
		this.capDrop = capDrop;
	}

	public void setRestartPolicy(RestartPolicy restartPolicy) {
		this.restartPolicy = restartPolicy;
	}

	public void setNetworkMode(String networkMode) {
		this.networkMode = networkMode;
	}

	public void setDevices(Device[] devices) {
		this.devices = devices;
	}

	public void setExtraHosts(String[] extraHosts) {
		this.extraHosts = extraHosts;
	}

	public void setUlimits(Ulimit[] ulimits) {
		this.ulimits = ulimits;
	}

	public void setCpuShares(String cpuShares) {
		this.cpuShares = cpuShares;
	}

	public void setCpusetCpus(String cpusetCpus) {
		this.cpusetCpus = cpusetCpus;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
