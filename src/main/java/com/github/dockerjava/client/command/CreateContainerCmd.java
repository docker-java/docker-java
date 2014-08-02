package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.client.model.*;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


/**
 *
 * Creates a new container.
 *
 */
public class CreateContainerCmd extends AbstrDockerCmd<CreateContainerCmd, CreateContainerResponse>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateContainerCmd.class);
	
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
	
	public CreateContainerCmd(String image) {
		Preconditions.checkNotNull(image, "image was not specified");
		withImage(image);
	}

	public CreateContainerCmd withName(String name) {
		Preconditions.checkNotNull(name, "name was not specified");
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts) {
        this.exposedPorts = new ExposedPorts(exposedPorts);
        return this;
    }

    @JsonIgnore
    public ExposedPort[] getExposedPorts() {
        return exposedPorts.getExposedPorts();
    }


    public boolean isDisableNetwork() {
        return disableNetwork;
    }

    public String getWorkingDir() { 
    	return workingDir;
    }

    public CreateContainerCmd withWorkingDir(String workingDir) {
        this.workingDir = workingDir;
        return this;
    }


    public String getHostName() {
        return hostName;
    }

    public CreateContainerCmd withDisableNetwork(boolean disableNetwork) {
        this.disableNetwork = disableNetwork;
        return this;
    }

    public CreateContainerCmd withHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public String[] getPortSpecs() {
        return portSpecs;
    }

    public CreateContainerCmd withPortSpecs(String... portSpecs) {
        this.portSpecs = portSpecs;
        return this;
    }

    public String getUser() {
        return user;
    }

    public CreateContainerCmd withUser(String user) {
        this.user = user;
        return this;
    }

    public boolean isTty() {
        return tty;
    }

    public CreateContainerCmd withTty(boolean tty) {
        this.tty = tty;
        return this;
    }

    public boolean isStdinOpen() {
        return stdinOpen;
    }

    public CreateContainerCmd withStdinOpen(boolean stdinOpen) {
        this.stdinOpen = stdinOpen;
        return this;
    }

    public boolean isStdInOnce() {
        return stdInOnce;
    }

    public CreateContainerCmd withStdInOnce(boolean stdInOnce) {
        this.stdInOnce = stdInOnce;
        return this;
    }

    public long getMemoryLimit() {
        return memoryLimit;
    }

    public CreateContainerCmd withMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
        return this;
    }

    public long getMemorySwap() {
        return memorySwap;
    }

    public CreateContainerCmd withMemorySwap(long memorySwap) {
        this.memorySwap = memorySwap;
        return this;
    }


    public boolean isAttachStdin() {
        return attachStdin;
    }

    public CreateContainerCmd withAttachStdin(boolean attachStdin) {
        this.attachStdin = attachStdin;
        return this;
    }

    public boolean isAttachStdout() {
        return attachStdout;
    }

    public CreateContainerCmd withAttachStdout(boolean attachStdout) {
        this.attachStdout = attachStdout;
        return this;
    }

    public boolean isAttachStderr() {
        return attachStderr;
    }

    public CreateContainerCmd withAttachStderr(boolean attachStderr) {
        this.attachStderr = attachStderr;
        return this;
    }

    public String[] getEnv() {
        return env;
    }

    public CreateContainerCmd withEnv(String... env) {
        this.env = env;
        return this;
    }

    public String[] getCmd() {
        return cmd;
    }

    public CreateContainerCmd withCmd(String... cmd) {
        this.cmd = cmd;
        return this;
    }

    public String[] getDns() {
        return dns;
    }

    public CreateContainerCmd withDns(String... dns) {
        this.dns = dns;
        return this;
    }

    public String getImage() {
        return image;
    }

    public CreateContainerCmd withImage(String image) {
        this.image = image;
        return this;
    }

    @JsonIgnore
    public Volume[] getVolumes() {
        return volumes.getVolumes();
    }

    public CreateContainerCmd withVolumes(Volume... volumes) {
        this.volumes = new Volumes(volumes);
        return this;
    }

    public String[] getVolumesFrom() {
        return volumesFrom;
    }

    public CreateContainerCmd withVolumesFrom(String... volumesFrom) {
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

	protected CreateContainerResponse impl() {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		if (name != null) {
			params.add("name", name);
		}
		WebResource webResource = baseResource.path("/containers/create").queryParams(params);

		try {
			LOGGER.trace("POST: {} ", webResource);
			return webResource.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
					.post(CreateContainerResponse.class, this);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("%s is an unrecognized image. Please pull the image first.", getImage()));
			} else if (exception.getResponse().getStatus() == 406) {
				throw new DockerException("impossible to attach (container not running)");
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}

}    
