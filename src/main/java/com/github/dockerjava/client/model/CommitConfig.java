package com.github.dockerjava.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class CommitConfig {

    @JsonProperty("AttachStdin")
    private boolean attachStdin;

    @JsonProperty("AttachStdout")
    private boolean attachStdout;

    @JsonProperty("AttachStderr")
    private boolean attachStderr;

    @JsonProperty("Cmd")
    private String[] cmd;
    
    @JsonProperty("DisableNetwork")
    private boolean disableNetwork;
    
    @JsonProperty("Env")
    private String[] env;
    
    @JsonProperty("ExposedPorts")
    private ExposedPorts exposedPorts;
    
    @JsonProperty("Hostname")
    private String hostname;
    
    @JsonProperty("Memory")
    private Integer memory;
    
    @JsonProperty("MemorySwap")
    private Integer memorySwap;
    
    @JsonProperty("OpenStdin")
    private boolean openStdin;
    
    @JsonProperty("PortSpecs")
    private String[] portSpecs;
    
    @JsonProperty("StdinOnce")
    private boolean stdinOnce;
    
    @JsonProperty("Tty")
    private boolean tty;
    
    @JsonProperty("User")
    private String user;

    @JsonProperty("Volumes")
    private Volumes volumes;

    @JsonProperty("WorkingDir")
    private String workingDir;

	public boolean isAttachStdin() {
		return attachStdin;
	}

	public void setAttachStdin(boolean attachStdin) {
		this.attachStdin = attachStdin;
	}

	public boolean isAttachStdout() {
		return attachStdout;
	}

	public void setAttachStdout(boolean attachStdout) {
		this.attachStdout = attachStdout;
	}

	public boolean isAttachStderr() {
		return attachStderr;
	}

	public void setAttachStderr(boolean attachStderr) {
		this.attachStderr = attachStderr;
	}

	public String[] getCmd() {
		return cmd;
	}

	public void setCmd(String[] cmd) {
		this.cmd = cmd;
	}

	public boolean isDisableNetwork() {
		return disableNetwork;
	}

	public void setDisableNetwork(boolean disableNetwork) {
		this.disableNetwork = disableNetwork;
	}

	public String[] getEnv() {
		return env;
	}

	public void setEnv(String[] env) {
		this.env = env;
	}

	public ExposedPorts getExposedPorts() {
		return exposedPorts;
	}

	public void setExposedPorts(ExposedPorts exposedPorts) {
		this.exposedPorts = exposedPorts;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Integer getMemory() {
		return memory;
	}

	public void setMemory(Integer memory) {
		this.memory = memory;
	}

	public Integer getMemorySwap() {
		return memorySwap;
	}

	public void setMemorySwap(Integer memorySwap) {
		this.memorySwap = memorySwap;
	}

	public boolean isOpenStdin() {
		return openStdin;
	}

	public void setOpenStdin(boolean openStdin) {
		this.openStdin = openStdin;
	}

	public String[] getPortSpecs() {
		return portSpecs;
	}

	public void setPortSpecs(String[] portSpecs) {
		this.portSpecs = portSpecs;
	}

	public boolean isStdinOnce() {
		return stdinOnce;
	}

	public void setStdinOnce(boolean stdinOnce) {
		this.stdinOnce = stdinOnce;
	}

	public boolean isTty() {
		return tty;
	}

	public void setTty(boolean tty) {
		this.tty = tty;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Volumes getVolumes() {
		return volumes;
	}

	public void setVolumes(Volumes volumes) {
		this.volumes = volumes;
	}

	public String getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}
    
    
    
}
