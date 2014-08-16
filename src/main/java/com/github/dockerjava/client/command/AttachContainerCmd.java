package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.entity;

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
public class AttachContainerCmd extends	AbstrDockerCmd<AttachContainerCmd, Response> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AttachContainerCmd.class);

	private String containerId;

	private boolean logs, followStream, timestamps, stdout, stderr;

	public AttachContainerCmd(String containerId) {
		withContainerId(containerId);
	}

    public String getContainerId() {
        return containerId;
    }

    public boolean hasLogsEnabled() {
        return logs;
    }

    public boolean hasFollowStreamEnabled() {
        return followStream;
    }

    public boolean hasTimestampsEnabled() {
        return timestamps;
    }

    public boolean hasStdoutEnabled() {
        return stdout;
    }

    public boolean hasStderrEnabled() {
        return stderr;
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

	protected Response impl() throws DockerException {
		WebTarget webResource = baseResource.path("/containers/{id}/attach")
                .resolveTemplate("{id}", containerId)
                .queryParam("logs", logs ? "1" : "0")
                .queryParam("timestamps", timestamps ? "1" : "0")
                .queryParam("stdout", stdout ? "1" : "0")
                .queryParam("stderr", stderr ? "1" : "0")
                .queryParam("follow", followStream ? "1" : "0");

		try {
			LOGGER.trace("POST: {}", webResource);
			return webResource.request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
					.post(entity(null, MediaType.APPLICATION_JSON), Response.class);
		} catch (ClientErrorException exception) {
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
