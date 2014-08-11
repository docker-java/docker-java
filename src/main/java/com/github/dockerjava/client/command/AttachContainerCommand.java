package com.github.dockerjava.client.command;

import java.io.InputStream;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.AttachContainerCmd;
import com.google.common.base.Preconditions;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.entity;

/**
 * Attach to container
 * 
 * @param logs
 *            - true or false, includes logs. Defaults to false.
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
public class AttachContainerCommand extends	AbstrDockerCmd<AttachContainerCommand, InputStream> implements AttachContainerCmd {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AttachContainerCommand.class);

	private String containerId;

	private boolean logs, followStream, timestamps, stdout, stderr;

	public AttachContainerCommand(String containerId) {
		withContainerId(containerId);
	}

	@Override
	public String getContainerId() {
		return containerId;
	}

	@Override
	public boolean hasLogsEnabled() {
		return logs;
	}

	@Override
	public boolean hasFollowStreamEnabled() {
		return followStream;
	}

	@Override
	public boolean hasTimestampsEnabled() {
		return timestamps;
	}

	@Override
	public boolean hasStdoutEnabled() {
		return stdout;
	}

	@Override
	public boolean hasStderrEnabled() {
		return stderr;
	}

	@Override
	public AttachContainerCmd withContainerId(String containerId) {
		Preconditions
				.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
	public AttachContainerCmd withFollowStream() {
		return withFollowStream(true);
	}

	@Override
	public AttachContainerCmd withFollowStream(boolean followStream) {
		this.followStream = followStream;
		return this;
	}

	@Override
	public AttachContainerCmd withTimestamps(boolean timestamps) {
		this.timestamps = timestamps;
		return this;
	}

	@Override
	public AttachContainerCmd withStdOut() {
		return withStdOut(true);
	}

	@Override
	public AttachContainerCmd withStdOut(boolean stdout) {
		this.stdout = stdout;
		return this;
	}

	@Override
	public AttachContainerCmd withStdErr() {
		return withStdErr(true);
	}

	@Override
	public AttachContainerCmd withStdErr(boolean stderr) {
		this.stderr = stderr;
		return this;
	}

	@Override
	public AttachContainerCmd withLogs(boolean logs) {
		this.logs = logs;
		return this;
	}
	
	/**
	 * @throws NotFoundException No such container 
	 */
	@Override
	public InputStream exec() throws NotFoundException {
		return super.exec();
	}

	protected InputStream impl() throws DockerException {
		WebTarget webResource = baseResource.path("/containers/{id}/attach")
                .resolveTemplate("{id}", containerId)
                .queryParam("logs", logs ? "1" : "0")
                .queryParam("timestamps", timestamps ? "1" : "0")
                .queryParam("stdout", stdout ? "1" : "0")
                .queryParam("stderr", stderr ? "1" : "0")
                .queryParam("follow", followStream ? "1" : "0");

		LOGGER.trace("POST: {}", webResource);
		return webResource.request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
				.post(entity(null, MediaType.APPLICATION_JSON), Response.class).readEntity(InputStream.class);
	}
}
