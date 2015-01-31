package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.DockerCmd;
import com.github.dockerjava.api.command.DockerCmdExec;

public abstract class AbstrDockerCmd<CMD_T extends DockerCmd<RES_T>, RES_T> implements DockerCmd<RES_T> {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstrDockerCmd.class);

	protected DockerCmdExec<CMD_T, RES_T> execution;
	
	public AbstrDockerCmd(DockerCmdExec<CMD_T, RES_T> execution) {
		checkNotNull(execution, "execution was not specified");
		this.execution = execution;
	}

    @Override
    @SuppressWarnings("unchecked")
	public RES_T exec() throws DockerException {
		LOGGER.debug("Cmd: {}", this);
		return execution.exec((CMD_T)this);
	}

    @Override
	public void close() throws IOException {}
}
