package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.client.model.ExposedPorts;
import com.github.dockerjava.client.model.Volumes;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 *
 * Create a new image from a container's changes. Returns the new image ID.
 *
 */
public class CommitCmd extends AbstrDockerCmd<CommitCmd, String>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommitCmd.class);

	private String containerId, repository, tag, message, author;

	private boolean pause = true;

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


	public CommitCmd(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
	}

    public String getContainerId() {
        return containerId;
    }

    public String getRepository() {
        return repository;
    }

    public String getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public boolean hasPauseEnabled() {
        return pause;
    }

	public CommitCmd withAttachStderr(boolean attachStderr) {
		this.attachStderr = attachStderr;
		return this;
	}

	public CommitCmd withAttachStderr() {
		return withAttachStderr(true);
	}

	public CommitCmd withAttachStdin(boolean attachStdin) {
		this.attachStdin = attachStdin;
		return this;
	}

	public CommitCmd withAttachStdin() {
		return withAttachStdin(true);
	}

	public CommitCmd withAttachStdout(boolean attachStdout) {
		this.attachStdout = attachStdout;
		return this;
	}

	public CommitCmd withAttachStdout() {
		return withAttachStdout(true);
	}

	public CommitCmd withCmd(String... cmd) {
		Preconditions.checkNotNull(cmd, "cmd was not specified");
		this.cmd = cmd;
		return this;
	}

	public CommitCmd withDisableNetwork(boolean disableNetwork) {
		this.disableNetwork = disableNetwork;
		return this;
	}

	public CommitCmd withAuthor(String author) {
		Preconditions.checkNotNull(author, "author was not specified");
		this.author = author;
		return this;
	}

	public CommitCmd withMessage(String message) {
		Preconditions.checkNotNull(message, "message was not specified");
		this.message = message;
		return this;
	}

	public CommitCmd withTag(String tag) {
		Preconditions.checkNotNull(tag, "tag was not specified");
		this.tag = tag;
		return this;
	}

	public CommitCmd withRepository(String repository) {
		Preconditions.checkNotNull(repository, "repository was not specified");
		this.repository = repository;
		return this;
	}

	public CommitCmd withPause(boolean pause) {
		this.pause = pause;
		return this;
	}
	
	public String[] getEnv() {
        return env;
    }

    public CommitCmd withEnv(String... env) {
    	Preconditions.checkNotNull(env, "env was not specified");
        this.env = env;
        return this;
    }

    public ExposedPorts getExposedPorts() {
        return exposedPorts;
    }

    public CommitCmd withExposedPorts(ExposedPorts exposedPorts) {
    	Preconditions.checkNotNull(exposedPorts, "exposedPorts was not specified");
        this.exposedPorts = exposedPorts;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public CommitCmd withHostname(String hostname) {
    	Preconditions.checkNotNull(hostname, "hostname was not specified");
        this.hostname = hostname;
        return this;
    }

    public Integer getMemory() {
        return memory;
    }

    public CommitCmd withMemory(Integer memory) {
    	Preconditions.checkNotNull(memory, "memory was not specified");
        this.memory = memory;
        return this;
    }

    public Integer getMemorySwap() {
        return memorySwap;
    }

    public CommitCmd withMemorySwap(Integer memorySwap) {
    	Preconditions.checkNotNull(memorySwap, "memorySwap was not specified");
        this.memorySwap = memorySwap;
        return this;
    }

    public boolean isOpenStdin() {
        return openStdin;
    }

    public CommitCmd withOpenStdin(boolean openStdin) {
    	Preconditions.checkNotNull(openStdin, "openStdin was not specified");
        this.openStdin = openStdin;
        return this;
    }

    public String[] getPortSpecs() {
        return portSpecs;
    }

    public CommitCmd withPortSpecs(String... portSpecs) {
    	Preconditions.checkNotNull(portSpecs, "portSpecs was not specified");
        this.portSpecs = portSpecs;
        return this;
    }

    public boolean isStdinOnce() {
        return stdinOnce;
    }

    public CommitCmd withStdinOnce(boolean stdinOnce) {
        this.stdinOnce = stdinOnce;
        return this;
    }
    
    public CommitCmd withStdinOnce() {
    	return withStdinOnce(true);
    }

    public boolean isTty() {
        return tty;
    }

    public CommitCmd withTty(boolean tty) {
        this.tty = tty;
        return this;
    }
    
    public CommitCmd withTty() {
    	return withTty(true);
    }

    public String getUser() {
        return user;
    }

    public CommitCmd withUser(String user) {
    	Preconditions.checkNotNull(user, "user was not specified");
        this.user = user;
        return this;
    }

    public Volumes getVolumes() {
        return volumes;
    }

    public CommitCmd withVolumes(Volumes volumes) {
    	Preconditions.checkNotNull(volumes, "volumes was not specified");
        this.volumes = volumes;
        return this;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public CommitCmd withWorkingDir(String workingDir) {
    	Preconditions.checkNotNull(workingDir, "workingDir was not specified");
        this.workingDir = workingDir;
        return this;
    }


	@Override
	public String toString() {
		return new ToStringBuilder(this).append("commit ")
			.append(author != null ? "--author " + author + " " : "")
			.append(message != null ? "--message " + message + " " : "")
			.append(containerId)
			.append(repository != null ?  " " + repository + ":" : " ")
			.append(tag != null ?  tag : "")
			.toString();
	}
	
	/**
     * @throws NotFoundException No such container
     */
	@Override
	public String exec() throws NotFoundException {
		return super.exec();
	}

	protected String impl() throws DockerException {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("container", containerId);
		params.add("repo", repository);
		params.add("tag", tag);
		params.add("m", message);
		params.add("author", author);
		params.add("pause", pause ? "1" : "0");

		WebResource webResource = baseResource.path("/commit").queryParams(params);
		
		LOGGER.trace("POST: {}", webResource);
		ObjectNode objectNode = webResource.queryParams(params).accept("application/vnd.docker.raw-stream").type(MediaType.APPLICATION_JSON).post(ObjectNode.class, this);

		return objectNode.get("Id").asText();
	}

	   
}
