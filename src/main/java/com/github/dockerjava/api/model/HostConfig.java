package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HostConfig {

	@JsonProperty("Binds")
	private String[] binds;

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

	public HostConfig() {
	}

	public HostConfig(String[] binds, Links links, LxcConf[] lxcConf, Ports portBindings, boolean publishAllPorts,
			boolean privileged, String[] dns, String[] dnsSearch, VolumesFrom[] volumesFrom, String containerIDFile,
			Capability[] capAdd, Capability[] capDrop, RestartPolicy restartPolicy, String networkMode, Device[] devices) {
		this.binds = binds;
		this.links = links;
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
	}

	public String[] getBinds() {
		return binds;
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

	public Links getLinks() {
		return links;
	}

	public String getNetworkMode() {
		return networkMode;
	}

	public Device[] getDevices() {
		return devices;
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

	public void setBinds(String[] binds) {
		this.binds = binds;
	}

	public void setLinks(Links links) {
		this.links = links;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
