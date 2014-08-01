package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.client.model.ExposedPorts;
import com.github.dockerjava.client.model.Volumes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.UniformInterfaceException;
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

	private CommitConfig commitConfig = new CommitConfig();

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
		this.commitConfig.setAttachStderr(attachStderr);
		return this;
	}

	public CommitCmd withAttachStderr() {
		return withAttachStderr(true);
	}

	public CommitCmd withAttachStdin(boolean attachStdin) {
		this.commitConfig.setAttachStdin(attachStdin);
		return this;
	}

	public CommitCmd withAttachStdin() {
		return withAttachStdin(true);
	}

	public CommitCmd withAttachStdout(boolean attachStdout) {
		this.commitConfig.setAttachStdout(attachStdout);
		return this;
	}

	public CommitCmd withAttachStdout() {
		return withAttachStdout(true);
	}

	public CommitCmd withCmd(String... cmd) {
		Preconditions.checkNotNull(cmd, "cmd was not specified");
		this.commitConfig.setCmd(cmd);
		return this;
	}

	public CommitCmd withDisableNetwork(boolean disableNetwork) {
		this.commitConfig.setDisableNetwork(disableNetwork);
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

	@Override
	public String toString() {
		return new StringBuilder("commit ")
			.append(author != null ? "--author " + author + " " : "")
			.append(message != null ? "--message " + message + " " : "")
			.append(containerId)
			.append(repository != null ?  " " + repository + ":" : " ")
			.append(tag != null ?  tag : "")
			.toString();
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

		try {
			LOGGER.trace("POST: {}", webResource);
			ObjectNode objectNode = webResource.queryParams(params).accept("application/vnd.docker.raw-stream").type(MediaType.APPLICATION_JSON).post(ObjectNode.class, commitConfig);
            return objectNode.get("Id").asText();
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		} catch (Exception e) {
			throw new DockerException(e);
		}
	}

    /**
     *
     * @author Konstantin Pelykh (kpelykh@gmail.com)
     *
     */
    // TODO Simplify this
    private static class CommitConfig {

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
}
