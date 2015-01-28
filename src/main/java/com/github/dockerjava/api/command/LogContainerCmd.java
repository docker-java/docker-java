package com.github.dockerjava.api.command;

import java.io.InputStream;

import com.github.dockerjava.api.NotFoundException;

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
public interface LogContainerCmd extends DockerCmd<InputStream>{

	public String getContainerId();

	public int getTail();

	public boolean hasFollowStreamEnabled();

	public boolean hasTimestampsEnabled();

	public boolean hasStdoutEnabled();

	public boolean hasStderrEnabled();

	public LogContainerCmd withContainerId(String containerId);

	public LogContainerCmd withFollowStream();

	public LogContainerCmd withFollowStream(boolean followStream);

	public LogContainerCmd withTimestamps();
	
	public LogContainerCmd withTimestamps(boolean timestamps);

	public LogContainerCmd withStdOut();

	public LogContainerCmd withStdOut(boolean stdout);

	public LogContainerCmd withStdErr();

	public LogContainerCmd withStdErr(boolean stderr);

	public LogContainerCmd withTailAll();

	public LogContainerCmd withTail(int tail);

	/**
	 * @throws NotFoundException No such container
	 */
	@Override
	public InputStream exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<LogContainerCmd, InputStream> {
	}


}