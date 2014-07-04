package com.github.dockerjava.client.command;

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
	
	private CommitConfig commitConfig;
	
	public CommitCmd(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.commitConfig = new CommitConfig(containerId);
	}
	
	public CommitCmd withCommitConfig(CommitConfig commitConfig) {
		checkCommitConfig(commitConfig);
		this.commitConfig = commitConfig;
		return this;
	}
	
	public CommitCmd withRepo(String repo) {
		Preconditions.checkNotNull(repo, "repo was not specified");
		this.commitConfig.setRepo(repo);
		return this;
	}
	
	public CommitCmd withTag(String tag) {
		Preconditions.checkNotNull(tag, "tag was not specified");
		this.commitConfig.setTag(tag);
		return this;
	}

	public CommitCmd withMessage(String message) {
		Preconditions.checkNotNull(message, "message was not specified");
		this.commitConfig.setMessage(message);
		return this;
	}
	
	public CommitCmd withAuthor(String author) {
		Preconditions.checkNotNull(author, "author was not specified");
		this.commitConfig.setAuthor(author);
		return this;
	}
	
	public CommitCmd withRun(String run) {
		Preconditions.checkNotNull(run, "run was not specified");
		this.commitConfig.setRun(run);
		return this;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("commit ")
			.append(commitConfig.getAuthor() != null ? "--author " + commitConfig.getAuthor() + " " : "")
			.append(commitConfig.getMessage() != null ? "--message " + commitConfig.getMessage() + " " : "")
			.append(commitConfig.getContainerId())
			.append(commitConfig.getRepo() != null ?  " " + commitConfig.getRepo() + ":" : " ")
			.append(commitConfig.getTag() != null ?  commitConfig.getTag() : "")
			.toString();
	}
		
	private void checkCommitConfig(CommitConfig commitConfig) {
		Preconditions.checkNotNull(commitConfig, "CommitConfig was not specified");
	}
	
	protected String impl() throws DockerException {
		checkCommitConfig(commitConfig);
		
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("container", commitConfig.getContainerId());
		params.add("repo", commitConfig.getRepo());
		params.add("tag", commitConfig.getTag());
		params.add("m", commitConfig.getMessage());
		params.add("author", commitConfig.getAuthor());
		params.add("run", commitConfig.getRun());

		WebResource webResource = baseResource.path("/commit").queryParams(params);

		try {
			LOGGER.trace("POST: {}", webResource);
			ObjectNode ObjectNode = webResource.accept("application/vnd.docker.raw-stream").post(ObjectNode.class, params);
            return ObjectNode.get("Id").asText();
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", commitConfig.getContainerId()));
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
