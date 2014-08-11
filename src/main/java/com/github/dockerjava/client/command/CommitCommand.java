package com.github.dockerjava.client.command;

import static javax.ws.rs.client.Entity.entity;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.api.model.ExposedPorts;
import com.github.dockerjava.api.model.Volumes;
import com.google.common.base.Preconditions;

/**
 *
 * Create a new image from a container's changes. Returns the new image ID.
 *
 */
public class CommitCommand extends AbstrDockerCmd<CommitCommand, String> implements CommitCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommitCommand.class);

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


	public CommitCommand(String containerId) {
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public CommitCommand withContainerId(String containerId) {
    	Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}


    @Override
	public String getRepository() {
        return repository;
    }

    @Override
	public String getTag() {
        return tag;
    }

    @Override
	public String getMessage() {
        return message;
    }

    @Override
	public String getAuthor() {
        return author;
    }

    @Override
	public boolean hasPauseEnabled() {
        return pause;
    }
    
    

	@Override
	public CommitCommand withAttachStderr(boolean attachStderr) {
		this.attachStderr = attachStderr;
		return this;
	}

	@Override
	public CommitCommand withAttachStderr() {
		return withAttachStderr(true);
	}

	@Override
	public CommitCommand withAttachStdin(boolean attachStdin) {
		this.attachStdin = attachStdin;
		return this;
	}

	@Override
	public CommitCommand withAttachStdin() {
		return withAttachStdin(true);
	}

	@Override
	public CommitCommand withAttachStdout(boolean attachStdout) {
		this.attachStdout = attachStdout;
		return this;
	}

	@Override
	public CommitCommand withAttachStdout() {
		return withAttachStdout(true);
	}

	@Override
	public CommitCommand withCmd(String... cmd) {
		Preconditions.checkNotNull(cmd, "cmd was not specified");
		this.cmd = cmd;
		return this;
	}

	@Override
	public CommitCommand withDisableNetwork(boolean disableNetwork) {
		this.disableNetwork = disableNetwork;
		return this;
	}

	@Override
	public CommitCommand withAuthor(String author) {
		Preconditions.checkNotNull(author, "author was not specified");
		this.author = author;
		return this;
	}

	@Override
	public CommitCommand withMessage(String message) {
		Preconditions.checkNotNull(message, "message was not specified");
		this.message = message;
		return this;
	}

	@Override
	public CommitCommand withTag(String tag) {
		Preconditions.checkNotNull(tag, "tag was not specified");
		this.tag = tag;
		return this;
	}

	@Override
	public CommitCommand withRepository(String repository) {
		Preconditions.checkNotNull(repository, "repository was not specified");
		this.repository = repository;
		return this;
	}

	@Override
	public CommitCommand withPause(boolean pause) {
		this.pause = pause;
		return this;
	}
	
	@Override
	public String[] getEnv() {
        return env;
    }

    @Override
	public CommitCommand withEnv(String... env) {
    	Preconditions.checkNotNull(env, "env was not specified");
        this.env = env;
        return this;
    }

    @Override
	public ExposedPorts getExposedPorts() {
        return exposedPorts;
    }

    @Override
	public CommitCommand withExposedPorts(ExposedPorts exposedPorts) {
    	Preconditions.checkNotNull(exposedPorts, "exposedPorts was not specified");
        this.exposedPorts = exposedPorts;
        return this;
    }

    @Override
	public String getHostname() {
        return hostname;
    }

    @Override
	public CommitCommand withHostname(String hostname) {
    	Preconditions.checkNotNull(hostname, "hostname was not specified");
        this.hostname = hostname;
        return this;
    }

    @Override
	public Integer getMemory() {
        return memory;
    }

    @Override
	public CommitCommand withMemory(Integer memory) {
    	Preconditions.checkNotNull(memory, "memory was not specified");
        this.memory = memory;
        return this;
    }

    @Override
	public Integer getMemorySwap() {
        return memorySwap;
    }

    @Override
	public CommitCommand withMemorySwap(Integer memorySwap) {
    	Preconditions.checkNotNull(memorySwap, "memorySwap was not specified");
        this.memorySwap = memorySwap;
        return this;
    }

    @Override
	public boolean isOpenStdin() {
        return openStdin;
    }

    @Override
	public CommitCommand withOpenStdin(boolean openStdin) {
    	Preconditions.checkNotNull(openStdin, "openStdin was not specified");
        this.openStdin = openStdin;
        return this;
    }

    @Override
	public String[] getPortSpecs() {
        return portSpecs;
    }

    @Override
	public CommitCommand withPortSpecs(String... portSpecs) {
    	Preconditions.checkNotNull(portSpecs, "portSpecs was not specified");
        this.portSpecs = portSpecs;
        return this;
    }

    @Override
	public boolean isStdinOnce() {
        return stdinOnce;
    }

    @Override
	public CommitCommand withStdinOnce(boolean stdinOnce) {
        this.stdinOnce = stdinOnce;
        return this;
    }
    
    @Override
	public CommitCommand withStdinOnce() {
    	return withStdinOnce(true);
    }

    @Override
	public boolean isTty() {
        return tty;
    }

    @Override
	public CommitCommand withTty(boolean tty) {
        this.tty = tty;
        return this;
    }
    
    @Override
	public CommitCommand withTty() {
    	return withTty(true);
    }

    @Override
	public String getUser() {
        return user;
    }

    @Override
	public CommitCommand withUser(String user) {
    	Preconditions.checkNotNull(user, "user was not specified");
        this.user = user;
        return this;
    }

    @Override
	public Volumes getVolumes() {
        return volumes;
    }

    @Override
	public CommitCommand withVolumes(Volumes volumes) {
    	Preconditions.checkNotNull(volumes, "volumes was not specified");
        this.volumes = volumes;
        return this;
    }

    @Override
	public String getWorkingDir() {
        return workingDir;
    }

    @Override
	public CommitCommand withWorkingDir(String workingDir) {
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

        WebTarget webResource = baseResource.path("/commit")
                .queryParam("container", containerId)
                .queryParam("repo", repository)
                .queryParam("tag", tag)
                .queryParam("m", message)
                .queryParam("author", author)
                .queryParam("pause", pause ? "1" : "0");
		
		LOGGER.trace("POST: {}", webResource);
		ObjectNode objectNode = webResource.request().accept("application/vnd.docker.raw-stream").post(entity(this, MediaType.APPLICATION_JSON), ObjectNode.class);
        return objectNode.get("Id").asText();
	
	}
}
