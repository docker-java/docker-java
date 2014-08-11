package com.github.dockerjava.client.command;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.google.common.base.Preconditions;

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
public class LogContainerCommand extends AbstrDockerCmd<LogContainerCommand, InputStream> implements LogContainerCmd {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LogContainerCommand.class);

	private String containerId;

	private int tail = -1;

	private boolean followStream, timestamps, stdout, stderr;

	public LogContainerCommand(String containerId) {
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public int getTail() {
        return tail;
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
	public LogContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
	public LogContainerCmd withFollowStream() {
		return withFollowStream(true);
	}

	@Override
	public LogContainerCmd withFollowStream(boolean followStream) {
		this.followStream = followStream;
		return this;
	}
	
	@Override
	public LogContainerCmd withTimestamps() {
		return withTimestamps(true);
	}

	@Override
	public LogContainerCmd withTimestamps(boolean timestamps) {
		this.timestamps = timestamps;
		return this;
	}

	@Override
	public LogContainerCmd withStdOut() {
		return withStdOut(true);
	}

	@Override
	public LogContainerCmd withStdOut(boolean stdout) {
		this.stdout = stdout;
		return this;
	}

	@Override
	public LogContainerCmd withStdErr() {
		return withStdErr(true);
	}

	@Override
	public LogContainerCmd withStdErr(boolean stderr) {
		this.stderr = stderr;
		return this;
	}

	@Override
	public LogContainerCmd withTailAll() {
		this.tail = -1;
		return this;
	}


	@Override
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
    
    /**
     * @throws NotFoundException No such container
     */
	@Override
    public InputStream exec() throws NotFoundException {
    	return super.exec();
    }


	protected InputStream impl() throws DockerException {

        WebTarget webResource = baseResource.path("/containers/{id}/logs")
                        .resolveTemplate("id", containerId)
                        .queryParam("timestamps", timestamps ? "1" : "0")
                        .queryParam("stdout", stdout ? "1" : "0")
                        .queryParam("stderr", stderr ? "1" : "0")
                        .queryParam("follow", followStream ? "1" : "0")
                        .queryParam("tail", tail < 0 ? "all" : "" + tail);

		LOGGER.trace("GET: {}", webResource);
		return webResource.request().get(Response.class).readEntity(InputStream.class);
	}
}
