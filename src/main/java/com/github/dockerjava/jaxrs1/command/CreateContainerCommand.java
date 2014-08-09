package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.github.dockerjava.client.model.ExposedPort;
import com.github.dockerjava.client.model.ExposedPorts;
import com.github.dockerjava.client.model.Volume;
import com.github.dockerjava.client.model.Volumes;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 *
 * Creates a new container.
 *
 */
public class CreateContainerCommand extends AbstrDockerCmd<CreateContainerCommand, CreateContainerResponse> implements CreateContainerCmd  {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateContainerCommand.class);
	
	private String name;
	
	@JsonProperty("Hostname")     private String    hostName = "";
    @JsonProperty("User")         private String    user = "";
    @JsonProperty("Memory")       private long      memoryLimit = 0;
    @JsonProperty("MemorySwap")   private long      memorySwap = 0;
    @JsonProperty("AttachStdin")  private boolean   attachStdin = false;
    @JsonProperty("AttachStdout") private boolean   attachStdout = false;
    @JsonProperty("AttachStderr") private boolean   attachStderr = false;
    @JsonProperty("PortSpecs")    private String[]  portSpecs;
    @JsonProperty("Tty")          private boolean   tty = false;
    @JsonProperty("OpenStdin")    private boolean   stdinOpen = false;
    @JsonProperty("StdinOnce")    private boolean   stdInOnce = false;
    @JsonProperty("Env")          private String[]  env;
    @JsonProperty("Cmd")          private String[]  cmd;
    @JsonProperty("Dns")          private String[]  dns;
    @JsonProperty("Image")        private String    image;
    @JsonProperty("Volumes")      private Volumes volumes = new Volumes();
    @JsonProperty("VolumesFrom")  private String[]    volumesFrom = new String[]{};
    @JsonProperty("WorkingDir")   private String workingDir = "";
    @JsonProperty("DisableNetwork") private boolean disableNetwork = false;
    @JsonProperty("ExposedPorts")   private ExposedPorts exposedPorts = new ExposedPorts();
	
	public CreateContainerCommand(String image) {
		Preconditions.checkNotNull(image, "image was not specified");
		withImage(image);
	}

	@Override
	public CreateContainerCommand withName(String name) {
		Preconditions.checkNotNull(name, "name was not specified");
        this.name = name;
        return this;
    }

    @Override
	public String getName() {
        return name;
    }

    @Override
	public CreateContainerCommand withExposedPorts(ExposedPort... exposedPorts) {
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
	public CreateContainerCommand withWorkingDir(String workingDir) {
        this.workingDir = workingDir;
        return this;
    }


    @Override
	public String getHostName() {
        return hostName;
    }

    @Override
	public CreateContainerCommand withDisableNetwork(boolean disableNetwork) {
        this.disableNetwork = disableNetwork;
        return this;
    }

    @Override
	public CreateContainerCommand withHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    @Override
	public String[] getPortSpecs() {
        return portSpecs;
    }

    @Override
	public CreateContainerCommand withPortSpecs(String... portSpecs) {
        this.portSpecs = portSpecs;
        return this;
    }

    @Override
	public String getUser() {
        return user;
    }

    @Override
	public CreateContainerCommand withUser(String user) {
        this.user = user;
        return this;
    }

    @Override
	public boolean isTty() {
        return tty;
    }

    @Override
	public CreateContainerCommand withTty(boolean tty) {
        this.tty = tty;
        return this;
    }

    @Override
	public boolean isStdinOpen() {
        return stdinOpen;
    }

    @Override
	public CreateContainerCommand withStdinOpen(boolean stdinOpen) {
        this.stdinOpen = stdinOpen;
        return this;
    }

    @Override
	public boolean isStdInOnce() {
        return stdInOnce;
    }

    @Override
	public CreateContainerCommand withStdInOnce(boolean stdInOnce) {
        this.stdInOnce = stdInOnce;
        return this;
    }

    @Override
	public long getMemoryLimit() {
        return memoryLimit;
    }

    @Override
	public CreateContainerCommand withMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
        return this;
    }

    @Override
	public long getMemorySwap() {
        return memorySwap;
    }

    @Override
	public CreateContainerCommand withMemorySwap(long memorySwap) {
        this.memorySwap = memorySwap;
        return this;
    }


    @Override
	public boolean isAttachStdin() {
        return attachStdin;
    }

    @Override
	public CreateContainerCommand withAttachStdin(boolean attachStdin) {
        this.attachStdin = attachStdin;
        return this;
    }

    @Override
	public boolean isAttachStdout() {
        return attachStdout;
    }

    @Override
	public CreateContainerCommand withAttachStdout(boolean attachStdout) {
        this.attachStdout = attachStdout;
        return this;
    }

    @Override
	public boolean isAttachStderr() {
        return attachStderr;
    }

    @Override
	public CreateContainerCommand withAttachStderr(boolean attachStderr) {
        this.attachStderr = attachStderr;
        return this;
    }

    @Override
	public String[] getEnv() {
        return env;
    }

    @Override
	public CreateContainerCommand withEnv(String... env) {
        this.env = env;
        return this;
    }

    @Override
	public String[] getCmd() {
        return cmd;
    }

    @Override
	public CreateContainerCommand withCmd(String... cmd) {
        this.cmd = cmd;
        return this;
    }

    @Override
	public String[] getDns() {
        return dns;
    }

    @Override
	public CreateContainerCommand withDns(String... dns) {
        this.dns = dns;
        return this;
    }

    @Override
	public String getImage() {
        return image;
    }

    @Override
	public CreateContainerCommand withImage(String image) {
        this.image = image;
        return this;
    }

    @Override
	@JsonIgnore
    public Volume[] getVolumes() {
        return volumes.getVolumes();
    }

    @Override
	public CreateContainerCommand withVolumes(Volume... volumes) {
        this.volumes = new Volumes(volumes);
        return this;
    }

    @Override
	public String[] getVolumesFrom() {
        return volumesFrom;
    }

    @Override
	public CreateContainerCommand withVolumesFrom(String... volumesFrom) {
        this.volumesFrom = volumesFrom;
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

	protected CreateContainerResponse impl() {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		if (name != null) {
			params.add("name", name);
		}
		WebResource webResource = baseResource.path("/containers/create").queryParams(params);
		
		LOGGER.trace("POST: {} ", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(CreateContainerResponse.class, this);
	}

}    
