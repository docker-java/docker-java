package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

import java.io.InputStream;

public interface ExecStartCmd extends DockerCmd<InputStream> {

	public String getExecId();

	public ExecStartCmd withExecId(String execId);

	public boolean hasDetachEnabled();

	public ExecStartCmd withDetach(boolean detach);
	
	public ExecStartCmd withDetach();

	public boolean hasTtyEnabled();

	public ExecStartCmd withTty(boolean tty);
	
	public ExecStartCmd withTty();

	/**
	 * @throws com.github.dockerjava.api.NotFoundException
	 *             No such exec instance
	 */
	@Override
	public InputStream exec() throws NotFoundException;

	public static interface Exec extends
			DockerCmdExec<ExecStartCmd, InputStream> {
	}
}
