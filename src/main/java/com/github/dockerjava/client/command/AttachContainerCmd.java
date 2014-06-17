package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Attach to container
 * 
 * @param logs - true or false, includes logs. Defaults to false.
 * 
 * @param followStream
 *            - true or false, return stream. Defaults to false.
 * @param stdout
 *            - true or false, includes stdout log. Defaults to false.
 * @param stderr
 *            - true or false, includes stderr log. Defaults to false.
 * @param timestamps
 *            - true or false, if true, print timestamps for every log line.
 *            Defaults to false.
 */
public class AttachContainerCmd extends	AbstrDockerCmd<AttachContainerCmd, ClientResponse> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AttachContainerCmd.class);

	private String containerId;

	private boolean logs, followStream, timestamps, stdout, stderr;

	public AttachContainerCmd(String containerId) {
		withContainerId(containerId);
	}

	public AttachContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	public AttachContainerCmd withFollowStream() {
		return withFollowStream(true);
	}

	public AttachContainerCmd withFollowStream(boolean followStream) {
		this.followStream = followStream;
		return this;
	}

	public AttachContainerCmd withTimestamps(boolean timestamps) {
		this.timestamps = timestamps;
		return this;
	}

	public AttachContainerCmd withStdOut() {
		return withStdOut(true);
	}

	public AttachContainerCmd withStdOut(boolean stdout) {
		this.stdout = stdout;
		return this;
	}

	public AttachContainerCmd withStdErr() {
		return withStdErr(true);
	}

	public AttachContainerCmd withStdErr(boolean stderr) {
		this.stderr = stderr;
		return this;
	}

	public AttachContainerCmd withLogs(boolean logs) {
		this.logs = logs;
		return this;
	}

	protected ClientResponse impl() throws DockerException {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("logs", logs ? "1" : "0");
		params.add("timestamps", timestamps ? "1" : "0");
		params.add("stdout", stdout ? "1" : "0");
		params.add("stderr", stderr ? "1" : "0");
		params.add("follow", followStream ? "1" : "0"); 

		WebResource webResource = baseResource.path(
				String.format("/containers/%s/attach", containerId))
				.queryParams(params);

		try {
			LOGGER.trace("POST: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
					.post(ClientResponse.class, params);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 400) {
				throw new DockerException("bad parameter");
			} else if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format(
						"No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}
}
