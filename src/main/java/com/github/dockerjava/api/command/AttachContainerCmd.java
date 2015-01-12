package com.github.dockerjava.api.command;

import java.io.InputStream;

import com.github.dockerjava.api.NotFoundException;

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
public interface AttachContainerCmd extends DockerCmd<InputStream>{

	public String getContainerId();

	public boolean hasLogsEnabled();

	public boolean hasFollowStreamEnabled();

	public boolean hasTimestampsEnabled();

	public boolean hasStdoutEnabled();

	public boolean hasStderrEnabled();

	public AttachContainerCmd withContainerId(String containerId);

	public AttachContainerCmd withFollowStream();

	public AttachContainerCmd withFollowStream(boolean followStream);

	public AttachContainerCmd withTimestamps(boolean timestamps);

	public AttachContainerCmd withStdOut();

	public AttachContainerCmd withStdOut(boolean stdout);

	public AttachContainerCmd withStdErr();

	public AttachContainerCmd withStdErr(boolean stderr);

	public AttachContainerCmd withLogs(boolean logs);
	
	public AttachContainerCmd withLogs();

	/**
	 * @throws NotFoundException No such container 
	 */
	@Override
	public InputStream exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<AttachContainerCmd, InputStream> {
	}

}