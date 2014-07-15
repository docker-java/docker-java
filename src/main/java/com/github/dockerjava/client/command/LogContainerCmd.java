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
 * Get container logs
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
 * @param tail
 * 			  - `all` or `<number>`, Output specified number of lines at the end of logs
 */
public class LogContainerCmd extends AbstrDockerCmd<LogContainerCmd, ClientResponse> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LogContainerCmd.class);

	private String containerId;
	
	private int tail = -1;

	private boolean followStream, timestamps, stdout, stderr;

	public LogContainerCmd(String containerId) {
		withContainerId(containerId);
	}

	public LogContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	public LogContainerCmd withFollowStream() {
		return withFollowStream(true);
	}

	public LogContainerCmd withFollowStream(boolean followStream) {
		this.followStream = followStream;
		return this;
	}

	public LogContainerCmd withTimestamps(boolean timestamps) {
		this.timestamps = timestamps;
		return this;
	}

	public LogContainerCmd withStdOut() {
		return withStdOut(true);
	}

	public LogContainerCmd withStdOut(boolean stdout) {
		this.stdout = stdout;
		return this;
	}

	public LogContainerCmd withStdErr() {
		return withStdErr(true);
	}

	public LogContainerCmd withStdErr(boolean stderr) {
		this.stderr = stderr;
		return this;
	}
	
	public LogContainerCmd withTailAll() {
		this.tail = -1;
		return this;
	}
	
	
	public LogContainerCmd withTail(int tail) {
		this.tail = tail;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("logs ")
            .append(followStream ? "--follow=true" : "")
            .append(timestamps ? "--timestamps=true" : "")
            .append(containerId)
            .toString();
    }	

	protected ClientResponse impl() throws DockerException {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("timestamps", timestamps ? "1" : "0");
		params.add("stdout", stdout ? "1" : "0");
		params.add("stderr", stderr ? "1" : "0");
		params.add("follow", followStream ? "1" : "0"); 
		params.add("tail", tail < 0 ? "all" : ""+ tail); 

		WebResource webResource = baseResource.path(
				String.format("/containers/%s/logs", containerId))
				.queryParams(params);

		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
					.get(ClientResponse.class);
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
