package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.github.dockerjava.client.model.CommitConfig;
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

    public CommitCmd withCommitConfig(CommitConfig commitConfig) {
		checkCommitConfig(commitConfig);
		this.commitConfig = commitConfig;
		return this;
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

	private void checkCommitConfig(CommitConfig commitConfig) {
		Preconditions.checkNotNull(commitConfig, "CommitConfig was not specified");
	}

	protected String impl() throws DockerException {
		checkCommitConfig(commitConfig);

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
}
