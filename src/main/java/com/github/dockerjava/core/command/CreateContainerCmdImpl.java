package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Capability;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.ExposedPorts;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.VolumesFrom;
import com.github.dockerjava.api.model.Volumes;

/**
 *
 * Creates a new container.
 *
 */
public class CreateContainerCmdImpl extends AbstrDockerCmd<CreateContainerCmd, CreateContainerResponse> implements CreateContainerCmd  {

	private String name;
	
	@JsonProperty("Hostname")     private String    hostName = "";
    @JsonProperty("User")         private String    user = "";
    @JsonProperty("Memory")       private long      memoryLimit = 0;
    @JsonProperty("MemorySwap")   private long      memorySwap = 0;
    @JsonProperty("CpuShares")    private int       cpuShares = 0;
    @JsonProperty("AttachStdin")  private boolean   attachStdin = false;
    @JsonProperty("AttachStdout") private boolean   attachStdout = false;
    @JsonProperty("AttachStderr") private boolean   attachStderr = false;
    @JsonProperty("PortSpecs")    private String[]  portSpecs;
    @JsonProperty("Tty")          private boolean   tty = false;
    @JsonProperty("OpenStdin")    private boolean   stdinOpen = false;
    @JsonProperty("StdinOnce")    private boolean   stdInOnce = false;
    @JsonProperty("Env")          private String[]  env;
    @JsonProperty("Cmd")          private String[]  cmd;
    @JsonProperty("Entrypoint")   private String[]  entrypoint;
    @JsonProperty("Image")        private String    image;
    @JsonProperty("Volumes")      private Volumes volumes = new Volumes();
    @JsonProperty("WorkingDir")   private String workingDir = "";
    @JsonProperty("DisableNetwork") private boolean disableNetwork = false;
    @JsonProperty("ExposedPorts")   private ExposedPorts exposedPorts = new ExposedPorts();
    @JsonProperty("HostConfig")   private HostConfig hostConfig = new HostConfig();
	
	public CreateContainerCmdImpl(CreateContainerCmd.Exec exec, String image) {
		super(exec);
		checkNotNull(image, "image was not specified");
		withImage(image);
	}

	@Override
	public CreateContainerCmdImpl withName(String name) {
		checkNotNull(name, "name was not specified");
        this.name = name;
        return this;
    }

    @Override
	public String getName() {
        return name;
    }

    @Override
	public CreateContainerCmdImpl withExposedPorts(ExposedPort... exposedPorts) {
        this.exposedPorts = new ExposedPorts(exposedPorts);
        return this;
    }

    @Override
	@JsonIgnore
    public ExposedPort[] getExposedPorts() {
        return exposedPorts.getExposedPorts();
    }


    @Override
	public boolean isDisableNetwork() {
        return disableNetwork;
    }

    @Override
	public String getWorkingDir() { 
    	return workingDir;
    }

    @Override
	public CreateContainerCmdImpl withWorkingDir(String workingDir) {
        this.workingDir = workingDir;
        return this;
    }


    @Override
	public String getHostName() {
        return hostName;
    }

    @Override
	public CreateContainerCmdImpl withDisableNetwork(boolean disableNetwork) {
        this.disableNetwork = disableNetwork;
        return this;
    }

    @Override
	public CreateContainerCmdImpl withHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    @Override
	public String[] getPortSpecs() {
        return portSpecs;
    }

    @Override
	public CreateContainerCmdImpl withPortSpecs(String... portSpecs) {
        this.portSpecs = portSpecs;
        return this;
    }

    @Override
	public String getUser() {
        return user;
    }

    @Override
	public CreateContainerCmdImpl withUser(String user) {
        this.user = user;
        return this;
    }

    @Override
	public boolean isTty() {
        return tty;
    }

    @Override
	public CreateContainerCmdImpl withTty(boolean tty) {
        this.tty = tty;
        return this;
    }

    @Override
	public boolean isStdinOpen() {
        return stdinOpen;
    }

    @Override
	public CreateContainerCmdImpl withStdinOpen(boolean stdinOpen) {
        this.stdinOpen = stdinOpen;
        return this;
    }

    @Override
	public boolean isStdInOnce() {
        return stdInOnce;
    }

