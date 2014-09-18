package com.github.dockerjava.core.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.BadRequestException;
import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotAcceptableException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.command.DockerCmd;
import com.github.dockerjava.api.command.DockerCmdExec;

import com.google.common.base.Preconditions;

import javax.ws.rs.ClientErrorException;

public abstract class AbstrDockerCmd<CMD_T extends DockerCmd<RES_T>, RES_T> implements DockerCmd<RES_T> {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstrDockerCmd.class);

	protected DockerCmdExec<CMD_T, RES_T> execution;
	
	public AbstrDockerCmd(DockerCmdExec<CMD_T, RES_T> execution) {
		Preconditions.checkNotNull(execution, "execution was not specified");
		this.execution = execution;
	}

	@Override
	public RES_T exec() throws DockerException {
		LOGGER.debug("Cmd: {}", this);
		return execution.exec((CMD_T)this);
	}
	
	protected DockerException toDockerException(ClientErrorException exception) {
		LOGGER.info("toDockerException");
		return new DockerException(exception.getMessage(), exception.getResponse().getStatus(), exception);
	} 
}