    @Override
	public CreateContainerCmdImpl withStdInOnce(boolean stdInOnce) {
        this.stdInOnce = stdInOnce;
        return this;
    }

    @Override
	public long getMemoryLimit() {
        return memoryLimit;
    }

    @Override
	public CreateContainerCmdImpl withMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
        return this;
    }

    @Override
	public long getMemorySwap() {
        return memorySwap;
    }

    @Override
	public CreateContainerCmdImpl withMemorySwap(long memorySwap) {
        this.memorySwap = memorySwap;
        return this;
    }

    @Override
	public int getCpuShares() {
        return cpuShares;
    }

    @Override
	public CreateContainerCmdImpl withCpuShares(int cpuShares) {
        this.cpuShares = cpuShares;
        return this;
    }

    @Override
	public boolean isAttachStdin() {
        return attachStdin;
    }

    @Override
	public CreateContainerCmdImpl withAttachStdin(boolean attachStdin) {
        this.attachStdin = attachStdin;
        return this;
    }

    @Override
	public boolean isAttachStdout() {
        return attachStdout;
    }

    @Override
	public CreateContainerCmdImpl withAttachStdout(boolean attachStdout) {
        this.attachStdout = attachStdout;
        return this;
    }

    @Override
	public boolean isAttachStderr() {
        return attachStderr;
    }

    @Override
	public CreateContainerCmdImpl withAttachStderr(boolean attachStderr) {
        this.attachStderr = attachStderr;
        return this;
    }

    @Override
	public String[] getEnv() {
        return env;
    }

    @Override
	public CreateContainerCmdImpl withEnv(String... env) {
        this.env = env;
        return this;
    }

    @Override
	public String[] getCmd() {
        return cmd;
    }

    @Override
	public CreateContainerCmdImpl withCmd(String... cmd) {
        this.cmd = cmd;
        return this;
    }
    
    @Override
    public String[] getEntrypoint() {
    	return entrypoint;
    }
    
    @Override
	public CreateContainerCmdImpl withEntrypoint(String... entrypoint) {
        this.entrypoint = entrypoint;
        return this;
    }

    @Override
    public String[] getDns() {
        return hostConfig.getDns();
    }

    @Override
    public CreateContainerCmdImpl withDns(String... dns) {
        hostConfig.setDns(dns);
        return this;
    }

    @Override
	public String getImage() {
        return image;
    }

    @Override
	public CreateContainerCmdImpl withImage(String image) {
        this.image = image;
        return this;
    }

    @Override
	@JsonIgnore
    public Volume[] getVolumes() {
        return volumes.getVolumes();
    }

    @Override
	public CreateContainerCmdImpl withVolumes(Volume... volumes) {
        this.volumes = new Volumes(volumes);
        return this;
    }

    @Override
	public VolumesFrom[] getVolumesFrom() {
        return hostConfig.getVolumesFrom();
    }

    @Override
	public CreateContainerCmdImpl withVolumesFrom(VolumesFrom... volumesFrom) {
        this.hostConfig.setVolumesFrom(volumesFrom);
        return this;
    }

    @Override
    public HostConfig getHostConfig() {
    	return hostConfig;
    }
    
    @Override
    public CreateContainerCmd withHostConfig(HostConfig hostConfig) {
    	checkNotNull(hostConfig, "no host config was specified");
    	this.hostConfig = hostConfig;
    	return this;
    }
    
    @Override
    public Capability[] getCapAdd() {
        return hostConfig.getCapAdd();
    }

    @Override
    public CreateContainerCmd withCapAdd(Capability... capAdd) {
        hostConfig.setCapAdd(capAdd);
        return this;
    }

    @Override
    public Capability[] getCapDrop() {
        return hostConfig.getCapDrop();
    }

    @Override
    public CreateContainerCmd withCapDrop(Capability... capDrop) {
        hostConfig.setCapDrop(capDrop);
        return this;
    }

	@Override
    public String toString() {
        return new ToStringBuilder(this).append("create container ")
            .append(name != null ? "name=" + name + " " : "")
            .append(this)
            .toString();
    }
    
    /**
     * @throws NotFoundException No such container
     * @throws ConflictException Named container already exists
     */
	@Override
    public CreateContainerResponse exec() throws NotFoundException, ConflictException {
    	return super.exec();
    }

}    
